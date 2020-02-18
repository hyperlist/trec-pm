package de.julielab.ir.ltr;

import at.medunigraz.imi.bst.config.TrecConfig;
import de.julielab.ir.ltr.features.IRScore;
import de.julielab.ir.ltr.features.IRScoreFeatureKey;
import de.julielab.ir.model.QueryDescription;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static de.julielab.ir.ltr.features.TrecPmQueryPart.FULL;

public class TreatmentRanker<Q extends QueryDescription> implements Ranker<Q> {
    private static final File STOPLIST_FILE = new File(TreatmentRanker.class.getResource("/treatment-stoplist.txt").getFile());

    private List<String> stoplist;

    private IRScoreFeatureKey outputScoreType = new IRScoreFeatureKey(IRScore.TREATMENT, FULL);

    private static List<String> loadStoplist(File file) {
        try {
            return FileUtils.readLines(file, "UTF-8");
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read stoplist file.");
        }
    }

    @Override
    public void train(DocumentList<Q> documents) {
        // NOOP
    }

    @Override
    public void train(DocumentList<Q> documents, boolean doValidation, float fraction, int randomSeed) {
        // NOOP
    }

    @Override
    public void load(File modelFile) throws IOException {
        // NOOP
    }

    @Override
    public void save(File modelFile) {
        // NOOP
    }

    @Override
    public DocumentList<Q> rank(DocumentList<Q> documents) {
        DocumentList<Q> ret = new DocumentList<>();

        Map<Integer, List<String>> treatmentsByTopic = new HashMap<>();
        for (Document<Q> document : documents) {
            List<String> treatments = document.getTreatments();
            treatments = stoplistFilter(treatments);

            // Filter repeated treatments for the same topic
            int topic = document.getQueryDescription().getNumber();
            List<String> previousTreatments = treatmentsByTopic.getOrDefault(topic, new ArrayList<>());
            treatments = repeatedFilter(previousTreatments, treatments);

            // Prioritize frequent treatments (e.g. "imatinib" and "nilotinib" for 17363509 - Topic 21)
            treatments = frequencyRanker(treatments);

            // Only add documents if there are any remaining treatments
            if (treatments.size() == 0) {
                continue;
            }

            // Save treatments so they're not repeated
            previousTreatments.addAll(treatments);
            treatmentsByTopic.put(topic, previousTreatments);

            // Copy BM25 score
            document.setScore(outputScoreType, document.getIrScore(new IRScoreFeatureKey(IRScore.BM25, FULL)));
            document.setTreatments(treatments);

            ret.add(document);
        }
        return ret;
    }

    private List<String> stoplistFilter(List<String> treatments) {
        // Lazy initialization
        if (stoplist == null) {
            this.stoplist = loadStoplist(STOPLIST_FILE);
        }

        List<String> ret = new ArrayList<>();
        for (String treatment : treatments) {
            if (!stoplist.contains(treatment)) {
                ret.add(treatment);
            }
        }

        return ret;
    }

    private List<String> repeatedFilter(List<String> previousTreatments, List<String> treatments) {
        List<String> ret = new ArrayList<>();
        for (String treatment : treatments) {
            if (!previousTreatments.contains(treatment)) {
                ret.add(treatment);
            }
        }
        return ret;
    }

    /**
     * Rank treatments based on frequency. Remove duplicates and truncate to MAX_TREATMENTS as a side-effect.
     *
     * @param treatments
     * @return
     */
    private List<String> frequencyRanker(List<String> treatments) {
        // Count treatments
        Map<String, Integer> treatmentCounts = new LinkedHashMap<>();
        for (String treatment : treatments) {
            treatmentCounts.putIfAbsent(treatment, 0);
            treatmentCounts.compute(treatment, (k, v) -> v + 1);
        }

        // Keep k treatments in a heap to avoid n*logn sort operation
        PriorityQueue<Map.Entry<String, Integer>> heap = new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));
        for (Map.Entry<String, Integer> entry : treatmentCounts.entrySet()) {
            heap.offer(entry);
            if (heap.size() > TrecConfig.MAX_TREATMENTS) {
                heap.poll();
            }
        }

        // Re-add treatments in order of increasing frequency
        List<String> ret = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : heap) {
            ret.add(entry.getKey());
        }
        Collections.reverse(ret);

        return ret;
    }

    @Override
    public IRScoreFeatureKey getOutputScoreType() {
        // TODO pull up
        return outputScoreType;
    }

    @Override
    public void setOutputScoreType(IRScoreFeatureKey outputScoreType) {
        // TODO pull up
        this.outputScoreType = outputScoreType;
    }
}
