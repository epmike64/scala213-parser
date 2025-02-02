package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.IfKwLeafValue;

public class IfKwLeafNode extends CommonLeafNode {
	public IfKwLeafNode(fTree.BsNode parent, fToken kw_token) {
		super(parent, fTree.fTreeNKind.N_IF_KW_LEAF, new IfKwLeafValue(kw_token));
	}

	public IfKwLeafValue val() {
		return (IfKwLeafValue) value;
	}

	public ProdRootLeafN ifCondLeafN() {
		return val().ifCondLeafN;
	}

	public ProdRootLeafN ifBodyLeafN() {
		return val().ifBodyLeafN;
	}

	@Override
	public String toString() {
		return "" + value;
	}
}
