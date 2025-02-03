package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.CaseKwLeafValue;

public class CaseKwLeafNode extends CommonLeafNode {
	public CaseKwLeafNode(fTree.BsNode parent, fToken kw_token) {
		super(parent, fTree.fTreeNKind.N_CASE_KW_LEAF, new CaseKwLeafValue(kw_token));
	}
	public CaseKwLeafValue val() {
		return (CaseKwLeafValue) value;
	}
}
