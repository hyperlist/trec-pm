package at.medunigraz.imi.bst.trec.experiment;

import at.medunigraz.imi.bst.config.TrecConfig;
import at.medunigraz.imi.bst.retrieval.Query;
import at.medunigraz.imi.bst.retrieval.Retrieval;
import at.medunigraz.imi.bst.trec.model.Metrics;
import at.medunigraz.imi.bst.trec.model.ResultList;
import at.medunigraz.imi.bst.trec.model.TopicSet;
import de.julielab.ir.goldstandards.GoldStandard;
import de.julielab.ir.model.QueryDescription;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class Experiment<Q extends QueryDescription> {

    private static final Logger LOG = LogManager.getLogger();
    private Retrieval<?, Q> retrieval;
    private GoldStandard goldStandard;
    private String statsDir = "stats/";
    private String resultsDir = "results/";
    private TopicSet topicSet;
    private int k = TrecConfig.SIZE;
    private List<ResultList<Q>> lastResultListSet;

    /**
     * Build an Experiment using the topics provided by the gold standard.
     *
     * @param goldStandard
     * @param retrieval
     */
    public Experiment(GoldStandard goldStandard, Retrieval retrieval) {
        this(goldStandard, retrieval, new TopicSet(goldStandard.getQueriesAsList()));
    }

    /**
     * Build an Experiment using the topics provided.
     * @param goldStandard
     * @param retrieval
     * @param topics
     */
    public Experiment(GoldStandard goldStandard, Retrieval retrieval, TopicSet topics) {
        this.goldStandard = goldStandard;
        this.retrieval = retrieval;
        this.topicSet = topics;
    }

    public Retrieval getRetrieval() {
        return retrieval;
    }

    public void setRetrieval(Retrieval retrieval) {
        this.retrieval = retrieval;
    }

    public String getStatsDir() {
        return statsDir;
    }

    public void setStatsDir(String statsDir) {
        this.statsDir = statsDir.endsWith(File.separator) ? statsDir : statsDir + File.separator;
    }

    public void setResultsDir(String resultsDir) {
        this.resultsDir = resultsDir.endsWith(File.separator) ? resultsDir : resultsDir + File.separator;
    }

    public String getExperimentId() {
        return retrieval.getExperimentId();
    }

    public TopicSet getTopicSet() {
        return topicSet;
    }

    public void setTopicSet(TopicSet topicSet) {
        this.topicSet = topicSet;
    }

    /**
     * <p>For the trec_eval script, specify if non-existing result entries should count as 0 in the 'all' performance values.</p>
     * <p>The sample_eval.pl script does not allow a setting here and always works as if this setting would be set to <tt>false</tt>.</p>
     *
     * @return Whether or not to calculate the evaluation scores including or excluding missing result documents.
     */
    public boolean isCalculateTrecEvalWithMissingResults() {
        // If are querying just a subset of the GS, we won't get metrics for all topics and thus need to set -c to false.
        if (topicSet.getTopics().size() < goldStandard.getQueriesAsList().size()) {
            return false;
        }
        return true;
    }

    public int getK() {
        return k;
    }

    /**
     * <p>The number of top documents to calculate scores for with trec_eval. Defaults to 1000.</p>
     *
     * @param k The number of the top documents.
     */
    public void setK(int k) {
        this.k = k;
    }


    public List<ResultList<Q>> getLastResultListSet() {
        return lastResultListSet;
    }

    public Metrics run() {
        final String experimentId = retrieval.getExperimentId();
        final String longExperimentId = experimentId + " with decorators " + retrieval.getQuery().getName();

        LOG.info("Running collection " + longExperimentId + "...");

        if (retrieval.getResultsDir() == null)
            retrieval.withResultsDir(this.resultsDir);

        lastResultListSet = retrieval.retrieve((Collection<Q>) topicSet.getTopics(), goldStandard.getQueryIdFunction());

        File output = retrieval.getOutput();
        int k = this.k;
        boolean calculateTrecEvalWithMissingResults = isCalculateTrecEvalWithMissingResults();
        String statsDir = this.statsDir;

        Metrics allMetrics = new TrecMetricsCreator(experimentId, longExperimentId, output, getQrelFile(), k, calculateTrecEvalWithMissingResults, statsDir, goldStandard.getType(), getSampleQrelFile())
                .computeMetrics();

        return allMetrics;

        // TODO Experiment API #53
//        System.out.println(allMetrics.getInfNDCG() + ";" + longExperimentId);
    }

    private File getQrelFile() {
        File qrelFile = new File("qrels", String.format("%s.qrels", getExperimentId()));
        goldStandard.writeQrelFile(qrelFile);
        return qrelFile;
    }

    private File getSampleQrelFile() {
        if (goldStandard.isSampleGoldStandard()) {
            final File sampleQrelFile = new File("qrels", String.format("sample-%s.qrels", getExperimentId()));
            goldStandard.writeSampleQrelFile(sampleQrelFile);
            return sampleQrelFile;
        }
        return null;
    }

    public Query getDecorator() {
        return retrieval.getQuery();
    }

    public void setGoldStandard(GoldStandard goldStandard) {
        this.goldStandard = goldStandard;
    }
}
