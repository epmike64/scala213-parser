package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.FunSigLeafValue;

public class FunSigLeafNode extends CommonLeafNode {
	public FunSigLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_FUN_SIG_LEAF, new FunSigLeafValue(token));
	}

	public FunSigLeafValue val() {
		return (FunSigLeafValue) value;
	}
}
