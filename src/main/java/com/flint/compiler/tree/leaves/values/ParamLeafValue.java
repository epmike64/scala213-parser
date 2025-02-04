package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class ParamLeafValue extends TokenLeafValue {
	public String paramName;
	public ProdRootLeafN paramTypeLeafN;
	public ProdRootLeafN exprLeafN;
	public ParamLeafValue(fToken kw_token) {
		super(kw_token);
	}
}
