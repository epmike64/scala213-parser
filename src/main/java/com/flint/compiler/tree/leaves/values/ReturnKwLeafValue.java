package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class ReturnKwLeafValue  extends TokenLeafValue {
	public ProdRootLeafN returnExprLeafN;
	public ReturnKwLeafValue(fToken kw_token) {
		super(kw_token);
	}
}
