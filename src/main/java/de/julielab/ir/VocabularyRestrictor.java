package de.julielab.ir;

import com.wcohen.ss.BasicStringWrapper;
import com.wcohen.ss.BasicStringWrapperIterator;
import com.wcohen.ss.TFIDF;
import com.wcohen.ss.api.Token;
import com.wcohen.ss.tokens.SimpleTokenizer;
import de.julielab.java.utilities.IOStreamUtilities;
import de.julielab.java.utilities.cache.CacheAccess;
import de.julielab.java.utilities.cache.CacheService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VocabularyRestrictor {
    public static final String FIELD_VOCABLARIES_DB = "fieldVocablaries.db";
    private static VocabularyRestrictor instance;
    private final CacheAccess<String, Set<String>> cacheAccess;
    // Only used in case that the file cache is disabled
    private final Map<String, Set<String>> memCache;

    private Set<String> stopwords;

    private VocabularyRestrictor() {
        cacheAccess = CacheService.getInstance().getCacheAccess(FIELD_VOCABLARIES_DB, "FieldVobabularies", "string", "java");
        memCache = cacheAccess.isReadOnly() ? new HashMap<>() : Collections.emptyMap();
        try {
            stopwords = new HashSet<>(IOStreamUtilities.getLinesFromInputStream(getClass().getResourceAsStream("/data/stopwords.txt")));
        } catch (IOException e) {
            throw new IllegalStateException("Could not load the stopwords file from resource location /data/stopwords.txt");
        }
    }

    public static VocabularyRestrictor getInstance() {
        if (instance == null) {
            instance = new VocabularyRestrictor();
        }
        return instance;
    }

    public Set<String> calculateVocabulary(String vocabId, Stream<String> fieldContents, Restriction restriction, int cutoff) {
        Set<String> vocabulary = cacheAccess.get(vocabId);
        if (vocabulary == null) {
            switch (restriction) {
                case TFIDF:
                    vocabulary = calculateTfIdfVocabulary(fieldContents, cutoff);
                    break;
                case FREQUENCY:
                    vocabulary = calculateFrequencyVocabulary(fieldContents, cutoff);
                    break;
            }
            if (!cacheAccess.isReadOnly())
                cacheAccess.put(vocabId, vocabulary);
            else
                memCache.put(vocabId, vocabulary);
        }
        return vocabulary;
    }

    public Set<String> getVocabulary(String vocabId) {
        Set<String> vocabulary = cacheAccess.get(vocabId);
        if (vocabulary == null)
            vocabulary = memCache.get(vocabId);
        return vocabulary;
    }

    private Set<String> calculateFrequencyVocabulary(Stream<String> fieldContents, int cutoff) {
        // This is the tokenizer used with the default constructor of an TFIDF instance
        final SimpleTokenizer tokenizer = SimpleTokenizer.DEFAULT_TOKENIZER;
        final Map<String, List<String>> sets = fieldContents.map(tokenizer::tokenize).flatMap(Stream::of).map(Token::getValue).collect(Collectors.groupingBy(Function.identity()));
        final ArrayList<Map.Entry<String, List<String>>> entryList = new ArrayList<>(sets.entrySet());
        entryList.sort((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size()));
        Set<String> vocabulary = new HashSet<>(cutoff);
        for (int i = 0; i < entryList.size() && vocabulary.size() < cutoff; i++) {

            final String token = entryList.get(i).getKey();
            if (acceptVocabularyToken(token))
                vocabulary.add(token);
        }
        return vocabulary;
    }

    private Set<String> calculateTfIdfVocabulary(Stream<String> fieldContents, int cutoff) {
        final List<String> fieldContentList = fieldContents.collect(Collectors.toList());
        final TFIDF tfidf = new TFIDF();
        final Stream<BasicStringWrapper> stringWrapperStream = fieldContentList.stream().map(BasicStringWrapper::new);
        tfidf.train(new BasicStringWrapperIterator(stringWrapperStream.iterator()));
        List<Pair<Token, Double>> weightedTokens = new ArrayList<>();

        for (String fieldValue : fieldContentList) {
            tfidf.prepare(fieldValue);
            for (Token t : tfidf.getTokens())
                weightedTokens.add(new ImmutablePair<>(t, tfidf.getWeight(t)));
        }
        weightedTokens.sort((t1, t2) -> Double.compare(t2.getRight(), t1.getRight()));
        Set<String> vocabulary = new HashSet<>(cutoff);
        for (int i = 0; i < weightedTokens.size() && vocabulary.size() < cutoff; i++) {
            final Pair<Token, Double> weightedToken = weightedTokens.get(i);
            final Token token = weightedToken.getLeft();
            if (!vocabulary.contains(token.getValue()) && acceptVocabularyToken(token.getValue()))
                vocabulary.add(token.getValue());
        }
        return vocabulary;
    }

    private boolean acceptVocabularyToken(String token) {
        if (token.isBlank())
            return false;
        if (token.length() == 1)
            return false;
        if (stopwords.contains(token))
            return false;
        if (token.matches("[0-9]+"))
            return false;
        return true;
    }

    public enum Restriction {TFIDF, FREQUENCY}

}
