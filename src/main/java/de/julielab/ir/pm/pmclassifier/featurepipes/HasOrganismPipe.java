package de.julielab.ir.pm.pmclassifier.featurepipes;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;
import cc.mallet.types.Token;
import de.julielab.ir.pm.pmclassifier.Document;

public class HasOrganismPipe extends Pipe {

    @Override
    public Instance pipe(Instance inst) {
        Document document = (Document) inst.getSource();
        Token token = (Token) inst.getData();
        if (document.getOrganisms() != null) {
            for (String gene : document.getOrganisms())
                token.setFeatureValue("hasOrganism="+gene.toLowerCase(), 1);
        }
        return inst;
    }
}
