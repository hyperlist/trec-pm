package at.medunigraz.imi.bst.trec.query;

import at.medunigraz.imi.bst.lexigram.Lexigram;
import at.medunigraz.imi.bst.retrieval.Query;
import at.medunigraz.imi.bst.retrieval.QueryDecorator;
import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DiseaseExpanderQueryDecorator extends QueryDecorator<Topic> {

	private static final Logger LOG = LoggerFactory.getLogger(DiseaseExpanderQueryDecorator.class);

	public DiseaseExpanderQueryDecorator(Query decoratedQuery) {
		super(decoratedQuery);
	}

	@Override
	public List<Result> query(Topic topic) {
		expandDisease(topic);
		return decoratedQuery.query(topic);
	}

	private void expandDisease(Topic topic) {
		String disease = topic.getDisease();

		List<String> synonyms = Lexigram.addSynonymsFromBestConceptMatch(disease);
		
		String expandedDisease = String.join(" ", synonyms);
		topic.withDisease(expandedDisease);
		
		LOG.debug(disease + " changed to " + expandedDisease);
	}

}
