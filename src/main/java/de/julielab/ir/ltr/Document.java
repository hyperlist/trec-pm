package de.julielab.ir.ltr;

import at.medunigraz.imi.bst.trec.model.Topic;

/**
 * A (topic/document) pair. Such objects are the input to ranking algorithms that will rank multiple documents
 * with respect to their topic (query).
 */
public class Document {
    private String id;
    private Topic topic;
    private String type;

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
