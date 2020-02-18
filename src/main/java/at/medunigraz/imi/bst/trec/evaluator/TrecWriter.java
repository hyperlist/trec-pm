package at.medunigraz.imi.bst.trec.evaluator;

import at.medunigraz.imi.bst.config.TrecConfig;
import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.ResultList;
import com.opencsv.CSVWriter;
import de.julielab.ir.ltr.Document;
import de.julielab.ir.ltr.DocumentList;
import de.julielab.ir.ltr.features.IRScoreFeatureKey;
import de.julielab.ir.model.QueryDescription;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;

public class TrecWriter implements Closeable {
	private static final String VALID_RUN_NAME_REGEX = "[a-zA-Z0-9]{1,12}";
	private static final int NUM_FIELDS = 6;
	private CSVWriter writer;
	private String runName;
    private static final Function<QueryDescription, String> defaultQueryIdFunction = q -> String.valueOf(q.getNumber());

	public TrecWriter(File output, String runName) {
		if (!checkRunName(runName)) {
			throw new RuntimeException("Invalid run name!");
		}

		if (!output.getParentFile().exists())
			output.getParentFile().mkdirs();

		this.runName = runName;

		try {
			writer = new CSVWriter(new FileWriter(output), '\t', CSVWriter.NO_QUOTE_CHARACTER, '\\', System.getProperty("line.separator"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean checkRunName(String runName) {
		//final Pattern valid = Pattern.compile(VALID_RUN_NAME_REGEX);
		//return valid.matcher(runName).matches();
		return true;
	}

    public <T extends QueryDescription> void write(List<ResultList<T>> resultListSet) {
        write(resultListSet, null);
    }

	public <T extends QueryDescription> void write(List<ResultList<T>> resultListSet, Function<QueryDescription, String> queryIdFunction) {
		for (ResultList resultList : resultListSet) {
			write(resultList, queryIdFunction);
		}
	}

	public <T extends QueryDescription> void writeDocuments(List<DocumentList<T>> documents, IRScoreFeatureKey scoreToWrite, Function<QueryDescription, String> queryIdFunction) {
        List<ResultList<T>> resultLists = new ArrayList<>();
        for (DocumentList<T> documentList : documents) {
            final ResultList<T> resultList = new ResultList<>(documentList.get(0).getQueryDescription());
            for (Document<T> doc : documentList) {
                final Result result = new Result(doc.getId(), doc.getIrScore(scoreToWrite));
                resultList.add(result);
            }
            resultLists.add(resultList);
        }
        write(resultLists, queryIdFunction);
    }


	public void write(ResultList<?> resultList) {
		write(resultList, null);
	}

	public void write(ResultList<?> resultList, Function<QueryDescription, String> queryIdFunction) {
		if (queryIdFunction == null)
            queryIdFunction = defaultQueryIdFunction;
		
		int rank = 1;
		for (Result result : resultList.getResults()) {
			Set<String> treatments = result.getUniqueTreatments();

			String[] entries = new String[NUM_FIELDS + Math.min(TrecConfig.MAX_TREATMENTS, treatments.size())];

			// 0, 1, and 5 are fixed fields, but we set then here because the array size is unknown beforehand.
			entries[0] = queryIdFunction.apply(resultList.getTopic());
			entries[1] = "Q0";
			entries[2] = String.valueOf(result.getId());
			entries[3] = String.valueOf(rank++);
			entries[4] = String.format(Locale.ROOT, "%.6f", result.getScore());
			entries[5] = runName; // XXX must be 1-12 alphanumeric characters

			// Traditional runs should not set the extra stored fields and therefore will have an empty treatment list
			int i = 0;
			for (String treatment : treatments) {
				entries[6 + i++] = String.format("\"%s\"", treatment);
				if (i >= TrecConfig.MAX_TREATMENTS) {
					break;
				}
			}

			writer.writeNext(entries);
		}
	}
	
	public void flush() {
		try {
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
