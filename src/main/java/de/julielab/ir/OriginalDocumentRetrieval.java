package de.julielab.ir;

import at.medunigraz.imi.bst.config.TrecConfig;
import de.julielab.java.utilities.FileUtilities;
import de.julielab.xml.XmiBuilder;
import de.julielab.xml.XmiSplitConstants;
import de.julielab.xmlData.dataBase.CoStoSysConnection;
import de.julielab.xmlData.dataBase.DBCIterator;
import de.julielab.xmlData.dataBase.DataBaseConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.uima.UIMAException;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.xml.sax.SAXException;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OriginalDocumentRetrieval {

    private static OriginalDocumentRetrieval instance;
    /**
     * This map contains the XMI namespace definitions required to rebuild valid XMI documents from the
     * XMI elements we will retrieve from the database. The base document and the annotations are all stored
     * without this information which is why it has to be added when assembling the final XMI document.
     */
    private final Map<String, String> namespaceMap;
    /**
     * This object takes the XMI document parts: The base document and possibly annotations retrieved additionally
     * from the database. It also makes takes the namespace map to then assembly complete XMI documents.
     */
    private final XmiBuilder xmiBuilder;
    private Logger log = LogManager.getLogger();
    /**
     * The object that offers convenience methods to retrieve documents from a Postgres database.
     */
    private DataBaseConnector dbc;
    /**
     * This list contains fully qualified Java names of the UIMA types. For those types we want to retrieve
     * annotations from the database. This list is read from a configuration file.
     */
    private String[] annotationTypesToRetrieve;
    /**
     * The set of tables from which we want to retrieve information. That is the table with the base document (document
     * text plus document meta data and structures) plus any annotation tables we want to use. This array is
     * derived from the annotationTypesToRetrieve array.
     */
    private String[] tablesToJoin;
    /**
     * This is a parallel array to tablesToJoin and lists the database table schemas, as defined in the DBC,
     * to be used when retrieving data from the respective tables.
     */
    private String[] schemaNames;

    private OriginalDocumentRetrieval() throws IOException {

        dbc = new DataBaseConnector(TrecConfig.COSTOSYS_CONFIG);
        try (BufferedReader br = FileUtilities.getReaderFromFile(new File(TrecConfig.COSTOSYS_ANNOTATIONS_LIST))) {
            annotationTypesToRetrieve = br.lines().toArray(String[]::new);
        }
        tablesToJoin = Stream.of(annotationTypesToRetrieve).map(type -> type.replaceAll("\\.", "_").toLowerCase()).toArray(String[]::new);
        final List<Map<String, String>> primaryKeyFields = dbc.getActiveTableFieldConfiguration().getPrimaryKeyFields().collect(Collectors.toList());
        final String baseDocumentTableSchema = dbc.addXmiDocumentFieldConfiguration(primaryKeyFields, true).getName();
        final String annotationTableSchema = dbc.addXmiAnnotationFieldConfiguration(primaryKeyFields, true).getName();
        schemaNames = new String[tablesToJoin.length];
        schemaNames[0] = baseDocumentTableSchema;
        for (int i = 1; i < schemaNames.length; i++) {
            schemaNames[i] = annotationTableSchema;
        }
        try (CoStoSysConnection ignored = dbc.obtainOrReserveConnection()) {
            namespaceMap = getNamespaceMap();
        }
        xmiBuilder = new XmiBuilder(namespaceMap, annotationTypesToRetrieve);
    }

    public static void initialize() throws IOException {
        if (instance == null) {
            instance = new OriginalDocumentRetrieval();
        }
    }

    public static OriginalDocumentRetrieval getInstance() {
        return instance;
    }

    /**
     * Retrieves CAS data from the Postgres database for the given document IDs. Note that the returned CAS
     * is actually the same for each call of {@link Iterator#next()} and that each such call will reset the CAS.
     * Thus, before advancing the iterator, all required processing should have finished with the current
     * CAS contents.
     * @param ids The IDs of the documents to retrieve from the database.
     * @return An iterator for the retrieved documents.
     */
    public Iterator<JCas> getDocuments(List<Object[]> ids) {
        final DBCIterator<byte[][]> dbcIterator = dbc.retrieveColumnsByTableSchema(ids, tablesToJoin, schemaNames);
        try {
            return new Iterator<>() {

                private JCas cas = JCasFactory.createJCas("de.julielab.jcore.types.jcore-all-types");

                @Override
                public boolean hasNext() {
                    return dbcIterator.hasNext();
                }

                @Override
                public JCas next() {
                    cas.reset();
                    final byte[][] xmiData = dbcIterator.next();
                    LinkedHashMap<String, InputStream> dataMap = new LinkedHashMap<>();
                    for (int i = 0; i < tablesToJoin.length; i++) {
                        dataMap.put(tablesToJoin[i], new ByteArrayInputStream(xmiData[i]));
                    }
                    final ByteArrayOutputStream xmiBaos = xmiBuilder.buildXmi(dataMap, TrecConfig.COSTOSYS_BASEDOCUMENTS, cas.getTypeSystem());
                    try {
                        XmiCasDeserializer.deserialize(new ByteArrayInputStream(xmiBaos.toByteArray()), cas.getCas());
                    } catch (SAXException | IOException e) {
                        log.error("Could not deserialize XMI data into a CAS", e);
                    }
                    return cas;
                }
            };
        } catch (UIMAException e) {
            log.error("Could not create an iterator to return CAS data from the database", e);
        }
        return null;
    }

    private Map<String, String> getNamespaceMap() {
        Map<String, String> map = null;
        if (dbc.tableExists(dbc.getActiveDataPGSchema() + "." + XmiSplitConstants.XMI_NS_TABLE)) {
            try (CoStoSysConnection conn = dbc.obtainOrReserveConnection()) {
                map = new HashMap<>();
                conn.setAutoCommit(true);
                Statement stmt = conn.createStatement();
                String sql = String.format("SELECT %s,%s FROM %s", XmiSplitConstants.PREFIX, XmiSplitConstants.NS_URI,
                        dbc.getActiveDataPGSchema() + "." + XmiSplitConstants.XMI_NS_TABLE);
                ResultSet rs = stmt.executeQuery(String.format(sql));
                while (rs.next())
                    map.put(rs.getString(1), rs.getString(2));
            } catch (SQLException e) {
                e.printStackTrace();
                SQLException ne = e.getNextException();
                if (null != ne)
                    ne.printStackTrace();
            }
        } else {
            log.warn(
                    "Table \"{}\" was not found it is assumed that the table from which is read contains complete XMI documents.",
                    dbc.getActiveDataPGSchema() + "." + XmiSplitConstants.XMI_NS_TABLE);
        }
        return map;
    }
}
