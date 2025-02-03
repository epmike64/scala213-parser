package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.fTokenKind;
import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class GuardLeafValue extends TokenLeafValue{
	public ProdRootLeafN ifCondLeafN;
	public GuardLeafValue(fToken kw_token) {
		super(kw_token);
		assert kw_token.kind == fTokenKind.T_CASE;
	}
}
