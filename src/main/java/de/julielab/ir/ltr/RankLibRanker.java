package de.julielab.ir.ltr;

import cc.mallet.types.FeatureVector;
import ciir.umass.edu.learning.*;
import ciir.umass.edu.metric.METRIC;
import ciir.umass.edu.metric.MetricScorerFactory;
import de.julielab.ir.model.QueryDescription;
import de.julielab.java.utilities.FileUtilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class RankLibRanker<Q extends QueryDescription> implements Ranker<Q> {

    private final MetricScorerFactory metricScorerFactory;
    private ciir.umass.edu.learning.Ranker ranker;

    public RankLibRanker() {
        metricScorerFactory = new MetricScorerFactory();
        // TODO create the actual ranker
    }

    @Override
    public void train(DocumentList<Q> documents) {
        final LinkedHashMap<String, List<DataPoint>> dataPointsByQueryId = documents.stream().map(d -> {
            final FeatureVector fv = d.getFeatureVector();
            final double[] values = fv.getValues();
            final int[] indices = fv.getIndices();

            // In RankLib, the 0 index is unused.
            float[] ranklibValues = new float[indices.length + 1];
            int[] ranklibIndices = new int[indices.length + 1];
            if (values == null) {
                // binary vector
                Arrays.fill(ranklibValues, 1f);
            } else {
                System.arraycopy(values, 0, ranklibValues, 1, values.length);
            }
            System.arraycopy(indices, 0, ranklibIndices, 1, indices.length);
            return (DataPoint) new SparseDataPoint(ranklibValues, ranklibIndices, d.getId(), d.getRelevance());
        }).collect(Collectors.groupingBy(DataPoint::getID, LinkedHashMap::new, Collectors.toList()));
        final LinkedHashMap<String, RankList> rankLists = new LinkedHashMap<>();
        dataPointsByQueryId.forEach((key, value) -> rankLists.put(key, new RankList(value)));
        ranker = new RankerFactory().createRanker(RANKER_TYPE.COOR_ASCENT, new ArrayList(rankLists.values()), null, metricScorerFactory.createScorer(METRIC.NDCG, 20));
    }

    @Override
    public void load(File modelFile) throws IOException {
        // TODO create the ranker
        try (final BufferedReader br = FileUtilities.getReaderFromFile(modelFile)) {
            ranker.loadFromString(br.lines().collect(Collectors.joining(System.getProperty("line.separator"))));
        }
    }

    @Override
    public void save(File modelFile) {
        ranker.save(modelFile.getAbsolutePath());
    }

    @Override
    public DocumentList rank(DocumentList documents) {
        return null;
    }


}
