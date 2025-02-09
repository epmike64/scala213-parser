package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class IdParenWrapPatternsLeafValue extends TokenLeafValue {
	public String idName;
	public ProdRootLeafN patternsLeafN;
	public IdParenWrapPatternsLeafValue(fToken kw_token) {
		super(kw_token);
	}
}
