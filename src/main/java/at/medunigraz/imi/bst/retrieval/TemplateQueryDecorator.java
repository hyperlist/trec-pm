package at.medunigraz.imi.bst.retrieval;

import at.medunigraz.imi.bst.trec.model.Result;
import de.julielab.ir.model.QueryDescription;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateQueryDecorator<T extends QueryDescription> extends MapQueryDecorator<T> {
	private static final Logger LOG = LogManager.getLogger();
	protected File template;

	/**
	 * Matches double+ commas with any whitespace in between (this happens when two dynamic subtemplates are not expanded).
	 * @todo cleanup double curly braces
	 */
	private static final Pattern DOUBLE_COMMA = Pattern.compile("(\\p{javaWhitespace}*,){2,}");

	/**
	 *
	 * @param template File to the JSON template. Elements of the topic or the passed properties must be enclosed by double
	 *                 curly braces to be correctly filled with the desired values.
	 * @param decoratedQuery The query to be decorated
	 */
	public TemplateQueryDecorator(File template, Query decoratedQuery) {
		super(decoratedQuery);
		if (template == null)
            throw new IllegalArgumentException("The passed template is null");
		this.template = template;
		// XXX This cannot be called here anymore, as the final template generated may depend on the topic
		//loadTemplate(null);
	}

	@Override
	public List<Result> query(T topic) {
	    // We reload the template for each new query, as the jsonQuery has been filled with the previous topic data
		loadTemplate(topic);
		map(topic.getAttributes());
		setJSONQuery(cleanup(getJSONQuery()));
		try {
			return decoratedQuery.query(topic);
		} catch (JSONException e) {
			LOG.error("JSON exception when trying to build template from {}: {}", template, getJSONQuery());
			throw e;
		}
	}

	private void checkDanglingTemplates(String jsonQuery) {
		Pattern p = Pattern.compile("\\{\\{([^}]+)\\}\\}");
		final Matcher matcher = p.matcher(jsonQuery);
		Set<String> missingTemplates = new HashSet<>();
		while (matcher.find()) {
			missingTemplates.add(matcher.group(1));
		}
		if (!missingTemplates.isEmpty())
			throw new IllegalStateException("The following template properties have not been set: " + missingTemplates);
	}

	protected static String readTemplate(File template) {
		String ret = "";
		try {
			ret = FileUtils.readFileToString(template, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	protected void loadTemplate(T topic) {
		setJSONQuery(readTemplate(template));
	}

	private static String cleanup(String template) {
		return DOUBLE_COMMA.matcher(template).replaceAll(",");
	}
	
	@Override
	protected String getMyName() {
		return getSimpleClassName() + "(" + template.getName() + ")";
	}

}
