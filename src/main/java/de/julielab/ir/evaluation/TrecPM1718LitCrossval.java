package de.julielab.ir.evaluation;

import at.medunigraz.imi.bst.trec.model.Challenge;
import at.medunigraz.imi.bst.trec.model.Task;
import at.medunigraz.imi.bst.trec.model.Topic;
import at.medunigraz.imi.bst.trec.model.TopicSet;
import at.medunigraz.imi.bst.trec.stats.CSVStatsWriter;
import de.julielab.ir.goldstandards.AggregatedGoldStandard;
import de.julielab.ir.goldstandards.TrecQrelGoldStandard;
import de.julielab.ir.ltr.RankLibRanker;
import de.julielab.ir.model.Query;

import java.io.File;
import java.util.List;

public class TrecPM1718LitCrossval {
    public static void main(String args[]) {
        File topicsFile2017 = new File(CSVStatsWriter.class.getResource("/topics/topics2017.xml").getPath());
        final TopicSet topics2017 = new TopicSet(topicsFile2017, Challenge.TREC_PM, Task.PUBMED, 2017);
        File topicsFile2018 = new File(CSVStatsWriter.class.getResource("/topics/topics2018.xml").getPath());
        final TopicSet topics2018 = new TopicSet(topicsFile2018, Challenge.TREC_PM, Task.PUBMED, 2018);

        final TrecQrelGoldStandard<Topic> trecPmLit2017 = new TrecQrelGoldStandard<>(Challenge.TREC_PM, Task.PUBMED, 2017, topics2017.getTopics(), TrecPM1718LitCrossval.class.getResourceAsStream("/gold-standard/qrels-treceval-abstracts.2017.txt"));
        final TrecQrelGoldStandard<Topic> trecPmLit2018 = new TrecQrelGoldStandard<>(Challenge.TREC_PM, Task.PUBMED, 2018, topics2018.getTopics(), TrecPM1718LitCrossval.class.getResourceAsStream("/gold-standard/qrels-treceval-abstracts.2018.txt"));
        final AggregatedGoldStandard<Topic> aggregatedGoldStandard = new AggregatedGoldStandard<>(trecPmLit2017, trecPmLit2018);

        final List<List<Topic>> topicPartitioning = aggregatedGoldStandard.createStratifiedTopicPartitioning(5, Topic::getDisease);

        new RankLibRanker();
        // train and eval
    }
}
