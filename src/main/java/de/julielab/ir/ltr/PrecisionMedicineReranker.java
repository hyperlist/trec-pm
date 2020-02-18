package de.julielab.ir.ltr;

import at.medunigraz.imi.bst.config.TrecConfig;
import at.medunigraz.imi.bst.trec.experiment.TrecPmRetrieval;
import at.medunigraz.imi.bst.trec.experiment.registry.ClinicalTrialsRetrievalRegistry;
import at.medunigraz.imi.bst.trec.experiment.registry.LiteratureArticlesRetrievalRegistry;
import at.medunigraz.imi.bst.trec.model.Task;
import at.medunigraz.imi.bst.trec.model.Topic;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import ciir.umass.edu.learning.RANKER_TYPE;
import ciir.umass.edu.metric.METRIC;
import de.julielab.ir.goldstandards.AggregatedTrecQrelGoldStandard;
import de.julielab.ir.goldstandards.TrecQrelGoldStandard;
import de.julielab.ir.ltr.features.*;
import de.julielab.java.utilities.cache.CacheService;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static de.julielab.ir.ltr.features.TrecPmQueryPart.*;

public class PrecisionMedicineReranker implements Ranker<Topic> {
    private static final Logger log = LoggerFactory.getLogger(PrecisionMedicineReranker.class);
    private RANKER_TYPE rType = RANKER_TYPE.LAMBDAMART;
    private METRIC trainMetric = METRIC.NDCG;
    private int k = TrecConfig.SIZE;
    private int vocabCutoff = 500;
    private FeaturePreprocessing featurePreprocessing;
    private double[] scalingFactors;
    private RankLibRanker<Topic> ranker;
    private Task task;
    private DocumentList<Topic> trainDocuments;
    private File modelFile;
    private Pipe pipe;
    /**
     * The retrievals set IR scores and stored index fields to the documents. Both can be used for feature generation.
     * Care must be taken with multiple retrieval: They could alter/override fields set by a previous retrieval.
     */
    private LinkedHashMap<IRScoreFeatureKey, TrecPmRetrieval> irScoreRetrievals;

    public PrecisionMedicineReranker(Task task, List<TrecQrelGoldStandard<Topic>> trainGoldStandards, HierarchicalConfiguration<ImmutableNode> featureConfiguration, List<File> dbConnectionFiles, String xmiDbTableName, String docIdIndexField, File modelFile) {
        try {
            this.task = task;
            this.modelFile = modelFile;
            if (!FeatureControlCenter.isInitialized())
                FeatureControlCenter.initialize(featureConfiguration);
            featurePreprocessing = new FeaturePreprocessing(docIdIndexField, vocabCutoff, xmiDbTableName);
            // Set the DB connection to get the test documents from (the train documents are retrieved from trainGoldStandards).
            featurePreprocessing.setCanonicalDbConnectionFiles(toCanonicalFiles(dbConnectionFiles));
            AggregatedTrecQrelGoldStandard<Topic> gs = new AggregatedTrecQrelGoldStandard<>(trainGoldStandards);
            trainDocuments = gs.getQrelDocuments();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void trainModel() {
        train(trainDocuments);
        save(modelFile);
    }

    private List<File> toCanonicalFiles(Collection<File> files) throws IOException {
        List<File> ret = new ArrayList<>(files.size());
        for (File f : files)
            ret.add(f.getCanonicalFile());
        return ret;
    }

    @Override
    public void train(DocumentList<Topic> documentList) {
        String index;
        irScoreRetrievals = new LinkedHashMap<>();
        if (task == Task.PUBMED) {
            irScoreRetrievals.put(new IRScoreFeatureKey(IRScore.BM25, FULL), LiteratureArticlesRetrievalRegistry.jlpmletor(TrecConfig.SIZE));
            index = TrecConfig.ELASTIC_BA_INDEX;
            irScoreRetrievals.putAll(IRFeaturePMRetrievals.getRetrievals(index, EnumSet.of(DISEASE, GENE, DNA, CANCER, CHEMO, NEG_BOOSTS, POS_BOOSTS)));
        } else if (task == Task.CLINICAL_TRIALS) {
            irScoreRetrievals.put(new IRScoreFeatureKey(IRScore.BM25, FULL), ClinicalTrialsRetrievalRegistry.jlctletor(TrecConfig.SIZE));
            index = TrecConfig.ELASTIC_CT_INDEX;
            irScoreRetrievals.putAll(IRFeatureCTRetrievals.getRetrievals(index, EnumSet.of(AGE, CANCER, STRUCTURED, OTHER, DISEASE, GENE, SEX, POS_BOOSTS, DNA)));
        } else throw new IllegalArgumentException("Unsupported task " + task);
        log.info("Creating features");
        featurePreprocessing.setRetrievals(irScoreRetrievals);
        featurePreprocessing.preprocessTrain(documentList, "");

        CacheService.getInstance().commitAllCaches();

        scalingFactors = FeatureNormalizationUtils.scaleFeatures(documentList.stream().map(Document::getFeatureVector).collect(Collectors.toList()));
        pipe = trainDocuments.stream().findFirst().get().getFeaturePipes();

        log.info("Training LtR model");
        ranker = new RankLibRanker<>(rType, null, trainMetric, k, null);
        long time = System.currentTimeMillis();
        ranker.train(documentList, true, 0.8f, 1);
        time = System.currentTimeMillis() - time;
        log.info("Training of ranker {} on {} documents took {}ms ({}minutes)", rType, documentList.size(), time, time / 1000 / 60);

        if (log.isDebugEnabled()) {
            DocumentList<Topic> rankedDocuments = ranker.rank(trainDocuments);
            log.debug(trainMetric + "@" + k + " score of the newly trained ranker on the whole training data (including the dev set(s)): {}", ranker.score(rankedDocuments, trainMetric, k));
        }
    }

    @Override
    public void train(DocumentList<Topic> documents, boolean doValidation, float fraction, int randomSeed) {
        ranker.train(documents, doValidation, fraction, randomSeed);
    }

    @Override
    public void load(File modelFile) throws IOException {
        ranker = new RankLibRanker<>(rType, null, trainMetric, k, null);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(modelFile.getAbsolutePath() + ".bin"))) {
            rType = (RANKER_TYPE) ois.readObject();
            trainMetric = (METRIC) ois.readObject();
            k = (int) ois.readObject();
            String rankLibModelString = (String) ois.readObject();
            ranker.loadFromString(rankLibModelString);
            scalingFactors = (double[]) ois.readObject();
            pipe = (Pipe) ois.readObject();
            irScoreRetrievals = (LinkedHashMap<IRScoreFeatureKey, TrecPmRetrieval>) ois.readObject();

            log.info("Read ranker type {}", rType);
            log.info("Read trainMetric {}", trainMetric);
            log.info("Read top k metric computation documents as {}", k);
            log.info("Read the RankLib model string of length {}", rankLibModelString.length());
            log.info("Read {} scaling factors", scalingFactors != null ? scalingFactors.length : 0);
            log.info("Read pipe with {} elements", ((SerialPipes) pipe).size());
            log.info("Read IR score retrievals with {} elements", irScoreRetrievals.size());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void save(File modelFile) {
        if (!modelFile.getParentFile().exists())
            modelFile.getParentFile().mkdirs();
        log.info("Writing all data required for the correct application of the ranker to {}.", modelFile);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(modelFile.getAbsolutePath() + ".bin"))) {
            log.info("Writing the ranker type {}", rType);
            oos.writeObject(rType);
            log.info("Writing the trainMetric {}", trainMetric);
            oos.writeObject(trainMetric);
            log.info("Writing the top k documents for metric computation: {}", k);
            oos.writeObject(k);
            String modelAsString = ranker.getModelAsString();
            log.info("Writing the ranker itself of length {}", modelAsString.length());
            oos.writeObject(modelAsString);
            log.info("Writing the {} feature scaling factors", scalingFactors != null ? scalingFactors.length : 0);
            oos.writeObject(scalingFactors);
            log.info("Writing the feature pipe with {} elements", ((SerialPipes) pipe).size());
            oos.writeObject(pipe);
            log.info("Writing the IR retrievals with {} elements", irScoreRetrievals.size());
            oos.writeObject(irScoreRetrievals);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public DocumentList<Topic> rank(DocumentList<Topic> documentList) {
        try {
            if (ranker == null)
                load(modelFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        featurePreprocessing.setRetrievals(irScoreRetrievals);
        featurePreprocessing.preprocessTest(documentList, pipe, trainDocuments, "");
        if (scalingFactors != null)
            documentList.stream().forEach(d -> FeatureNormalizationUtils.rangeScaleFeatures(d.getFeatureVector(), scalingFactors));
        final DocumentList<Topic> rankedList = ranker.rank(documentList);
        return rankedList;
    }

    @Override
    public IRScoreFeatureKey getOutputScoreType() {
        return ranker.getOutputScoreType();
    }

    @Override
    public void setOutputScoreType(IRScoreFeatureKey outputScoreType) {
        ranker.setOutputScoreType(outputScoreType);
    }

    public RankLibRanker<Topic> getRankLibRanker() {
        return ranker;
    }
}
