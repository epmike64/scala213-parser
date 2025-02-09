package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.ParenWrapPatternsLeafValue;

public class ParenWrapPatternsLeafNode extends CommonLeafNode{
	public ParenWrapPatternsLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_PAREN_WRAP_PATTERNS_LEAF, new ParenWrapPatternsLeafValue(token));
	}

	public ParenWrapPatternsLeafValue val() {
		return (ParenWrapPatternsLeafValue) value;
	}
}
