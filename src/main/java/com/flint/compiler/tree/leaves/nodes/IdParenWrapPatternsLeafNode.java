package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.IdParenWrapPatternsLeafValue;

public class IdParenWrapPatternsLeafNode extends CommonLeafNode {
	public IdParenWrapPatternsLeafNode(fTree.BsNode parent, fToken kw_token) {
		super(parent, fTree.fTreeNKind.N_ID_PAREN_WRAP_PATTERNS_LEAF, new IdParenWrapPatternsLeafValue(kw_token));
	}
	public IdParenWrapPatternsLeafValue val() {
		return (IdParenWrapPatternsLeafValue) value;
	}
}
