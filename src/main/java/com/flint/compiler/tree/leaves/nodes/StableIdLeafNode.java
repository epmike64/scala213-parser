package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.StableIdLeafValue;

public class StableIdLeafNode extends CommonLeafNode {
	public StableIdLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_STABLE_ID_LEAF, new StableIdLeafValue(token));
	}
	public StableIdLeafValue val() {
		return (StableIdLeafValue) value;
	}
}
