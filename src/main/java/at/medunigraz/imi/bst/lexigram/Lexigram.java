package at.medunigraz.imi.bst.lexigram;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Lexigram {

    static String API_KEY_FILE = "src/main/resources/apikey.txt";
    static String API_KEY;

    static {
        File apiFile = new File(API_KEY_FILE);

        try {
            API_KEY = FileUtils.readFileToString(apiFile, "UTF-8").replace("\n", "");
        } catch (IOException e) {
            System.out.print("Please place your Lexigram API key in the following file: " + API_KEY_FILE);
            e.printStackTrace();
        }
    }

    private static class Cache {
        private static final String FILENAME = "cache/lexigram.ser";
        private static HashMap<String, String> CALLS = new HashMap<>();
        static {
            if (Files.exists(Paths.get(FILENAME))) {
                load();
            }
        }

        private static void load() {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME));
                CALLS = (HashMap) ois.readObject();
                ois.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private static void save() {
            try
            {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME));
                oos.writeObject(CALLS);
                oos.close();
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* Retrieves the preferred term ("label") of the best-matching concept.
    *  If no match, it returns itself */
    public static String getPreferredTerm(String label) throws UnirestException {
        Optional<String> search = search(label);
        if (!search.isPresent()) {
            return label;
        }

        try {
            /* Get info (label and synonyms) of concept */
            JSONObject body = get("https://api.lexigram.io/v1/lexigraph/concepts/"+ search.get());
            return cleanUpString(body.getString("label"));
        }
        catch (Exception e) {
            throw e;
        }

        //return label;
    }

    /* Possibly the most useful function: Given a string, it searches for the best-matching concept and adds all synonyms
     *  If no match, it returns a list with itself */
    public static List<String> addSynonymsFromBestConceptMatch(String label) throws UnirestException {
        Optional<String> search = search(label);
        if (!search.isPresent()) {
            return Collections.singletonList(label);
        }

        try {
            List<String> keywordAndSynonyms = new ArrayList<>();
            keywordAndSynonyms.add(label);

            /* Get info (label and synonyms) of concept */
            JSONObject body = get("https://api.lexigram.io/v1/lexigraph/concepts/"+ search.get());

            keywordAndSynonyms.add(body.getString("label"));

            JSONArray synonymsJson = body.getJSONArray("synonyms");

            for(int i = 0; i < synonymsJson.length(); i++) {
                keywordAndSynonyms.add(synonymsJson.get(i).toString());
            }

            return cleanUpList(keywordAndSynonyms);
        }
        catch (Exception e) {
            throw e;
        }

        //return Collections.singletonList(label);
    }

    public static Optional<String> search(String label) throws UnirestException {
        JSONObject body = get("https://api.lexigram.io/v1/lexigraph/search?q="+ URLEncoder.encode(label));
        JSONArray results = body.getJSONArray("conceptSearchHits");
        if (results.length() == 0) {
            return Optional.empty();
        }

        /* Get first concept (most relevant search hit) */
        JSONObject item = results.getJSONObject(0);
        JSONObject concept = item.getJSONObject("concept");
        return Optional.of(concept.getString("id"));
    }

    private static List<String> cleanUpList(List<String> labels) {
        Set<String> cleanLabels = new HashSet<>();
        cleanLabels.addAll(labels.stream().map(String::toLowerCase).map(l -> l.replaceAll("\\(.*\\)", "")).collect(Collectors.toList()));
        cleanLabels = cleanLabels.stream().map(l -> l.replaceAll("\\[.*\\]", "")).collect(Collectors.toSet());
        cleanLabels = cleanLabels.stream().map(l -> l.split(",")[0]).collect(Collectors.toSet());
        List<String> cleanLabelsArrayList = new ArrayList<>();
        cleanLabelsArrayList.addAll(cleanLabels);
        return cleanLabelsArrayList;
    }

    private static String cleanUpString(String label) {
        String cleanLabel;
        cleanLabel = label.toLowerCase();
        cleanLabel = label.replaceAll("\\(.*\\)", "");
        cleanLabel = cleanLabel.replaceAll("\\[.*\\]", "");
        cleanLabel = cleanLabel.split(",")[0];
        return cleanLabel;
    }

    private static JSONObject get(String url) throws UnirestException {
        if (!Cache.CALLS.containsKey(url)) {
            HttpResponse<JsonNode> response = Unirest.get(url)
                    .header("authorization", "Bearer " + API_KEY)
                    .asJson();
            JSONObject body = new JSONObject(response.getBody());
            String firstArrayObject = body.getJSONArray("array").getJSONObject(0).toString();

            Cache.CALLS.put(url, firstArrayObject);
            Cache.save();
        }

        return new JSONObject(Cache.CALLS.get(url));
    }
}
