package de.julielab.ir.ltr;

import de.julielab.ir.model.QueryDescription;

import java.io.File;
import java.io.IOException;

/**
 * Abstraction layer for all algorithms that rank a list of documents.
 */
public interface Ranker<Q extends QueryDescription> {

    void train(DocumentList<Q> documents);

    void load(File modelFile) throws IOException;

    void save(File modelFile);

    DocumentList<Q> rank(DocumentList<Q> documents);
}
