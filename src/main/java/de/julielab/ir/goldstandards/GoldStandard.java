package de.julielab.ir.goldstandards;

import at.medunigraz.imi.bst.trec.model.GoldStandardType;
import de.julielab.ir.ltr.Document;
import de.julielab.ir.ltr.DocumentList;
import de.julielab.ir.model.QueryDescription;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface GoldStandard<Q extends QueryDescription> {
    Stream<Q> getQueries();

    String getDatasetId();

    /**
     * <p>Creates <tt>nPartitions</tt> partitions balanced for the query properties obtained by the given functions.</p>
     * <p>
     * The idea is to sort all the queries of this gold standard in the order of the properties given with
     * <tt>topicProperties</tt>. Multiple property functions mean that the queries are sorted by the first
     * property first, then by the second, then the third etc. until two queries are deemed different or
     * the last property in <tt>topicProperties</tt> has been used for comparison.
     * </p>
     * <p>
     * Then, the partitions are filled be iterating once over the sorted queries and let the partitions take turns
     * in adding the current query to them.
     * </p>
     *
     * @param nPartitions     The number of partitions to create.
     * @param topicProperties The functions returning the property to sort the queries by.
     * @return The query-property-balanced partitions.
     */
    default List<List<Q>> createPropertyBalancedQueryPartitioning(int nPartitions, List<Function<Q, String>> topicProperties) {
        Stream<Q> sortedQueries = getQueries().sorted((q1, q2) -> {
            int comparison = 0;
            int numProp = 0;
            while (comparison == 0 && numProp < topicProperties.size()) {
                Function<Q, String> queryPropertyFun = topicProperties.get(numProp);
                String prop1 = queryPropertyFun.apply(q1);
                String prop2 = queryPropertyFun.apply(q2);
                comparison = prop1.compareTo(prop2);
                ++numProp;
            }
            return comparison;
        });

        List<List<Q>> partitioning = new ArrayList<>();
        for (int i = 0; i < nPartitions; i++)
            partitioning.add(new ArrayList<>());

        int i = 0;
        Iterator<Q> qIt = sortedQueries.iterator();
        while (qIt.hasNext()) {
            Q next = qIt.next();
            if (getQrelDocumentsForQuery(next).isEmpty())
                continue;
            partitioning.get(i % nPartitions).add(next);
            ++i;
        }
        return partitioning;
    }

    default List<List<Q>> createRandomizedQueryPartitioning(int nPartitions, int seed) {
        final Random random = new Random(seed);
        List<Q> randomizedQueries = getQueries().collect(Collectors.toList());
        Collections.shuffle(randomizedQueries, random);

        List<List<Q>> partitioning = new ArrayList<>();
        for (int i = 0; i < nPartitions; i++)
            partitioning.add(new ArrayList<>());

        int partitioningIndex = 0;
        for (int i = 0; i < randomizedQueries.size(); i++) {
            Q q = randomizedQueries.get(i);
            // exclude queries that do not have any documents
            if (getQrelDocumentsForQuery(q).isEmpty())
                continue;
            partitioning.get(partitioningIndex++).add(q);
            partitioningIndex %= nPartitions;
        }

        return partitioning;
    }

    List<Q> getQueriesAsList();

    Map<Q, DocumentList<Q>> getQrelDocumentsPerQuery();

    /**
     * Writes the underlying data structure to a traditional qrel file.
     *
     * @param qrelFile
     */
    void writeQrelFile(File qrelFile);

    void writeQrelFile(File qrelFile, Collection<Q> queries);

    /**
     * Writes the underlying data structure to a sample qrel file, if possible.
     *
     * @param qrelFile
     */
    void writeSampleQrelFile(File qrelFile);

    void writeSampleQrelFile(File qrelFile, Collection<Q> queries);

    boolean isSampleGoldStandard();

    DocumentList<Q> getQrelDocuments();

    DocumentList<Q> getSampleQrelDocuments();

    Map<Integer, Q> getQueriesByNumber();

    Map<Q, DocumentList<Q>> getSampleQrelDocumentsPerQuery();

    DocumentList<Q> getQrelDocumentsForQuery(int queryId);

    DocumentList<Q> getSampleQrelDocumentsForQuery(int queryId);

    default DocumentList<Q> getQrelDocumentsForQuery(Q query) {
        return getQrelDocumentsPerQuery().get(query);
    }

    default DocumentList<Q> getSampleQrelDocumentsForQuery(Q query) {
        return getSampleQrelDocumentsPerQuery().get(query);
    }

    default DocumentList<Q> getQrelDocumentsForQueries(Collection<Q> queries) {
        return queries.stream().map(this::getQrelDocumentsForQuery).flatMap(Collection::stream).collect(Collectors.toCollection(DocumentList::new));
    }

    default DocumentList<Q> getSampleQrelDocumentsForQueries(Collection<Q> queries) {
        return queries.stream().map(this::getSampleQrelDocumentsForQuery).flatMap(Collection::stream).collect(Collectors.toCollection(DocumentList::new));
    }

    /**
     * Converts a given DocumentList to a non-stratified list.
     *
     * @param sampleQrelDocuments
     * @return
     */
    default DocumentList<Q> convertSampleToTraditional(DocumentList<Q> sampleQrelDocuments) {
        DocumentList<Q> ret = new DocumentList<>();
        for (Document<Q> doc : sampleQrelDocuments) {
            if (doc.getRelevance() != -1) {
                ret.add(doc);
            }
        }
        return ret;
    }

    Function<QueryDescription, String> getQueryIdFunction();

    GoldStandardType getType();
}
