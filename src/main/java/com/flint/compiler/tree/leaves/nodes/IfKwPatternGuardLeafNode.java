package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;

public class IfKwPatternGuardLeafNode extends IfKwLeafNode {
	public IfKwPatternGuardLeafNode(fTree.BsNode parent, fToken kw_token) {
		super(parent, kw_token);
	}

	public ProdRootLeafN postfixExprLeafN() {
		return ifCondLeafN();
	}

	public ProdRootLeafN matchCasesLeafN() {
		return ifBodyLeafN();
	}
}
