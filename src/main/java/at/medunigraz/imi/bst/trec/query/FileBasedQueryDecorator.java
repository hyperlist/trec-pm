package at.medunigraz.imi.bst.trec.query;

import at.medunigraz.imi.bst.retrieval.Query;
import at.medunigraz.imi.bst.trec.model.Topic;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileBasedQueryDecorator extends DynamicQueryDecorator {

    private static final Logger LOG = LogManager.getLogger();

    // TODO add support for several expansions inline
    private static final Pattern FORMAT = Pattern.compile("(.+)=>(.+)");

    private Map<String, Set<String>> expansionMap;

    public FileBasedQueryDecorator(File expansions, Query decoratedQuery) {
        super(decoratedQuery);
        try {
            this.expansionMap = loadExpansionsFromFile(expansions);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read from file " + expansions.getName());
        }
    }

    @Override
    public Topic expandTopic(Topic topic) {
        String disease = topic.getDisease().toLowerCase();
        String gene = topic.getGeneField().toLowerCase();

        for (Map.Entry<String, Set<String>> entry : expansionMap.entrySet()) {
            String concept = entry.getKey();
            Set<String> expansions = entry.getValue();

            if (disease.contains(concept)) {
                expansions.forEach(topic::withCustomDiseaseExpansion);
            }
            if (gene.contains(concept)) {
                expansions.forEach(topic::withCustomGeneExpansion);
            }
        }

        return topic;
    }

    private static Map<String, Set<String>> loadExpansionsFromFile(File expansions) throws IOException {
        Map<String, Set<String>> ret = new HashMap<>();

        List<String> lines = FileUtils.readLines(expansions, "UTF-8");

        for (String line : lines) {
            Matcher matcher = FORMAT.matcher(line);
            if (!matcher.find()) {
                LOG.warn("Could not parse line: {}", line);
                continue;
            }
            String concept = matcher.group(1).trim().toLowerCase();
            String expansion = matcher.group(2).trim().toLowerCase();
            Set<String> previousExpansions = ret.getOrDefault(concept, new LinkedHashSet<>());
            previousExpansions.add(expansion);
            ret.put(concept, previousExpansions);
        }

        return ret;
    }

}