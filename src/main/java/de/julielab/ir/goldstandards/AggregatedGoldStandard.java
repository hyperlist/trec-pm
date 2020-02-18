package de.julielab.ir.goldstandards;

import at.medunigraz.imi.bst.trec.model.Topic;
import de.julielab.ir.ltr.Document;
import de.julielab.ir.ltr.DocumentList;
import de.julielab.ir.model.QueryDescription;
import de.julielab.java.utilities.FileUtilities;
import org.slf4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A convenience class that manages multiple gold standards to make them appear as one.
 */
public abstract class AggregatedGoldStandard<Q extends QueryDescription> implements GoldStandard<Q> {

    private Map<String, Q> queriesByCrossDatasetId;
    private Logger log;
    protected Map<String, AtomicGoldStandard<Q>> goldStandards;
    private List<Q> queryList;
    private Map<Q, DocumentList<Q>> documentsByQuery;

    public AggregatedGoldStandard(Logger log, AtomicGoldStandard<Q>... goldStandards) {
        this.log = log;
        this.goldStandards = Stream.of(goldStandards).collect(Collectors.toMap(AtomicGoldStandard::getDatasetId, Function.identity()));
    }

    public AggregatedGoldStandard(Logger log, Collection<AtomicGoldStandard<Q>> goldStandards) {
        this.log = log;
        this.goldStandards = goldStandards.stream().collect(Collectors.toMap(AtomicGoldStandard::getDatasetId, Function.identity()));
    }

    @Override
    public DocumentList<Q> getSampleQrelDocuments() {
        return goldStandards.values().stream().map(GoldStandard::getSampleQrelDocuments).flatMap(Collection::stream).collect(Collectors.toCollection(DocumentList::new));
    }

    @Override
    public DocumentList<Q> getQrelDocuments() {
        return goldStandards.values().stream().map(GoldStandard::getQrelDocuments).flatMap(Collection::stream).collect(Collectors.toCollection(DocumentList::new));
    }

    public DocumentList getDocumentsForTopic(Topic topic) {
        return goldStandards.get(topic.getCrossDatasetId()).getQrelDocumentsForQuery(topic.getNumber());
    }

    /**
     * Returns a stream over all topics of all gold standards, in no specific order.
     *
     * @return The topics of the underlying gold standards.
     */
    public Stream<Q> getTopics() {
        return goldStandards.values().stream().flatMap(GoldStandard::getQueries);
    }

    @Override
    public Stream<Q> getQueries() {
        return goldStandards.values().stream().flatMap(GoldStandard::getQueries);
    }

    @Override
    public String getDatasetId() {
        return goldStandards.values().stream().map(GoldStandard::getDatasetId).collect(Collectors.joining("-"));
    }

    @Override
    public List<Q> getQueriesAsList() {
        if (queryList == null)
            queryList = goldStandards.values().stream().flatMap(GoldStandard::getQueries).collect(Collectors.toList());
        return queryList;
    }

    @Override
    public DocumentList<Q> getQrelDocumentsForQuery(QueryDescription query) {
        DocumentList documentList = getQrelDocumentsPerQuery().get(query);
        if (documentList == null)
            throw new IllegalArgumentException("The dataset \""+getDatasetId()+"\" does not contain the query " + query);
        return documentList;
    }

    @Override
    public Map<Q, DocumentList<Q>> getQrelDocumentsPerQuery() {
        // To create the documentsByQuery field
        getSampleQrelDocumentsPerQuery();
        if (isSampleGoldStandard())
            return documentsByQuery.keySet().stream().collect(Collectors.toMap(Function.identity(), t -> convertSampleToTraditional(documentsByQuery.get(t))));
        return documentsByQuery;
    }

    @Override
    public Map<Q, DocumentList<Q>> getSampleQrelDocumentsPerQuery() {
        if (documentsByQuery == null) {
            documentsByQuery = goldStandards.values().stream().map(GoldStandard::getSampleQrelDocumentsPerQuery).collect(HashMap::new, (m1, m2) -> m1.putAll(m2), (m1, m2) -> m1.putAll(m2));
        }
        return documentsByQuery;
    }

    @Override
    public DocumentList<Q> getSampleQrelDocumentsForQuery(int queryId) {
        throw new IllegalArgumentException("The retrieval of queries by their number makes no sense for aggregated gold standards.");
    }

    @Override
    public Map<Integer, Q> getQueriesByNumber() {
        throw new IllegalArgumentException("The retrieval of queries by their number makes no sense for aggregated gold standards.");
    }

    public Map<String, Q> getQueriesByCrossDatasetId() {
        if (queriesByCrossDatasetId == null)
            queriesByCrossDatasetId = getQueries().collect(Collectors.toMap(Q::getCrossDatasetId, Function.identity()));
        return queriesByCrossDatasetId;
    }

    @Override
    public DocumentList<Q> getQrelDocumentsForQuery(int queryId) {
        return getQrelDocumentsPerQuery().get(getQueriesByNumber().get(queryId));
    }

    protected void writeAggregatedQrelFile(File qrelFile, GoldStandard<Q>[] goldStandards, Function<GoldStandard<Q>, DocumentList<Q>> documentListFunction, Function<Document, String> recordFunction) {
        if (!qrelFile.exists()) {
            if (!qrelFile.getParentFile().exists())
                qrelFile.getParentFile().mkdirs();
            try (final BufferedWriter bw = FileUtilities.getWriterToFile(qrelFile)) {
                for (GoldStandard<Q> gs : goldStandards) {
                    final DocumentList<Q> gsDocs = documentListFunction.apply(gs);
                    for (Document gsDoc : gsDocs) {
                        String line = recordFunction.apply(gsDoc);
                        bw.write(line);
                        bw.newLine();
                    }
                }
            } catch (IOException e) {
                log.error("Exception while writing aggregated qrel file to {}", qrelFile, e);
            }
        }
    }
}
