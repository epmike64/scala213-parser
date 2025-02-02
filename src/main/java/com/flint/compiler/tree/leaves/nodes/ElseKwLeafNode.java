package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.ElseKwLeafValue;

public class ElseKwLeafNode extends CommonLeafNode {
	public ElseKwLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_ELSE_KW_LEAF, new ElseKwLeafValue(token));
	}

	public ElseKwLeafValue val() {
		return (ElseKwLeafValue) value;
	}

	public IfKwLeafNode ifKwLeafNode() {
		return val().ifLeafN;
	}

	public ProdRootLeafN elseKwLeafNode() {
		return val().elseLeafN;
	}

	@Override
	public String toString() {
		return "" + value;
	}
}
