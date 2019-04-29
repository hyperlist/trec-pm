package de.julielab.ir.ltr;

import at.medunigraz.imi.bst.trec.model.Topic;
import de.julielab.ir.ltr.features.IRScore;

import java.util.HashMap;
import java.util.Map;

/**
 * A (topic/document) pair. Such objects are the input to ranking algorithms that will rank multiple documents
 * with respect to their topic (query).
 */
public class Document {
    private String id;
    private Topic topic;
    private String type;
    /**
     * The gold standard given relevance level, e.g. 0, 1, 2 for not relevant, partially relevant and definitively relevant.
     */
    private int relevance;
    /**
     * Scores that this document has for the given query.
     */
    private Map<IRScore, Double> irScores;

    public int getRelevance() {

        return relevance;
    }

    public void setRelevance(int relevance) {
        this.relevance = relevance;
    }

    public Map<IRScore, Double> getIrScores() {
        return irScores;
    }

    public void setScore(IRScore type, double score) {
        if (irScores == null)
            irScores = new HashMap<>();
        irScores.put(type, score);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
