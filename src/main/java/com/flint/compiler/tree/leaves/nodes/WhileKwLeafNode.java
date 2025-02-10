package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.WhileKwLeafValue;

public class WhileKwLeafNode extends CommonLeafNode {
	public WhileKwLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_WHILE_KW_LEAF, new WhileKwLeafValue(token));
	}

	public WhileKwLeafValue val() {
		return (WhileKwLeafValue) value;
	}
}
