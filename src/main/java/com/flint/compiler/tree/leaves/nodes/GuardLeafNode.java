package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.GuardLeafValue;

public class GuardLeafNode extends CommonLeafNode {
	public GuardLeafNode(fTree.BsNode parent,  fToken kw_token) {
		super(parent, fTree.fTreeNKind.N_GUARD_LEAF, new GuardLeafValue(kw_token));
	}

	public GuardLeafValue val() {
		return (GuardLeafValue) value;
	}

	@Override
	public String toString() {
		return "guard";
	}
}
