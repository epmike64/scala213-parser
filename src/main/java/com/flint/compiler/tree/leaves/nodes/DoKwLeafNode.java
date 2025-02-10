package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.DoKwLeafValue;

public class DoKwLeafNode extends CommonLeafNode{
	public DoKwLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_DO_KW_LEAF, new DoKwLeafValue(token));
	}

	public DoKwLeafValue val() {
		return (DoKwLeafValue) value;
	}
}
