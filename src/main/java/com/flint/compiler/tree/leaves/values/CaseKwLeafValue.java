package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.fTokenKind;
import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class CaseKwLeafValue extends TokenLeafValue {
	public ProdRootLeafN patternLeafN;
	public ProdRootLeafN guardLeafN;
	public ProdRootLeafN blockLeafN;
	public CaseKwLeafValue(fToken kw_token) {
		super(kw_token);
		assert kw_token.kind == fTokenKind.T_CASE;
	}

	@Override
	public String toString() {
		return ""+token.kind+"{pos=" + token.pos +'}';
	}
}
