package de.julielab.ir.ltr;

import de.julielab.ir.ltr.features.IRScore;
import de.julielab.ir.ltr.features.IRScoreFeatureKey;
import de.julielab.ir.ltr.features.TrecPmQueryPart;
import de.julielab.ir.model.QueryDescription;

import java.io.File;
import java.util.Collections;
import java.util.Random;

/**
 * <p>This ranker is mostly for testing. It just shuffles the input.</p>
 *
 * @param <Q>
 */
public class RandomRanker<Q extends QueryDescription> implements Ranker<Q> {
    private IRScoreFeatureKey outputScoreType = new IRScoreFeatureKey(IRScore.RANDOM, TrecPmQueryPart.FULL);

    @Override
    public void train(DocumentList<Q> documents) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void train(DocumentList<Q> documents, boolean doValidation, float fraction, int randomSeed) {

    }

    @Override
    public void load(File modelFile) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(File modelFile) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DocumentList<Q> rank(DocumentList<Q> documents) {
        DocumentList<Q> ret = new DocumentList<>(documents);
        Collections.shuffle(ret, new Random(System.currentTimeMillis()));
        for (int i = 0; i < ret.size(); i++) {
            ret.get(i).setScore(outputScoreType, (double) 50 / (i + 1));
        }
        return ret;
    }

    @Override
    public IRScoreFeatureKey getOutputScoreType() {
        return outputScoreType;
    }

    @Override
    public void setOutputScoreType(IRScoreFeatureKey outputScoreType) {

        this.outputScoreType = outputScoreType;
    }
}
