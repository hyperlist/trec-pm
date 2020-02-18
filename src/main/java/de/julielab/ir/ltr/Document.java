package de.julielab.ir.ltr;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.FeatureVector;
import de.julielab.ir.OriginalDocumentRetrieval;
import de.julielab.ir.ltr.features.FeatureUtils;
import de.julielab.ir.ltr.features.IRScore;
import de.julielab.ir.ltr.features.IRScoreFeatureKey;
import de.julielab.ir.ltr.features.TrecPmQueryPart;
import de.julielab.ir.model.QueryDescription;
import de.julielab.jcore.types.AbstractText;
import de.julielab.jcore.types.Title;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A (topic/document) pair. Such objects are the input to ranking algorithms that will rank multiple documents
 * with respect to their topic (query).
 */
public class Document<Q extends QueryDescription> {
    private final static Logger log = LoggerFactory.getLogger(Document.class);
    private String id;
    private Q queryDescription;
    private String type;
    /**
     * The gold standard given relevance level, e.g. -1, 0, 1, 2 for not judged, not relevant, partially relevant and definitively relevant.
     */
    private int relevance = -1;
    /**
     * For infNDCG or infAP evaluation after
     * Yilmaz, E., Kanoulas, E., & Aslam, J. a. (2008). A simple and efficient sampling method for estimating AP and NDCG.
     * Proceedings of the 31st Annual International ACM SIGIR Conference on Research and Development in Information Retrieval, 603–610.
     * https://doi.org/papers2://publication/uuid/FABB7DFB-E56B-45F9-855F-77F256E1CB1D
     * and also
     * https://groups.google.com/forum/#!msg/trec-cds/o6Y7_tSaGdE/Rmwo-4WcCwAJ
     * and
     * https://groups.google.com/forum/#!msg/trec-cds/nk5vx1FDacQ/S2BaqQh6tskJ
     * <p>
     * documents are partitioned into strata, e.g. 2, from which independent samples are taken. This field reveals
     * the stratum the document-query pair is from.
     */
    private int stratum;
    /**
     * Scores that this document has for the given query.
     */
    private Map<IRScoreFeatureKey, Double> irScores;
    /**
     * The serialized CAS data of this document as retrieved from the database. To be set through
     * {@link OriginalDocumentRetrieval#setXmiCasDataToDocuments(DocumentList, String)}.
     */
    private byte[] fullDocumentData;
    /**
     * The feature vector that is created for this document for the LtR algorithms.
     */
    private FeatureVector fv;
    /**
     * The deserialized version of fullDocumentData. To be created by {@link OriginalDocumentRetrieval#parseXmiDataIntoJCas(byte[])}.
     * This field will mostly be null because it is very expansive to keep a lot of CAS instances around.
     * Thus, this field will be populated on request.
     */
    private CAS cas;
    private String normalizedDocumentText;
    private File documentDbConfiguration;

    /**
     * <p>The CoStoSys configuration file configured to the database containing this document.</p>
     * @return
     */
    public File getDocumentDbConfiguration() {
        return documentDbConfiguration;
    }

    /**
     * <p>Sets the given file path to the CoStoSys configuration file for retrieval of this document.</p>
     * <p>Uses {@link File#getCanonicalFile()} to normalize the path, thus the method throws an <code>IOException</code>.</p>
     * @param documentDbConfiguration The CoStoSys configuration file that allows to retrieve this document's contents from a database.
     * @throws IOException If the canonical File cannot be retrieved from the configuration file path.
     */
    public void setDocumentDbConfiguration(File documentDbConfiguration) throws IOException {
        this.documentDbConfiguration = documentDbConfiguration.getCanonicalFile();
        if (!documentDbConfiguration.exists())
            throw new FileNotFoundException("The CoStoSys document database configuration file at " + documentDbConfiguration.getCanonicalPath() + " could not be found. It is required to exist in order to be used to fetch the document contents.");
    }

    /**
     * Quick-and-dirty approach to have treatments from `Result` here.
     *
     * @todo Convert into a FeatureVector or unify this class with `Result` (#31)
     */
    private List<String> treatments;
    private Map<String, Object> sourceFields = Collections.emptyMap();
    private Pipe featurePipes;

    public int getStratum() {
        return stratum;
    }

    public void setStratum(int stratum) {
        this.stratum = stratum;
    }

    public FeatureVector getFeatureVector() {
        return fv;
    }

    public void setFeatureVector(FeatureVector fv) {
        this.fv = fv;
    }

    /**
     * Returns the UIMA CAS of the document. This can only be done if {@link #setFullDocumentData(byte[])} has
     * been used before to set the serialized CAS data. If this method hasn't been called before or the CAS
     * had been released, the XMI CAS data will be parsed into a CAS from the CasPool managed by
     * {@link OriginalDocumentRetrieval#casPool}
     *
     * @return The UIMA CAS of this document.
     */
    public CAS getCas() {
        if (fullDocumentData == null || fullDocumentData.length == 0) {
            log.warn("Cannot populate the CAS for document {} because its XMI data is not set. Features related to the document itself cannot be created.", id);
            //throw new IllegalArgumentException();
        }
        if (cas == null) {
            cas = OriginalDocumentRetrieval.getInstance().parseXmiDataIntoJCas(fullDocumentData);
        }
        return cas;
    }

    public int getRelevance() {

        return relevance;
    }

    public void setRelevance(int relevance) {
        this.relevance = relevance;
    }

    public Map<IRScoreFeatureKey, Double> getIrScores() {
        return irScores;
    }

    public void setScore(IRScoreFeatureKey type, double score) {
        if (irScores == null)
            irScores = new HashMap<>();
        irScores.put(type, score);
    }

    public Double getIrScore(IRScoreFeatureKey scoreType) {
        return irScores != null ? irScores.get(scoreType) : null;
    }

    public Double getIrScore(IRScore scoreType, TrecPmQueryPart queryPart) {
        return irScores != null ? irScores.get(new IRScoreFeatureKey(scoreType, queryPart)) : null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Q getQueryDescription() {
        return queryDescription;
    }

    public void setQueryDescription(Q queryDescription) {
        this.queryDescription = queryDescription;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getFullDocumentData() {
        return fullDocumentData;
    }

    public void setFullDocumentData(byte[] fullDocumentData) {
        this.fullDocumentData = fullDocumentData;
    }

    /**
     * Releases the JCas of this document and sets its reference to <tt>null</tt>. If the JCas is required again
     * after this call, its XMI data will be parsed into some CAS retrieved from {@link OriginalDocumentRetrieval#casPool}.
     */
    public void releaseJCas() {
        if (cas != null)
            OriginalDocumentRetrieval.getInstance().releaseCas(cas);
        cas = null;
    }

    private String getNormalizedDocumentText() {
        if (normalizedDocumentText == null) {
            final CAS cas = getCas();
            normalizedDocumentText = FeatureUtils.getInstance().normalizeString(cas.getDocumentText());
        }
        return normalizedDocumentText;
    }

    public boolean isStratified() {
        // XXX Here we just use the default Java int value. However, maybe one gold standard could have a valid stratum named 0.
        return getStratum() != 0;
    }

    public List<String> getTreatments() {
        return treatments;
    }

    public void setTreatments(List<String> treatments) {
        this.treatments = treatments;
    }

    public String getTitle() {
        final Title title = getTitleAnnotation();
        return title != null ? title.getCoveredText() : "";
    }

    private Title getTitleAnnotation() {
        try {
            final JCas jCas = getCas().getJCas();
            for (Title t : jCas.<Title>getAnnotationIndex(Title.type)) {
                if (t.getTitleType() == null || t.getTitleType().equals("document"))
                    return t;
            }
        } catch (CASException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getTitleLength() {
        final Title t = getTitleAnnotation();
        return t != null ? t.getEnd() - t.getBegin() : 0;
    }

    public int getAbstractLength() {
        final AbstractText a = getAbstractAnnotation();
        return a != null ? a.getEnd() - a.getBegin() : 0;
    }

    public String getAbstract() {
        final AbstractText a = getAbstractAnnotation();
        return a != null ? a.getCoveredText() : null;

    }

    private AbstractText getAbstractAnnotation() {
        try {
            final JCas jCas = getCas().getJCas();
            for (AbstractText a : jCas.<AbstractText>getAnnotationIndex(AbstractText.type)) {
                return a;
            }
        } catch (CASException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Object> getSourceFields() {
        return sourceFields;
    }

    public void setSourceFields(Map<String, Object> sourceFields) {
        this.sourceFields = sourceFields;
    }

    public Pipe getFeaturePipes() {
        return featurePipes;
    }

    public void setFeaturePipes(Pipe featurePipes) {
        this.featurePipes = featurePipes;
    }
}
