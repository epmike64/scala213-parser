package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.TryKwLeafValue;

public class TryKwLeafNode extends CommonLeafNode{
	public TryKwLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_TRY_KW_LEAF, new TryKwLeafValue(token));
	}

	public TryKwLeafValue val() {
		return (TryKwLeafValue) value;
	}
}
