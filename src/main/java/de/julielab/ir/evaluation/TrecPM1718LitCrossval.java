package de.julielab.ir.evaluation;

import at.medunigraz.imi.bst.trec.model.Challenge;
import at.medunigraz.imi.bst.trec.model.Task;
import at.medunigraz.imi.bst.trec.model.TopicSet;
import at.medunigraz.imi.bst.trec.stats.CSVStatsWriter;
import de.julielab.ir.goldstandards.TrecQrelGoldStandard;

import java.io.File;

public class TrecPM1718LitCrossval {
    public static void main(String args[]) {
        File topicsFile2017 = new File(CSVStatsWriter.class.getResource("/topics/topics2017.xml").getPath());
        final TopicSet topics2017 = new TopicSet(topicsFile2017, Challenge.TREC_PM, Task.PUBMED, 2017);
        File topicsFile2018 = new File(CSVStatsWriter.class.getResource("/topics/topics2018.xml").getPath());
        final TopicSet topics2018 = new TopicSet(topicsFile2018, Challenge.TREC_PM, Task.PUBMED, 2018);

        // load gold standard
        // make topic partition
        // train and eval
    }
}
