package de.julielab.ir.ltr.features.featuregroups;

import cc.mallet.types.Instance;
import cc.mallet.types.Token;
import com.wcohen.ss.TFIDF;
import de.julielab.ir.ltr.features.FeatureGroup;
import de.julielab.ir.pm.pmclassifier.InstancePreparator;
import de.julielab.java.utilities.FileUtilities;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This feature group does simply set the TF/IDF weights of the words in the document represented by the
 * instance passed to pipe() to the Token of the instance. Currently, the individual features - which would be
 * one feature for each possible word - cannot be toggled on/off individually using the toggle mechanism.
 * However, a stopword file is ready, currently a static one, excluding words found on that list.
 */
public class TfidfFeatureGroup extends FeatureGroup {

    private Set<String> stopwords;

    public TfidfFeatureGroup() {
        super("TFIDF");
        try {
            stopwords = FileUtilities.getReaderFromFile(new File("resources/stopwords.txt")).lines().collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Instance pipe(Instance inst) {
        final InstancePreparator instancePreparator = InstancePreparator.getInstance();
        TFIDF tfidf;
        synchronized (instancePreparator) {
            tfidf = instancePreparator.getTfidf();
        }
        assert tfidf != null : "The TFIDF model has not been trained.";
        Token token = (Token) inst.getData();
        com.wcohen.ss.api.Token[] tokens;
        // Synchronization is required because the InstancePreparator is static and thus, the TFIDF object
        // is the same for all threads and it is not thread safe (found out the hard way)
        synchronized (tfidf) {
            tfidf.prepare(token.getText());
            tokens = tfidf.getTokens();
        }
        com.wcohen.ss.api.Token lasttoken = null;
        for (com.wcohen.ss.api.Token tfidfToken : tokens) {
            if (!stopwords.contains(tfidfToken.getValue().toLowerCase())) {
                token.setFeatureValue(tfidfToken.getValue(), tfidf.getWeight(tfidfToken));
            }
        }
        return inst;
    }
}
