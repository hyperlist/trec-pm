package de.julielab.ir.ltr;

import at.medunigraz.imi.bst.trec.model.Task;
import at.medunigraz.imi.bst.trec.search.ElasticClientFactory;
import ciir.umass.edu.learning.RANKER_TYPE;
import ciir.umass.edu.utilities.MyThreadPool;
import de.julielab.ir.Multithreading;
import de.julielab.ir.OriginalDocumentRetrieval;
import de.julielab.ir.TrecCacheConfiguration;
import de.julielab.ir.goldstandards.TrecPMGoldStandardFactory;
import de.julielab.ir.ltr.features.features.FastTextEmbeddingFeatures;
import de.julielab.java.utilities.ConfigurationUtilities;
import de.julielab.java.utilities.cache.CacheService;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class RankerFromCt1718 extends PrecisionMedicineReranker {


    public RankerFromCt1718() throws ConfigurationException, IOException {
            super(Task.CLINICAL_TRIALS,
                    Arrays.asList(TrecPMGoldStandardFactory.trialsOfficial2017(), TrecPMGoldStandardFactory.trialsOfficial2018()),
                    ConfigurationUtilities.loadXmlConfiguration(new File("config", "featureConfiguration.xml")),
                    Arrays.asList(new File("config", "costosys-ct19.xml").getCanonicalFile(), new File("config", "costosys-ct1718.xml").getCanonicalFile()),
                    "_data_xmi.documents_ct",
                    "id.keyword",
                    new File("rankLibModels/ct1718-val20pct-" + RANKER_TYPE.LAMBDAMART + ".mod"));

    }

    /**
     * For training.
     *
     * @param args
     */
    public static void main(String args[]) throws ConfigurationException, IOException {
        CacheService.initialize(new TrecCacheConfiguration());
        final RankerFromCt1718 ranker = new RankerFromCt1718();
        ranker.trainModel();
        CacheService.getInstance().commitAllCaches();
        ElasticClientFactory.getClient().close();
        OriginalDocumentRetrieval.getInstance().shutdown();
        FastTextEmbeddingFeatures.shutdown();
        Multithreading.getInstance().shutdown();
        MyThreadPool.getInstance().shutdown();
    }

}
