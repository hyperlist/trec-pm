package at.medunigraz.imi.bst.trec.model;

import de.julielab.ir.model.QueryDescription;
import org.apache.commons.collections.ArrayStack;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Topic extends QueryDescription {

    private static Pattern mutationPattern = Pattern.compile("\\(([^)]+)\\)");
    // MUST be public to be accessed via Reflection on SubTemplateQueryDecorator
    public String diseasePreferredTerm = "";
    // MUST be public to be accessed via Reflection on SubTemplateQueryDecorator
    public List<String> geneDescriptions = new ArrayList<>();
    // MUST be public to be accessed via Reflection on SubTemplateQueryDecorator
    public List<String> diseaseSynonyms = new ArrayList<>();
    // MUST be public to be accessed via Reflection on SubTemplateQueryDecorator
    public List<String> geneSynonyms = new ArrayList<>();
    // MUST be public to be accessed via Reflection on SubTemplateQueryDecorator
    public List<String> diseaseHypernyms = new ArrayList<>();
    // MUST be public to be accessed via Reflection on SubTemplateQueryDecorator
    public List<String> geneHypernyms = new ArrayList<>();
    public List<String> drugInteractions = new ArrayList<>();
    /**
     * This field is meant to collect mentions of drugs against which this gene with this mutation is resistant
     * according to the COSMIC CosmicResistanceMutations.tsv file.
     */
    public List<String> resistantDrugs = new ArrayList<>();
    /**
     * Custom, handcrafted, expansions. Normally used for the `SolidTumorQueryDecorator`.
     */
    public List<String> customDiseaseExpansions = new ArrayList<>();
    public List<String> customGeneExpansions = new ArrayList<>();
    /**
     * Custom boosters for cancer topics.
     */
    public List<String> cancerBoosters = new ArrayList<>();
    public List<String> chemotherapyBoosters = new ArrayList<>();
    private String disease = "";
    private String geneField = "";
    private TopicGene[] genes = new TopicGene[0];
    private String demographic = "";
    private String other = "";

    public Topic() {

    }

    /**
     * Builds a Topic out of a XML file in the format:
     *
     * <pre>
     * {@code
     * <topic number="1">
     *     <disease>Acute lymphoblastic leukemia</disease>
     *     <gene>ABL1, PTPN11</gene>
     *     <demographic>12-year-old male</demographic>
     *     <other>No relevant factors</other>
     * </topic>
     * }
     * </pre>
     *
     * @param xmlFile
     * @return
     */
    public static Topic fromXML(File xmlFile) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        Document doc = null;
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            doc = documentBuilder.parse(xmlFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Element element = (Element) doc.getElementsByTagName("topic").item(0);

        return fromElement(element);
    }

    public static Topic fromElement(Element element) {
        int number = Integer.parseInt(getAttribute(element, "number"));
        String disease = getElement(element, "disease");

        // Backwards compatibility
        String gene = "";
        if (hasElement(element, "variant")) {
            gene = getElement(element, "variant");
        } else if (hasElement(element, "gene")) {
            gene = getElement(element, "gene");
        }

        final String[] geneSplit = gene.split(",");
        List<TopicGene> topicGenes = new ArrayStack();
        for (String geneItem : geneSplit) {
            String geneName = geneItem;
            String mutation = null;
            final Matcher m = mutationPattern.matcher(geneItem);
            if (m.find()) {
                mutation = m.group(1).trim();
                geneName = geneItem.substring(0, m.start());
            }
            topicGenes.add(new TopicGene(geneName.trim(), mutation));
        }

        String demographic = getElement(element, "demographic");

        // 2018 topics have no "other" field
        String other = "";
        if (hasElement(element, "other")) {
            other = getElement(element, "other");
        }

        Topic topic = new Topic().withNumber(number).withDisease(disease).withGeneField(gene).withGenes(topicGenes.toArray(new TopicGene[0]))
                .withDemographic(demographic).withOther(other);

        return topic;
    }

    private static boolean hasElement(Element element, String name) {
        return element.getElementsByTagName(name).getLength() > 0 ? true : false;
    }

    private static String getElement(Element element, String name) {
        return element.getElementsByTagName(name).item(0).getTextContent();
    }

    private static String getAttribute(Element element, String name) {
        return element.getAttribute(name);
    }

    public TopicGene[] getGenes() {
        return genes;
    }

    public Topic withNumber(int number) {
        this.number = number;
        return this;
    }

    public Topic withChallenge(Challenge challenge) {
        setChallenge(challenge);
        return this;
    }

    public Topic withYear(int year) {
        setYear(year);
        return this;
    }

    public Topic withDisease(String disease) {
        this.disease = disease;
        return this;
    }

    public Topic withGenes(TopicGene... genes) {
        this.genes = genes;
        return this;
    }

    public Topic withGeneField(String gene) {
        this.geneField = gene;
        return this;
    }

    public Topic withDemographic(String demographic) {
        this.demographic = demographic;
        return this;
    }

    public Topic withOther(String other) {
        this.other = other;
        return this;
    }

    public Topic withDiseasePreferredTerm(String term) {
        this.diseasePreferredTerm = term;
        return this;
    }

    public Topic withGeneDescription(String description) {
        this.geneDescriptions.add(description);
        return this;
    }

    public Topic withDiseaseSynonym(String synonym) {
        this.diseaseSynonyms.add(synonym);
        return this;
    }

    public Topic withGeneSynonym(String synonym) {
        this.geneSynonyms.add(synonym);
        return this;
    }

    public Topic withDiseaseHypernym(String hypernym) {
        this.diseaseHypernyms.add(hypernym);
        return this;
    }

    public Topic withGeneHypernym(String hypernym) {
        this.geneHypernyms.add(hypernym);
        return this;
    }

    public Topic withDrugInteraction(String interaction) {
        this.drugInteractions.add(interaction);
        return this;
    }

    public Topic withResistantDrug(String drug) {
        this.resistantDrugs.add(drug);
        return this;
    }

    public Topic withCustomDiseaseExpansion(String diseaseExpansion) {
        this.customDiseaseExpansions.add(diseaseExpansion);
        return this;
    }

    public Topic withCustomGeneExpansion(String geneExpansion) {
        this.customGeneExpansions.add(geneExpansion);
        return this;
    }

    public Topic withCancerBooster(String geneExpansion) {
        this.cancerBoosters.add(geneExpansion);
        return this;
    }

    public Topic withChemotherapyBooster(String chemotherapyBooster) {
        this.chemotherapyBoosters.add(chemotherapyBooster);
        return this;
    }

    public int getNumber() {
        return number;
    }

    public String getDisease() {
        return disease;
    }

    public String[] getDiseaseTokens() {
        return disease.split(" ");
    }

    /**
     * Returns the exact contents of the <tt>gene</tt> field. Multiple genes are comma-separated, mutations
     * are enclosed in parenthesis following the gene name, e.g.
     * <pre>
     *     BRAF (V600E), CDKN2A Deletion
     * </pre>
     *
     * @return
     */
    public String getGeneField() {
        return geneField;
    }

    public String[] getGeneFieldTokens() {
        return geneField.split(" ");
    }

    public String getDemographic() {
        return demographic;
    }

    public int getAge() {
        try {
            return Integer.parseInt(demographic.replaceAll("[^0-9]+", ""));
        } catch (Exception e) {
            return -1;
        }
    }

    public String getSex() {
        if (demographic.toLowerCase().contains("female"))
            return "female";
        else
            return "male";
    }

    public String getOther() {
        return other;
    }

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> ret = new HashMap<>();

        // TODO use reflection to read the fields of this class and put this code into QueryDescription
        // itself. It will also be a good idea to create an annotation like "QueryAttribute" for the fields
        // that should actually go into this.
        ret.put("number", String.valueOf(number));
        ret.put("disease", disease);
        ret.put("gene", geneField);
        ret.put("variant", geneField);    // Backwards compatibility
        ret.put("demographic", demographic);
        ret.put("other", other);
        ret.put("sex", getSex());
        ret.put("age", String.valueOf(getAge()));

        if (diseasePreferredTerm != null)
            ret.put("diseasePreferredTerm", diseasePreferredTerm);

        for (int i = 0; i < geneDescriptions.size(); i++) {
            ret.put("geneDescriptions" + i, geneDescriptions.get(i));
        }

        for (int i = 0; i < diseaseSynonyms.size(); i++) {
            ret.put("diseaseSynonyms" + i, diseaseSynonyms.get(i));
        }

        for (int i = 0; i < geneSynonyms.size(); i++) {
            ret.put("geneSynonyms" + i, geneSynonyms.get(i));
        }

        for (int i = 0; i < diseaseHypernyms.size(); i++) {
            ret.put("diseaseHypernyms" + i, diseaseHypernyms.get(i));
        }

        for (int i = 0; i < geneHypernyms.size(); i++) {
            ret.put("geneHypernyms" + i, geneHypernyms.get(i));
        }

        for (int i = 0; i < drugInteractions.size(); i++) {
            ret.put("drugInteractions" + i, drugInteractions.get(i));
        }

        for (int i = 0; i < resistantDrugs.size(); i++) {
            ret.put("resistantDrugs" + i, resistantDrugs.get(i));
        }

        for (int i = 0; i < customDiseaseExpansions.size(); i++) {
            ret.put("customDiseaseExpansions" + i, customDiseaseExpansions.get(i));
        }

        for (int i = 0; i < customGeneExpansions.size(); i++) {
            ret.put("customGeneExpansions" + i, customGeneExpansions.get(i));
        }

        for (int i = 0; i < cancerBoosters.size(); i++) {
            ret.put("cancerBoosters" + i, cancerBoosters.get(i));
        }

        for (int i = 0; i < chemotherapyBoosters.size(); i++) {
            ret.put("chemotherapyBoosters" + i, chemotherapyBoosters.get(i));
        }

        return ret;
    }

    @Override
    public Topic getCleanCopy() {
        Topic copy = new Topic().withChallenge(challenge).withNumber(number).withYear(year).withDisease(disease).withGeneField(geneField).withGenes(genes)
                .withDemographic(demographic).withOther(other);
        copy.setIndex(index);
        return copy;
    }

    @Override
    public String toString() {
        return "Topic [number=" + number + ", disease=" + disease + ", gene=" + geneField
                + ", demographic=" + demographic + ", other=" + other + "]";
    }

    public String getDiseasePreferredTerm() {
        return diseasePreferredTerm;
    }

    public List<String> getGeneDescriptions() {
        return geneDescriptions;
    }

    public List<String> getDiseaseSynonyms() {
        return diseaseSynonyms;
    }

    public List<String> getGeneSynonyms() {
        return geneSynonyms;
    }

    public List<String> getDiseaseHypernyms() {
        return diseaseHypernyms;
    }

    public List<String> getGeneHypernyms() {
        return geneHypernyms;
    }

    public List<String> getDrugInteractions() {
        return drugInteractions;
    }

    public List<String> getResistantDrugs() {
        return resistantDrugs;
    }

    public List<String> getCustomDiseaseExpansions() {
        return customDiseaseExpansions;
    }

    public List<String> getCustomGeneExpansions() {
        return customGeneExpansions;
    }

    public List<String> getCancerBoosters() {
        return cancerBoosters;
    }

    public List<String> getChemotherapyDrugs() {
        return chemotherapyBoosters;
    }
}
