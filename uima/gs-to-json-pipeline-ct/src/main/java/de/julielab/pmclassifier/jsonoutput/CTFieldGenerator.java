package de.julielab.pmclassifier.jsonoutput;

import de.julielab.jcore.consumer.es.ArrayFieldValue;
import de.julielab.jcore.consumer.es.FieldGenerator;
import de.julielab.jcore.consumer.es.FilterRegistry;
import de.julielab.jcore.consumer.es.preanalyzed.Document;
import de.julielab.jcore.consumer.es.preanalyzed.RawToken;
import de.julielab.jcore.types.*;
import de.julielab.jcore.types.ct.*;
import de.julielab.jcore.types.ct.Header;
import de.julielab.jcore.types.pubmed.ManualDescriptor;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.uima.fit.util.JCasUtil.select;
import static org.apache.uima.fit.util.JCasUtil.selectSingle;

public class CTFieldGenerator extends FieldGenerator {

    public CTFieldGenerator(FilterRegistry filterRegistry) {
        super(filterRegistry);
    }

    @Override
    public Document addFields(JCas jCas, Document document) {
        final Header header = selectSingle(jCas, Header.class);
        try {
            final BriefTitle briefTitle = selectSingle(jCas, BriefTitle.class);
            final OfficialTitle officialTitle = selectSingle(jCas, OfficialTitle.class);
            final Summary summaryAnno = selectSingle(jCas, Summary.class);
            final Description descriptionAnno = selectSingle(jCas, Description.class);
            final Collection<OutcomeMeasure> outcomeMeasureAnnos = select(jCas, OutcomeMeasure.class);
            final Collection<OutcomeDescription> outcomeDescriptionAnnos = select(jCas, OutcomeDescription.class);
            final Collection<Condition> conditionAnnos = select(jCas, Condition.class);
            final Collection<InterventionType> interventionTypeAnnos = select(jCas, InterventionType.class);
            final Collection<InterventionName> interventionNameAnnos = select(jCas, InterventionName.class);
            final Collection<ArmGroupDescription> armGroupDescriptionAnnos = select(jCas, ArmGroupDescription.class);
            final Inclusion inclusionAnno = selectSingle(jCas, Inclusion.class);
            final Exclusion exclusionAnno = selectSingle(jCas, Exclusion.class);
            ManualDescriptor md = null;
            if (!select(jCas, ManualDescriptor.class).isEmpty())
                md = selectSingle(jCas, ManualDescriptor.class);


            final String id = header.getDocId();
            final String brief_title = briefTitle.getCoveredText();
            final String official_title = officialTitle.getCoveredText();
            final String summary = summaryAnno.getCoveredText();
            final String description = descriptionAnno.getCoveredText();
            final String studyType = header.getStudyType();
            final String interventionModel = header.getStudyDesignInfo().getInterventionModel();
            final String primary_purpose = header.getStudyDesignInfo().getPrimaryPurpose();
            final List<String> outcomeMeasures = annos2CoveredStringList(outcomeMeasureAnnos);
            final List<String> outcomeDescriptions = annos2CoveredStringList(outcomeDescriptionAnnos);
            final List<String> conditions = annos2CoveredStringList(conditionAnnos);
            final List<String> interventionTypes = annos2CoveredStringList(interventionTypeAnnos);
            final List<String> interventionNames = annos2CoveredStringList(interventionNameAnnos);
            final List<String> armGroupDescriptions = annos2CoveredStringList(armGroupDescriptionAnnos);
            final String[] sex = header.getGender() != null ? header.getGender().toStringArray() : null;
            final int minimum_age = header.getMinimumAge();
            final int maximum_age = header.getMaximumAge();
            final String inclusion = inclusionAnno.getCoveredText();
            final String exclusion = exclusionAnno.getCoveredText();
            final List<String> keywords = md != null && md.getKeywordList() != null  ? Stream.of(md.getKeywordList().toArray()).map(Keyword.class::cast).map(Keyword::getName).collect(Collectors.toList()) : null;
            final List<String> meshTags = md != null && md.getMeSHList() != null     ? Stream.of(md.getMeSHList().toArray()).map(MeshHeading.class::cast).map(MeshHeading::getDescriptorName).collect(Collectors.toList()) : null;

            document.addField("id", id);
            document.addField("brief_title", brief_title);
            document.addField("official_title", official_title);
            document.addField("summary", summary);
            document.addField("description", description);
            document.addField("studyType", studyType);
            document.addField("interventionModel", interventionModel);
            document.addField("primary_purpose", primary_purpose);
            document.addField("outcomeMeasures", outcomeMeasures);
            document.addField("outcomeDescriptions", outcomeDescriptions);
            document.addField("conditions", conditions);
            document.addField("interventionTypes", interventionTypes);
            document.addField("interventionNames", interventionNames);
            document.addField("armGroupDescriptions", armGroupDescriptions);
            document.addField("sex", sex);
            document.addField("minimum_age", minimum_age);
            document.addField("maximum_age", maximum_age);
            document.addField("inclusion", inclusion);
            document.addField("exclusion", exclusion);
            document.addField("keywords", keywords);
            document.addField("meshTags", meshTags);

            addGenes(jCas, document);
            addOrganisms(jCas, document);
        } catch (Throwable t) {
            log.error("Error while indexing document {}", header.getDocId(), t);
            throw t;
        }
        return document;
    }

    private void addGenes(JCas jCas, Document document) {
        Collection<Gene> genes = select(jCas, Gene.class);
        ArrayFieldValue genesFieldValue = new ArrayFieldValue();
        for (Gene gene : genes) {
            genesFieldValue.add(new RawToken(gene.getCoveredText()));
        }
        document.addField("genes", genesFieldValue);
    }

    private void addOrganisms(JCas jCas, Document document) {
        Collection<Organism> organisms = select(jCas, Organism.class);
        ArrayFieldValue fieldValue = new ArrayFieldValue();
        for (Organism o : organisms) {
            FSArray resourceEntryList = o.getResourceEntryList();
            if (resourceEntryList != null) {
                for (int i = 0; i < resourceEntryList.size(); ++i) {
                    ResourceEntry entry = (ResourceEntry) resourceEntryList.get(i);
                    if (entry != null)
                        fieldValue.add(new RawToken(entry.getEntryId()));
                }
            }
        }
        document.addField("organisms", fieldValue);
    }


    private List<String> annos2CoveredStringList(Collection<? extends Annotation> annos) {
        List<String> ret = new ArrayList<>();
        for (Annotation a : annos) {
            ret.add(a.getCoveredText());
        }
        return ret;
    }
}
