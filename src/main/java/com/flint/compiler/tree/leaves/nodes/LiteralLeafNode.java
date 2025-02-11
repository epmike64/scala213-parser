package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.LiteralLeafValue;

public class LiteralLeafNode extends CommonLeafNode {
	public LiteralLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_LITERAL_LEAF, new LiteralLeafValue(token));
	}
	public LiteralLeafValue val() {
		return (LiteralLeafValue) value;
	}
}
