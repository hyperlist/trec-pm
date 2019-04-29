package de.julielab.ir.ltr;

import java.io.File;

/**
 * Abstraction layer for all algorithms that rank a list of documents.
 */
public interface Ranker {

    void train(DocumentList documents);

    void load(File modelFile);

    void save(File modelFile);

    DocumentList rank(DocumentList documents);
}
