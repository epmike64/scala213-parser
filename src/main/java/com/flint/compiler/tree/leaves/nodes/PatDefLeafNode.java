package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.PatDefLeafValue;

public class PatDefLeafNode extends CommonLeafNode {
	public PatDefLeafNode(fTree.BsNode parent, fToken kw_token) {
		super(parent, fTree.fTreeNKind.N_PAT_DEF_LEAF, new PatDefLeafValue(kw_token));
	}

	public PatDefLeafValue val() {
		return (PatDefLeafValue) value;
	}
}
