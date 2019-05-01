package de.julielab.ir.ltr.goldstandards;

import at.medunigraz.imi.bst.trec.model.Topic;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface GoldStandard {
    Stream<Topic> getTopics();

    String getName();

    default List<List<Topic>> createStratifiedTopicPartitioning(int nPartitions, Function<Topic, String> topicProperty) {
        // We will first group all the topics by the property to be stratified by, e.g. the disease topic field.
        // Then we will iterate through the (initially empty) partitions as long as there are still topics to distribute.
        // For each iteration round we put one topic from each property value into the current partition.
        // This way, the different property values are as well distributed over the partitions as possible.
        final Map<String, Deque<Topic>> topicsByProperty = getTopics().collect(Collectors.groupingBy(topicProperty, HashMap::new, Collectors.toCollection(ArrayDeque::new)));

        List<List<Topic>> partitioning = new ArrayList<>();
        for (int i = 0; i < nPartitions; i++)
            partitioning.add(new ArrayList<>());

        while (!topicsByProperty.isEmpty()) {
            for (List<Topic> partition : partitioning) {
                // When one property value has no topics left (because we will remove the topics when assigning them
                // to a partition), we remove the whole property from the map. This will eventually leave the
                // map empty, triggering the termination criterium of the while loop.
                List<String> emptyProperties = new ArrayList<>();
                for (Map.Entry<String, Deque<Topic>> topicsForProperty : topicsByProperty.entrySet()) {
                    final Topic topic = topicsForProperty.getValue().pop();
                    if (topicsForProperty.getValue().isEmpty())
                        emptyProperties.add(topicsForProperty.getKey());
                    partition.add(topic);
                }
                for (String property : emptyProperties)
                    topicsByProperty.remove(property);
            }
        }
        return partitioning;
    }

    default List<Topic> getTopicsAsList() {
        return getTopics().collect(Collectors.toList());
    }
}
