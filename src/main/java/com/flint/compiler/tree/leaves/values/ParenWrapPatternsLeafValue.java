package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class ParenWrapPatternsLeafValue extends TokenLeafValue {
	public ProdRootLeafN patternsLeafN;
	public ParenWrapPatternsLeafValue(fToken kw_token) {
		super(kw_token);
	}
}
