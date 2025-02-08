package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.TypeDefDclLeafValue;

public class TypeDefDclLeafNode extends CommonLeafNode {
	public TypeDefDclLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_TYPE_DEF_DCL_LEAF, new TypeDefDclLeafValue(token));
	}

	public TypeDefDclLeafValue val() {
		return (TypeDefDclLeafValue) value;
	}
}
