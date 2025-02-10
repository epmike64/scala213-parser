package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.ForKwLeafValue;

public class ForKwLeafNode extends CommonLeafNode{
	public ForKwLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_FOR_KW_LEAF, new ForKwLeafValue(token));
	}

	public ForKwLeafValue val() {
		return (ForKwLeafValue) value;
	}
}
