package de.julielab.ir.ltr;

import de.julielab.ir.model.Query;

import java.util.ArrayList;

/**
 * A list of documents they either represent labeled data for training or unlabeled data to be ranked.
 */
public class DocumentList<Q extends Query> extends ArrayList<Document<Q>> {
}
