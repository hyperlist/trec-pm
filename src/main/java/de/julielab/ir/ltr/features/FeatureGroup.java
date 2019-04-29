package de.julielab.ir.ltr.features;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;
import cc.mallet.types.Token;

import java.util.Set;

public abstract class FeatureGroup extends Pipe {
    private String name;
    /**
     * This is the set of features contained in this feature group.
     */
    private Set<Feature> features;

    public String getName() {
        return name;
    }


    @Override
    public abstract Instance pipe(Instance instance);

    protected void addFeatures(Token featureToken) {
    }
}
