package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.FunDclLeafValue;

public class FunDclDefLeafNode extends CommonLeafNode {
	public FunDclDefLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_FUN_DCL_LEAF, new FunDclLeafValue(token));
	}

	public FunDclLeafValue val() {
		return (FunDclLeafValue) value;
	}
}
