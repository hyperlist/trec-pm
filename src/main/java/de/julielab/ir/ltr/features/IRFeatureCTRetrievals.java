package de.julielab.ir.ltr.features;

import at.medunigraz.imi.bst.trec.experiment.TrecPmRetrieval;

import java.util.EnumSet;
import java.util.LinkedHashMap;

public class IRFeatureCTRetrievals {
    private static final String DISEASE = "/subtemplates/clinical_trials/disease_phrase.json";
    private static final String GENE = "/subtemplates/clinical_trials/gene.json";
    private static final String AGE = "/subtemplates/clinical_trials/age.json";
    private static final String SEX = "/subtemplates/clinical_trials/sex.json";
    private static final String POSITIVE = "/subtemplates/clinical_trials/positive_boosters.json";
    private static final String DNA = "/subtemplates/clinical_trials/dna.json";
    private static final String CANCER = "/subtemplates/clinical_trials/cancer.json";
    private static final String OTHER = "/subtemplates/clinical_trials/other.json";
    private static final String STRUCTURED = "/subtemplates/clinical_trials/structured.json";

    public static LinkedHashMap<IRScoreFeatureKey, TrecPmRetrieval> getRetrievals(String index, EnumSet<TrecPmQueryPart> retrievalTypes) {
        LinkedHashMap<IRScoreFeatureKey, TrecPmRetrieval> ret = new LinkedHashMap<>();
        if (retrievalTypes.contains(TrecPmQueryPart.DISEASE))
            ret.put(new IRScoreFeatureKey(IRScore.BM25, TrecPmQueryPart.DISEASE), new TrecPmRetrieval(index).withSubTemplate(DISEASE));
        if (retrievalTypes.contains(TrecPmQueryPart.GENE))
            ret.put(new IRScoreFeatureKey(IRScore.BM25, TrecPmQueryPart.GENE), new TrecPmRetrieval(index).withSubTemplate(GENE));
        if (retrievalTypes.contains(TrecPmQueryPart.AGE))
            ret.put(new IRScoreFeatureKey(IRScore.BM25, TrecPmQueryPart.AGE), new TrecPmRetrieval(index).withSubTemplate(AGE));
        if (retrievalTypes.contains(TrecPmQueryPart.SEX))
            ret.put(new IRScoreFeatureKey(IRScore.BM25, TrecPmQueryPart.SEX), new TrecPmRetrieval(index).withSubTemplate(SEX));
        if (retrievalTypes.contains(TrecPmQueryPart.POS_BOOSTS))
            ret.put(new IRScoreFeatureKey(IRScore.BM25, TrecPmQueryPart.POS_BOOSTS), new TrecPmRetrieval(index).withSubTemplate(POSITIVE));
        if (retrievalTypes.contains(TrecPmQueryPart.DNA))
            ret.put(new IRScoreFeatureKey(IRScore.BM25, TrecPmQueryPart.DNA), new TrecPmRetrieval(index).withSubTemplate(DNA));
        if (retrievalTypes.contains(TrecPmQueryPart.CANCER))
            ret.put(new IRScoreFeatureKey(IRScore.BM25, TrecPmQueryPart.CANCER), new TrecPmRetrieval(index).withSubTemplate(CANCER));
        if (retrievalTypes.contains(TrecPmQueryPart.OTHER))
            ret.put(new IRScoreFeatureKey(IRScore.BM25, TrecPmQueryPart.OTHER), new TrecPmRetrieval(index).withSubTemplate(OTHER));
        if (retrievalTypes.contains(TrecPmQueryPart.CANCER))
            ret.put(new IRScoreFeatureKey(IRScore.BM25, TrecPmQueryPart.STRUCTURED), new TrecPmRetrieval(index).withSubTemplate(STRUCTURED));
        return ret;
    }

}
