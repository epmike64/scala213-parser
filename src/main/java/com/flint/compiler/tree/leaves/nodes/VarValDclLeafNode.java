package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.VarValDclLeafValue;

public class VarValDclLeafNode extends CommonLeafNode {
	public VarValDclLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_VAR_VAL_DCL_LEAF, new VarValDclLeafValue(token));
	}

	public VarValDclLeafValue val() {
		return (VarValDclLeafValue) value;
	}
}
