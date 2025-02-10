package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.ThrowKwLeafValue;

public class ThrowKwLeafNode extends CommonLeafNode{
	public ThrowKwLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_THROW_KW_LEAF, new ThrowKwLeafValue(token));
	}

	public ThrowKwLeafValue val() {
		return (ThrowKwLeafValue) value;
	}
}
