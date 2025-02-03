package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class ConstrPatternLeafValue extends TokenLeafValue {
	public ProdRootLeafN patternLeafN;
	public ConstrPatternLeafValue(fToken kw_token) {
		super(kw_token);
	}
}
