package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.IdAtPattern3LeafValue;

public class IdAtPattern3LeafNode extends CommonLeafNode{
	public IdAtPattern3LeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_ID_AT_PATTERN3_LEAF, new IdAtPattern3LeafValue(token));
	}

	public IdAtPattern3LeafValue val() {
		return (IdAtPattern3LeafValue) value;
	}
}
