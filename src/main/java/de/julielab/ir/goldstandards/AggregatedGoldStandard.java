package de.julielab.ir.goldstandards;

import at.medunigraz.imi.bst.trec.model.Topic;
import de.julielab.ir.ltr.DocumentList;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A convenience class that manages multiple gold standards to make them appear as one.
 */
public class AggregatedGoldStandard implements  GoldStandard{

    private Map<String, AbstractGoldStandard> goldstandards;

    public AggregatedGoldStandard(AbstractGoldStandard... goldStandards) {
        this.goldstandards = Stream.of(goldStandards).collect(Collectors.toMap(AbstractGoldStandard::getCorpusId, Function.identity()));
    }

    public DocumentList getDocumentsForTopic(Topic topic) {
        return goldstandards.get(topic.getCrossDatasetId()).getDocumentsForTopic(topic.getNumber());
    }

    /**
     * Returns a stream over all topics of all gold standards, in no specific order.
     * @return The topics of the underlying gold standards.
     */
    public Stream<Topic> getTopics() {
        return goldstandards.values().stream().flatMap(gs -> gs.getQueries());
    }

    @Override
    public String getName() {
        return goldstandards.values().stream().map(GoldStandard::getName).collect(Collectors.joining("-"));
    }
}
