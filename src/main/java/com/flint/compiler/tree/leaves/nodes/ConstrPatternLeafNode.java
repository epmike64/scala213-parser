package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.ConstrPatternLeafValue;

public class ConstrPatternLeafNode extends CommonLeafNode {
	public ConstrPatternLeafNode(fTree.BsNode parent, fToken kw_token) {
		super(parent, fTree.fTreeNKind.N_CONSTR_PATTERN_LEAF, new ConstrPatternLeafValue(kw_token));
	}
	public ConstrPatternLeafValue val() {
		return (ConstrPatternLeafValue) value;
	}
}
