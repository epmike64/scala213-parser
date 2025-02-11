package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.ImportExprLeafValue;

public class ImportExprLeafNode extends CommonLeafNode {
	public ImportExprLeafNode(fTree.BsNode parent, fToken value) {
		super(parent,  fTree.fTreeNKind.N_IMPORT_EXPR_LEAF, new ImportExprLeafValue(value));
	}

	public ImportExprLeafValue val() {
		return (ImportExprLeafValue) value;
	}
}
