package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;
import com.flint.compiler.token.fTokenKind;

public class IfKwLeafValue extends TokenLeafValue {
	public ProdRootLeafN ifCondLeafN;
	public ProdRootLeafN ifBodyLeafN;

	public IfKwLeafValue(fToken kw_token) {
		super(kw_token);
		assert kw_token.kind == fTokenKind.T_IF;
	}

	@Override
	public String toString() {
		return ""+token.kind+"{pos=" + token.pos +'}';
	}
}
