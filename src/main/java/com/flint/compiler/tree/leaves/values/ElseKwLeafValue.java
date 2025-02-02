package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.fTokenKind;
import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;
import com.flint.compiler.tree.leaves.nodes.IfKwLeafNode;

public class ElseKwLeafValue extends TokenLeafValue {
	public IfKwLeafNode ifLeafN;
	public ProdRootLeafN elseLeafN;

	public ElseKwLeafValue(fToken kw_token) {
		super(kw_token);
		assert kw_token.kind == fTokenKind.T_ELSE;
	}

	@Override
	public String toString() {
		return ""+token.kind+"{pos=" + token.pos +'}';
	}
}
