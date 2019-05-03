package de.julielab.ir.ltr;

import de.julielab.ir.model.QueryDescription;

import java.util.ArrayList;

/**
 * A list of documents they either represent labeled data for training or unlabeled data to be ranked.
 */
public class DocumentList<Q extends QueryDescription> extends ArrayList<Document<Q>> {
}
