package de.julielab.ir.ltr.goldstandards;

import at.medunigraz.imi.bst.trec.model.Topic;
import de.julielab.ir.ltr.DocumentList;

import java.util.List;
import java.util.stream.Stream;

abstract public class AbstractGoldStandard implements  GoldStandard{
    private String corpusName;
    private List<Topic> topics;

    public Stream<Topic> getTopics() {
        return topics.stream();
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public String getCorpusId() {

        return corpusName;
    }

    public void setCorpusName(String corpusName) {
        this.corpusName = corpusName;
    }

    abstract public DocumentList getDocumentsForTopic(int topicId);
}
