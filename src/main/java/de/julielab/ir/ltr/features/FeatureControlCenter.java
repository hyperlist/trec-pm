package de.julielab.ir.ltr.features;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.impl.CasManager_impl;
import org.apache.uima.resource.impl.ResourceManager_impl;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.resource.metadata.impl.ProcessingResourceMetaData_impl;
import org.apache.uima.util.CasPool;

import static de.julielab.ir.ltr.features.FCConstants.*;
import static de.julielab.java.utilities.ConfigurationUtilities.*;

public class FeatureControlCenter {
    private static final Logger log = LogManager.getLogger();
    private static FeatureControlCenter singleton;
    private HierarchicalConfiguration<ImmutableNode> configuration;

    private FeatureControlCenter(HierarchicalConfiguration<ImmutableNode> configuration) {
        this.configuration = configuration;
    }

    public static FeatureControlCenter getInstance() {
        return singleton;
    }

    public static void initialize(HierarchicalConfiguration<ImmutableNode> configuration) {
        singleton = new FeatureControlCenter(configuration);
    }

    public boolean filterActive(FeatureGroup featureGroup) {
        final boolean isActive = configuration.getBoolean(slash(FEATUREGROUPS, FEATUREGROUP + attrEqPred(NAME_ATTR, featureGroup.getName()), ACTIVE_ATTR), true);
        log.trace("Checking if feature group '{}' is active {}: ", featureGroup.getName(), isActive);
        return isActive;
    }

    public boolean filterActive(FeatureGroup featureGroup, Feature feature) {
        final boolean isActive = configuration.getBoolean(slash(FEATUREGROUPS, FEATUREGROUP + attrEqMultiPred("and", NAME_ATTR, featureGroup.getName(), ACTIVE_ATTR, "true")), true);
        log.trace("Checking if feature group '{}' is active {}: ", featureGroup.getName(), isActive);
        return isActive;
    }
}
