package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.ImportDefLeafValue;

public class ImportDefLeafNode extends CommonLeafNode {
	public ImportDefLeafNode(fTree.BsNode parent, fToken value) {
		super(parent,  fTree.fTreeNKind.N_IMPORT_LEAF, new ImportDefLeafValue(value));
	}

	public ImportDefLeafValue val() {
		return (ImportDefLeafValue) value;
	}
}
