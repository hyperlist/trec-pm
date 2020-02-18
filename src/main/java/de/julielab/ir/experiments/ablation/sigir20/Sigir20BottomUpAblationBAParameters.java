package de.julielab.ir.experiments.ablation.sigir20;

import de.julielab.ir.experiments.ablation.AblationLatexTableInfo;

import java.util.*;

import static de.julielab.ir.experiments.ablation.sigir20.AblationNames.*;

public class Sigir20BottomUpAblationBAParameters extends LinkedHashMap<String, Map<String, String>> implements AblationLatexTableInfo {
    private static final Set<String> INDENT = new HashSet<>(Arrays.asList(String.format("-%s", DISSYN), String.format("-%s",  HYP), String.format("-%s", DISSYN), String.format("-%s", DESC), String.format("-%s", FAM)));
    private static final Set<String> MIDRULE_AFTER = new HashSet<>(Arrays.asList(String.format("-%s", HYP), String.format("-%s", FAM)));
    public Sigir20BottomUpAblationBAParameters(Map<String, String> optimizedParameters) {
        put("Reduced good-performing model", params(  "retrievalparameters.keywords.positivepm@word:Gleason", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:Gleason"),
                "retrievalparameters.keywords.positivepm@word:base", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:base"),
                "retrievalparameters.keywords.positivepm@word:clinical", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:clinical"),
                "retrievalparameters.keywords.positivepm@word:cure", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:cure"),
                "retrievalparameters.keywords.positivepm@word:dna", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:dna"),
                "retrievalparameters.keywords.positivepm@word:efficacy", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:efficacy"),
                "retrievalparameters.keywords.positivepm@word:gefitinib", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:gefitinib"),
                "retrievalparameters.keywords.positivepm@word:gene", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:gene"),
                "retrievalparameters.keywords.positivepm@word:genotype", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:genotype"),
                "retrievalparameters.keywords.positivepm@word:heal", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:heal"),
                "retrievalparameters.keywords.positivepm@word:healing", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:healing"),
                "retrievalparameters.keywords.positivepm@word:malignancy", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:malignancy"),
                "retrievalparameters.keywords.positivepm@word:outcome", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:outcome"),
                "retrievalparameters.keywords.positivepm@word:patient", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:patient"),
                "retrievalparameters.keywords.positivepm@word:personalized", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:personalized"),
                "retrievalparameters.keywords.positivepm@word:prevent", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:prevent"),
                "retrievalparameters.keywords.positivepm@word:prognoses", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:prognoses"),
                "retrievalparameters.keywords.positivepm@word:prognosis", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:prognosis"),
                "retrievalparameters.keywords.positivepm@word:prognostic", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:prognostic"),
                "retrievalparameters.keywords.positivepm@word:prophylactic", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:prophylactic"),
                "retrievalparameters.keywords.positivepm@word:prophylaxis", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:prophylaxis"),
                "retrievalparameters.keywords.positivepm@word:recover", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:recover"),
                "retrievalparameters.keywords.positivepm@word:recovery", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:recovery"),
                "retrievalparameters.keywords.positivepm@word:recurrence", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:recurrence"),
                "retrievalparameters.keywords.positivepm@word:resistance", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:resistance"),
                "retrievalparameters.keywords.positivepm@word:study", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:study"),
                "retrievalparameters.keywords.positivepm@word:surgery", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:surgery"),
                "retrievalparameters.keywords.positivepm@word:survival", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:survival"),
                "retrievalparameters.keywords.positivepm@word:survive", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:survive"),
                "retrievalparameters.keywords.positivepm@word:target", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:target"),
                "retrievalparameters.keywords.positivepm@word:targets", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:targets"),
                "retrievalparameters.keywords.positivepm@word:therapeutic", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:therapeutic"),
                "retrievalparameters.keywords.positivepm@word:therapeutical", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:therapeutical"),
                "retrievalparameters.keywords.positivepm@word:therapy", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:therapy"),
                "retrievalparameters.keywords.positivepm@word:treatment", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:treatment"),
                "retrievalparameters.templateparameters.disease.matchtypes.disease_match_type", optimizedParameters.get("retrievalparameters.templateparameters.disease.matchtypes.disease_match_type"),
                "retrievalparameters.templateparameters.disease.matchtypes.disease_syn_match_type", optimizedParameters.get("retrievalparameters.templateparameters.disease.matchtypes.disease_syn_match_type"),
                "retrievalparameters.templateparameters.disease.matchtypes.disease_hypernyms_match_type", optimizedParameters.get("retrievalparameters.templateparameters.disease.matchtypes.disease_hypernyms_match_type"),
                "retrievalparameters.templateparameters.disease.phraseslops.disease_slop", optimizedParameters.get("retrievalparameters.templateparameters.disease.phraseslops.disease_slop"),
                "retrievalparameters.templateparameters.gene.matchtypes.gene_topic_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_topic_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.gene_syn_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_syn_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.gene_hypernyms_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_hypernyms_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.gene_desc_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_desc_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.custom_gene_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.custom_gene_match_type"),
                "retrievalparameters.templateparameters.gene.phraseslops.gene_topic_slop", optimizedParameters.get("retrievalparameters.templateparameters.gene.phraseslops.gene_topic_slop"),
                "retrievalparameters.templateparameters.gene.phraseslops.gene_syn_slop", optimizedParameters.get("retrievalparameters.templateparameters.gene.phraseslops.gene_syn_slop"),
                "retrievalparameters.templateparameters.gene.phraseslops.gene_desc_slop", optimizedParameters.get("retrievalparameters.templateparameters.gene.phraseslops.gene_desc_slop"),
                "retrievalparameters.templateparameters.clauseboosts.conditional_cancer_boost", optimizedParameters.get("retrievalparameters.templateparameters.clauseboosts.conditional_cancer_boost"),
                "retrievalparameters.templateparameters.clauseboosts.conditional_chemo_boost", optimizedParameters.get("retrievalparameters.templateparameters.clauseboosts.conditional_chemo_boost"),
                "retrievalparameters.templateparameters.clauseboosts.exists_abstract_boost", optimizedParameters.get("retrievalparameters.templateparameters.clauseboosts.exists_abstract_boost"),
                "retrievalparameters.templateparameters.clauseboosts.filtered_treatments_boost", optimizedParameters.get("retrievalparameters.templateparameters.clauseboosts.filtered_treatments_boost"),
                "retrievalparameters.templateparameters.clauseboosts.negative_kw_boost", optimizedParameters.get("retrievalparameters.templateparameters.clauseboosts.negative_kw_boost"),
                "retrievalparameters.templateparameters.clauseboosts.positive_kw_boost", optimizedParameters.get("retrievalparameters.templateparameters.clauseboosts.positive_kw_boost"),
                "retrievalparameters.templateparameters.disease.boosts.disease_custom_boost", optimizedParameters.get("retrievalparameters.templateparameters.disease.boosts.disease_custom_boost"),
                "retrievalparameters.templateparameters.disease.boosts.disease_hypernyms_boost", optimizedParameters.get("retrievalparameters.templateparameters.disease.boosts.disease_hypernyms_boost"),
                "retrievalparameters.templateparameters.disease.boosts.disease_prefterm_boost", optimizedParameters.get("retrievalparameters.templateparameters.disease.boosts.disease_prefterm_boost"),
                "retrievalparameters.templateparameters.disease.boosts.disease_query_boost", optimizedParameters.get("retrievalparameters.templateparameters.disease.boosts.disease_query_boost"),
                "retrievalparameters.templateparameters.disease.boosts.disease_syn_boost", optimizedParameters.get("retrievalparameters.templateparameters.disease.boosts.disease_syn_boost"),
                "retrievalparameters.templateparameters.disease.boosts.disease_topic_clause_boost", optimizedParameters.get("retrievalparameters.templateparameters.disease.boosts.disease_topic_clause_boost"),
                "retrievalparameters.templateparameters.gene.boosts.gene_custom_boost", optimizedParameters.get("retrievalparameters.templateparameters.gene.boosts.gene_custom_boost"),
                "retrievalparameters.templateparameters.gene.boosts.gene_desc_boost", optimizedParameters.get("retrievalparameters.templateparameters.gene.boosts.gene_desc_boost"),
                "retrievalparameters.templateparameters.gene.boosts.gene_hypernyms_boost", optimizedParameters.get("retrievalparameters.templateparameters.gene.boosts.gene_hypernyms_boost"),
                "retrievalparameters.templateparameters.gene.boosts.gene_query_boost", optimizedParameters.get("retrievalparameters.templateparameters.gene.boosts.gene_query_boost"),
                "retrievalparameters.templateparameters.gene.boosts.gene_syn_boost", optimizedParameters.get("retrievalparameters.templateparameters.gene.boosts.gene_syn_boost"),
                "retrievalparameters.templateparameters.gene.boosts.gene_topic_clause_boost", optimizedParameters.get("retrievalparameters.templateparameters.gene.boosts.gene_topic_clause_boost"),
                "retrievalparameters.diseaseexpansion.synonyms", "true",
                "retrievalparameters.geneexpansion.synonyms", "true",
                "retrievalparameters.queryfiltering", "true"
        ));
        put(String.format("+%s+%s", DISEXP, STOP), params(
                "retrievalparameters.diseaseexpansion.custom", "true",
                "retrievalparameters.diseaseexpansion.hypernyms", "true",
                "retrievalparameters.diseaseexpansion.preferredterm", "true",
                "retrievalparameters.diseaseexpansion.synonyms", "true",
                "retrievalparameters.templateparameters.disease.matchtypes.disease_match_type", optimizedParameters.get("retrievalparameters.templateparameters.disease.matchtypes.disease_match_type"),
                "retrievalparameters.templateparameters.disease.matchtypes.disease_syn_match_type", optimizedParameters.get("retrievalparameters.templateparameters.disease.matchtypes.disease_syn_match_type"),
                "retrievalparameters.templateparameters.disease.matchtypes.disease_hypernyms_match_type", optimizedParameters.get("retrievalparameters.templateparameters.disease.matchtypes.disease_hypernyms_match_type"),
                "retrievalparameters.templateparameters.disease.phraseslops.disease_slop", optimizedParameters.get("retrievalparameters.templateparameters.disease.phraseslops.disease_slop"),
                "retrievalparameters.queryfiltering", "true"

        ));
        put(String.format("-%s", DISSYN), params(
                "retrievalparameters.diseaseexpansion.custom", "true",
                "retrievalparameters.diseaseexpansion.hypernyms", "true",
                "retrievalparameters.diseaseexpansion.preferredterm", "true",
                "retrievalparameters.diseaseexpansion.synonyms", "false",
                "retrievalparameters.templateparameters.disease.matchtypes.disease_match_type", optimizedParameters.get("retrievalparameters.templateparameters.disease.matchtypes.disease_match_type"),
                "retrievalparameters.templateparameters.disease.matchtypes.disease_syn_match_type", optimizedParameters.get("retrievalparameters.templateparameters.disease.matchtypes.disease_syn_match_type"),
                "retrievalparameters.templateparameters.disease.matchtypes.disease_hypernyms_match_type", optimizedParameters.get("retrievalparameters.templateparameters.disease.matchtypes.disease_hypernyms_match_type"),
                "retrievalparameters.templateparameters.disease.phraseslops.disease_slop", optimizedParameters.get("retrievalparameters.templateparameters.disease.phraseslops.disease_slop"),
                "retrievalparameters.queryfiltering", "true"
        ));
        put(String.format("-%s",  HYP), params(
                "retrievalparameters.diseaseexpansion.custom", "true",
                "retrievalparameters.diseaseexpansion.hypernyms", "false",
                "retrievalparameters.diseaseexpansion.preferredterm", "true",
                "retrievalparameters.diseaseexpansion.synonyms", "true",
                "retrievalparameters.templateparameters.disease.matchtypes.disease_match_type", optimizedParameters.get("retrievalparameters.templateparameters.disease.matchtypes.disease_match_type"),
                "retrievalparameters.templateparameters.disease.matchtypes.disease_syn_match_type", optimizedParameters.get("retrievalparameters.templateparameters.disease.matchtypes.disease_syn_match_type"),
                "retrievalparameters.templateparameters.disease.matchtypes.disease_hypernyms_match_type", optimizedParameters.get("retrievalparameters.templateparameters.disease.matchtypes.disease_hypernyms_match_type"),
                "retrievalparameters.templateparameters.disease.phraseslops.disease_slop", optimizedParameters.get("retrievalparameters.templateparameters.disease.phraseslops.disease_slop"),
                "retrievalparameters.queryfiltering", "true"
        ));
        put(String.format("+%s+%s", GENEXP, STOP), params(
                "retrievalparameters.geneexpansion.custom", "true",
                "retrievalparameters.geneexpansion.description", "true",
                "retrievalparameters.geneexpansion.hypernyms", "true",
                "retrievalparameters.geneexpansion.synonyms", "true",
                "retrievalparameters.templateparameters.gene.matchtypes.gene_topic_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_topic_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.gene_syn_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_syn_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.gene_hypernyms_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_hypernyms_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.gene_desc_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_desc_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.custom_gene_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.custom_gene_match_type"),
                "retrievalparameters.templateparameters.gene.phraseslops.gene_topic_slop", optimizedParameters.get("retrievalparameters.templateparameters.gene.phraseslops.gene_topic_slop"),
                "retrievalparameters.templateparameters.gene.phraseslops.gene_syn_slop", optimizedParameters.get("retrievalparameters.templateparameters.gene.phraseslops.gene_syn_slop"),
                "retrievalparameters.templateparameters.gene.phraseslops.gene_desc_slop", optimizedParameters.get("retrievalparameters.templateparameters.gene.phraseslops.gene_desc_slop"),
                "retrievalparameters.queryfiltering", "true"
        ));
        put(String.format("-%s", DISSYN), params(
                "retrievalparameters.geneexpansion.custom", "true",
                "retrievalparameters.geneexpansion.description", "true",
                "retrievalparameters.geneexpansion.hypernyms", "true",
                "retrievalparameters.geneexpansion.synonyms", "false",
                "retrievalparameters.templateparameters.gene.matchtypes.gene_topic_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_topic_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.gene_syn_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_syn_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.gene_hypernyms_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_hypernyms_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.gene_desc_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_desc_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.custom_gene_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.custom_gene_match_type"),
                "retrievalparameters.templateparameters.gene.phraseslops.gene_topic_slop", optimizedParameters.get("retrievalparameters.templateparameters.gene.phraseslops.gene_topic_slop"),
                "retrievalparameters.templateparameters.gene.phraseslops.gene_syn_slop", optimizedParameters.get("retrievalparameters.templateparameters.gene.phraseslops.gene_syn_slop"),
                "retrievalparameters.templateparameters.gene.phraseslops.gene_desc_slop", optimizedParameters.get("retrievalparameters.templateparameters.gene.phraseslops.gene_desc_slop"),
                "retrievalparameters.queryfiltering", "true"
        ));
        put(String.format("-%s",  DESC), params(
                "retrievalparameters.geneexpansion.custom", "true",
                "retrievalparameters.geneexpansion.description", "false",
                "retrievalparameters.geneexpansion.hypernyms", "true",
                "retrievalparameters.geneexpansion.synonyms", "true",
                "retrievalparameters.templateparameters.gene.matchtypes.gene_topic_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_topic_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.gene_syn_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_syn_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.gene_hypernyms_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_hypernyms_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.gene_desc_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_desc_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.custom_gene_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.custom_gene_match_type"),
                "retrievalparameters.templateparameters.gene.phraseslops.gene_topic_slop", optimizedParameters.get("retrievalparameters.templateparameters.gene.phraseslops.gene_topic_slop"),
                "retrievalparameters.templateparameters.gene.phraseslops.gene_syn_slop", optimizedParameters.get("retrievalparameters.templateparameters.gene.phraseslops.gene_syn_slop"),
                "retrievalparameters.templateparameters.gene.phraseslops.gene_desc_slop", optimizedParameters.get("retrievalparameters.templateparameters.gene.phraseslops.gene_desc_slop"),
                "retrievalparameters.queryfiltering", "true"
        ));
        put(String.format("-%s", FAM), params(
                "retrievalparameters.geneexpansion.custom", "false",
                "retrievalparameters.geneexpansion.description", "true",
                "retrievalparameters.geneexpansion.hypernyms", "true",
                "retrievalparameters.geneexpansion.synonyms", "true",
                "retrievalparameters.templateparameters.gene.matchtypes.gene_topic_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_topic_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.gene_syn_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_syn_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.gene_hypernyms_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_hypernyms_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.gene_desc_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_desc_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.custom_gene_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.custom_gene_match_type"),
                "retrievalparameters.templateparameters.gene.phraseslops.gene_topic_slop", optimizedParameters.get("retrievalparameters.templateparameters.gene.phraseslops.gene_topic_slop"),
                "retrievalparameters.templateparameters.gene.phraseslops.gene_syn_slop", optimizedParameters.get("retrievalparameters.templateparameters.gene.phraseslops.gene_syn_slop"),
                "retrievalparameters.templateparameters.gene.phraseslops.gene_desc_slop", optimizedParameters.get("retrievalparameters.templateparameters.gene.phraseslops.gene_desc_slop"),
                "retrievalparameters.queryfiltering", "true"
        ));
        put(String.format("+%s", BM25), params(
                "indexparameters.bm25.b", optimizedParameters.get("indexparameters.bm25.b"),
                "indexparameters.bm25.k1", optimizedParameters.get("indexparameters.bm25.k1")));
        put(String.format("+%s", CLSWT), params(
                "retrievalparameters.templateparameters.clauseboosts.conditional_cancer_boost", optimizedParameters.get("retrievalparameters.templateparameters.clauseboosts.conditional_cancer_boost"),
                "retrievalparameters.templateparameters.clauseboosts.conditional_chemo_boost", optimizedParameters.get("retrievalparameters.templateparameters.clauseboosts.conditional_chemo_boost"),
                "retrievalparameters.templateparameters.clauseboosts.exists_abstract_boost", optimizedParameters.get("retrievalparameters.templateparameters.clauseboosts.exists_abstract_boost"),
                "retrievalparameters.templateparameters.clauseboosts.filtered_treatments_boost", optimizedParameters.get("retrievalparameters.templateparameters.clauseboosts.filtered_treatments_boost"),
                "retrievalparameters.templateparameters.clauseboosts.negative_kw_boost", optimizedParameters.get("retrievalparameters.templateparameters.clauseboosts.negative_kw_boost"),
                "retrievalparameters.templateparameters.clauseboosts.positive_kw_boost", optimizedParameters.get("retrievalparameters.templateparameters.clauseboosts.positive_kw_boost"),
                "retrievalparameters.templateparameters.disease.boosts.disease_custom_boost", optimizedParameters.get("retrievalparameters.templateparameters.disease.boosts.disease_custom_boost"),
                "retrievalparameters.templateparameters.disease.boosts.disease_hypernyms_boost", optimizedParameters.get("retrievalparameters.templateparameters.disease.boosts.disease_hypernyms_boost"),
                "retrievalparameters.templateparameters.disease.boosts.disease_prefterm_boost", optimizedParameters.get("retrievalparameters.templateparameters.disease.boosts.disease_prefterm_boost"),
                "retrievalparameters.templateparameters.disease.boosts.disease_query_boost", optimizedParameters.get("retrievalparameters.templateparameters.disease.boosts.disease_query_boost"),
                "retrievalparameters.templateparameters.disease.boosts.disease_syn_boost", optimizedParameters.get("retrievalparameters.templateparameters.disease.boosts.disease_syn_boost"),
                "retrievalparameters.templateparameters.disease.boosts.disease_topic_clause_boost", optimizedParameters.get("retrievalparameters.templateparameters.disease.boosts.disease_topic_clause_boost"),
                "retrievalparameters.templateparameters.gene.boosts.gene_custom_boost", optimizedParameters.get("retrievalparameters.templateparameters.gene.boosts.gene_custom_boost"),
                "retrievalparameters.templateparameters.gene.boosts.gene_desc_boost", optimizedParameters.get("retrievalparameters.templateparameters.gene.boosts.gene_desc_boost"),
                "retrievalparameters.templateparameters.gene.boosts.gene_hypernyms_boost", optimizedParameters.get("retrievalparameters.templateparameters.gene.boosts.gene_hypernyms_boost"),
                "retrievalparameters.templateparameters.gene.boosts.gene_query_boost", optimizedParameters.get("retrievalparameters.templateparameters.gene.boosts.gene_query_boost"),
                "retrievalparameters.templateparameters.gene.boosts.gene_syn_boost", optimizedParameters.get("retrievalparameters.templateparameters.gene.boosts.gene_syn_boost"),
                "retrievalparameters.templateparameters.gene.boosts.gene_topic_clause_boost", optimizedParameters.get("retrievalparameters.templateparameters.gene.boosts.gene_topic_clause_boost")
        ));
        put(String.format("+%s", DISEXP), params(
                "retrievalparameters.diseaseexpansion.custom", "true",
                "retrievalparameters.diseaseexpansion.hypernyms", "true",
                "retrievalparameters.diseaseexpansion.preferredterm", "true",
                "retrievalparameters.diseaseexpansion.synonyms", "true",
                "retrievalparameters.templateparameters.disease.matchtypes.disease_match_type", optimizedParameters.get("retrievalparameters.templateparameters.disease.matchtypes.disease_match_type"),
                "retrievalparameters.templateparameters.disease.matchtypes.disease_syn_match_type", optimizedParameters.get("retrievalparameters.templateparameters.disease.matchtypes.disease_syn_match_type"),
                "retrievalparameters.templateparameters.disease.matchtypes.disease_hypernyms_match_type", optimizedParameters.get("retrievalparameters.templateparameters.disease.matchtypes.disease_hypernyms_match_type"),
                "retrievalparameters.templateparameters.disease.phraseslops.disease_slop", optimizedParameters.get("retrievalparameters.templateparameters.disease.phraseslops.disease_slop")
        ));
        put(String.format("+%s", FLDWT), params(
                "retrievalparameters.templateparameters.fieldboosts.abstract_field_disease_boost", optimizedParameters.get("retrievalparameters.templateparameters.fieldboosts.abstract_field_disease_boost"),
                "retrievalparameters.templateparameters.fieldboosts.abstract_field_gene_boost", optimizedParameters.get("retrievalparameters.templateparameters.fieldboosts.abstract_field_gene_boost"),
                "retrievalparameters.templateparameters.fieldboosts.abstract_field_kw_boost", optimizedParameters.get("retrievalparameters.templateparameters.fieldboosts.abstract_field_kw_boost"),
                "retrievalparameters.templateparameters.fieldboosts.genes_field_boost", optimizedParameters.get("retrievalparameters.templateparameters.fieldboosts.genes_field_boost"),
                "retrievalparameters.templateparameters.fieldboosts.meshTags_field_disease_boost", optimizedParameters.get("retrievalparameters.templateparameters.fieldboosts.meshTags_field_disease_boost"),
                "retrievalparameters.templateparameters.fieldboosts.meshTags_field_gene_boost", optimizedParameters.get("retrievalparameters.templateparameters.fieldboosts.meshTags_field_gene_boost"),
                "retrievalparameters.templateparameters.fieldboosts.meshTags_field_kw_boost", optimizedParameters.get("retrievalparameters.templateparameters.fieldboosts.meshTags_field_kw_boost"),
                "retrievalparameters.templateparameters.fieldboosts.title_field_disease_boost", optimizedParameters.get("retrievalparameters.templateparameters.fieldboosts.title_field_disease_boost"),
                "retrievalparameters.templateparameters.fieldboosts.title_field_gene_boost", optimizedParameters.get("retrievalparameters.templateparameters.fieldboosts.title_field_gene_boost"),
                "retrievalparameters.templateparameters.fieldboosts.title_field_kw_boost", optimizedParameters.get("retrievalparameters.templateparameters.fieldboosts.title_field_kw_boost")
        ));
        put("+"+GENEXP+"", params(
                "retrievalparameters.geneexpansion.custom", "true",
                "retrievalparameters.geneexpansion.description", "true",
                "retrievalparameters.geneexpansion.hypernyms", "true",
                "retrievalparameters.geneexpansion.synonyms", "true",
                "retrievalparameters.templateparameters.gene.matchtypes.gene_topic_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_topic_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.gene_syn_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_syn_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.gene_hypernyms_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_hypernyms_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.gene_desc_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.gene_desc_match_type"),
                "retrievalparameters.templateparameters.gene.matchtypes.custom_gene_match_type", optimizedParameters.get("retrievalparameters.templateparameters.gene.matchtypes.custom_gene_match_type"),
                "retrievalparameters.templateparameters.gene.phraseslops.gene_topic_slop", optimizedParameters.get("retrievalparameters.templateparameters.gene.phraseslops.gene_topic_slop"),
                "retrievalparameters.templateparameters.gene.phraseslops.gene_syn_slop", optimizedParameters.get("retrievalparameters.templateparameters.gene.phraseslops.gene_syn_slop"),
                "retrievalparameters.templateparameters.gene.phraseslops.gene_desc_slop", optimizedParameters.get("retrievalparameters.templateparameters.gene.phraseslops.gene_desc_slop")
        ));
        put(String.format("+%s", GENFLD), params(
                "retrievalparameters.templateparameters.fieldboosts.title_field_gene_boost", optimizedParameters.get("retrievalparameters.templateparameters.fieldboosts.title_field_gene_boost")
        ));
        put(String.format("+%s", HASAB), params("retrievalparameters.templateparameters.clauseboosts.exists_abstract_boost", optimizedParameters.get("retrievalparameters.templateparameters.clauseboosts.exists_abstract_boost")));
        put(String.format("+%s", HASTR), params("retrievalparameters.templateparameters.clauseboosts.filtered_treatments_boost", optimizedParameters.get("retrievalparameters.templateparameters.clauseboosts.filtered_treatments_boost")));
        put(String.format("+%s", NEG), params(
                "retrievalparameters.keywords.negativepm@word:case", optimizedParameters.get("retrievalparameters.keywords.negativepm@word:case"),
                "retrievalparameters.keywords.negativepm@word:cell", optimizedParameters.get("retrievalparameters.keywords.negativepm@word:cell"),
                "retrievalparameters.keywords.negativepm@word:development", optimizedParameters.get("retrievalparameters.keywords.negativepm@word:development"),
                "retrievalparameters.keywords.negativepm@word:dna", optimizedParameters.get("retrievalparameters.keywords.negativepm@word:dna"),
                "retrievalparameters.keywords.negativepm@word:model", optimizedParameters.get("retrievalparameters.keywords.negativepm@word:model"),
                "retrievalparameters.keywords.negativepm@word:mouse", optimizedParameters.get("retrievalparameters.keywords.negativepm@word:mouse"),
                "retrievalparameters.keywords.negativepm@word:pathogenesis", optimizedParameters.get("retrievalparameters.keywords.negativepm@word:pathogenesis"),
                "retrievalparameters.keywords.negativepm@word:specific", optimizedParameters.get("retrievalparameters.keywords.negativepm@word:specific"),
                "retrievalparameters.keywords.negativepm@word:staining", optimizedParameters.get("retrievalparameters.keywords.negativepm@word:staining"),
                "retrievalparameters.keywords.negativepm@word:tissue", optimizedParameters.get("retrievalparameters.keywords.negativepm@word:tissue"),
                "retrievalparameters.keywords.negativepm@word:tumor", optimizedParameters.get("retrievalparameters.keywords.negativepm@word:tumor")
        ));
        put(String.format("+%s", NONMEL), params(
                "retrievalparameters.template", "/templates/biomedical_articles_generic/jlpmcommon2generic.json"));
        put(String.format("+%s", POS), params(
                "retrievalparameters.keywords.positivepm@word:Gleason", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:Gleason"),
                "retrievalparameters.keywords.positivepm@word:base", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:base"),
                "retrievalparameters.keywords.positivepm@word:clinical", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:clinical"),
                "retrievalparameters.keywords.positivepm@word:cure", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:cure"),
                "retrievalparameters.keywords.positivepm@word:dna", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:dna"),
                "retrievalparameters.keywords.positivepm@word:efficacy", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:efficacy"),
                "retrievalparameters.keywords.positivepm@word:gefitinib", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:gefitinib"),
                "retrievalparameters.keywords.positivepm@word:gene", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:gene"),
                "retrievalparameters.keywords.positivepm@word:genotype", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:genotype"),
                "retrievalparameters.keywords.positivepm@word:heal", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:heal"),
                "retrievalparameters.keywords.positivepm@word:healing", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:healing"),
                "retrievalparameters.keywords.positivepm@word:malignancy", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:malignancy"),
                "retrievalparameters.keywords.positivepm@word:outcome", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:outcome"),
                "retrievalparameters.keywords.positivepm@word:patient", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:patient"),
                "retrievalparameters.keywords.positivepm@word:personalized", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:personalized"),
                "retrievalparameters.keywords.positivepm@word:prevent", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:prevent"),
                "retrievalparameters.keywords.positivepm@word:prognoses", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:prognoses"),
                "retrievalparameters.keywords.positivepm@word:prognosis", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:prognosis"),
                "retrievalparameters.keywords.positivepm@word:prognostic", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:prognostic"),
                "retrievalparameters.keywords.positivepm@word:prophylactic", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:prophylactic"),
                "retrievalparameters.keywords.positivepm@word:prophylaxis", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:prophylaxis"),
                "retrievalparameters.keywords.positivepm@word:recover", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:recover"),
                "retrievalparameters.keywords.positivepm@word:recovery", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:recovery"),
                "retrievalparameters.keywords.positivepm@word:recurrence", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:recurrence"),
                "retrievalparameters.keywords.positivepm@word:resistance", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:resistance"),
                "retrievalparameters.keywords.positivepm@word:study", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:study"),
                "retrievalparameters.keywords.positivepm@word:surgery", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:surgery"),
                "retrievalparameters.keywords.positivepm@word:survival", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:survival"),
                "retrievalparameters.keywords.positivepm@word:survive", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:survive"),
                "retrievalparameters.keywords.positivepm@word:target", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:target"),
                "retrievalparameters.keywords.positivepm@word:targets", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:targets"),
                "retrievalparameters.keywords.positivepm@word:therapeutic", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:therapeutic"),
                "retrievalparameters.keywords.positivepm@word:therapeutical", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:therapeutical"),
                "retrievalparameters.keywords.positivepm@word:therapy", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:therapy"),
                "retrievalparameters.keywords.positivepm@word:treatment", optimizedParameters.get("retrievalparameters.keywords.positivepm@word:treatment")
        ));
        put(String.format("+%s", STOP), params(
                "retrievalparameters.queryfiltering", "true"));
        put(String.format("+%s", SLDTMR), params(
                "retrievalparameters.diseaseexpansion.custom", "true"));
    }

    private Map<String, String> params(String... entries) {
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < entries.length; i++) {
            if (i % 2 == 1)
                params.put(entries[i - 1], entries[i]);
        }
        return params;
    }

    @Override
    public boolean indent(String ablationName) {
        return INDENT.contains(ablationName);
    }

    @Override
    public boolean addMidruleAfter(String ablationName) {
        return MIDRULE_AFTER.contains(ablationName);
    }
}
