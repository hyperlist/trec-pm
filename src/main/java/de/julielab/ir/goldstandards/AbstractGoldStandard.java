package de.julielab.ir.goldstandards;

import at.medunigraz.imi.bst.trec.model.Challenge;
import at.medunigraz.imi.bst.trec.model.Task;
import de.julielab.ir.ltr.DocumentList;
import de.julielab.ir.model.Query;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractGoldStandard<Q extends Query> implements GoldStandard<Q> {
    protected DocumentList<Q> documents;
    protected List<Q> queries;
    protected Challenge challenge;
    protected Task task;

    public DocumentList getDocuments() {
        return documents;
    }

    public void setDocuments(DocumentList documents) {
        this.documents = documents;
    }

    protected int year;

    public AbstractGoldStandard(Challenge challenge, Task task, int year, List<Q> queries, DocumentList documents) {
        this.challenge = challenge;
        this.task = task;
        this.year = year;
        this.queries = queries;
        this.documents = documents;
    }

    public AbstractGoldStandard(Challenge challenge, Task task, int year, List<Q> queries) {
        this(challenge, task, year, queries, null);
    }

    public Stream<Q> getQueries() {
        return queries.stream();
    }

    public void setQueries(List<Q> queries) {
        this.queries = queries;
    }


    abstract public DocumentList getDocumentsForTopic(int topicId);

    public String getDatasetId() {
        return Stream.of(challenge, task, year).filter(Objects::nonNull).map(String::valueOf).collect(Collectors.joining("-"));
    }
}
