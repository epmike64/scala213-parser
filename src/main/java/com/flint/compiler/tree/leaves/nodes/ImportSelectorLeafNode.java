package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.ImportSelectorLeafValue;

public class ImportSelectorLeafNode extends CommonLeafNode {
	public ImportSelectorLeafNode(fTree.BsNode parent, fToken value) {
		super(parent,  fTree.fTreeNKind.N_IMPORT_SELECTOR_LEAF, new ImportSelectorLeafValue(value));
	}

	public ImportSelectorLeafValue val() {
		return (ImportSelectorLeafValue) value;
	}
}
