package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class FunDclLeafValue extends TokenLeafValue {
	public String funName;
	public ProdRootLeafN funTypeParamsLeafN;
	public ProdRootLeafN funParamsLeafN;
	public FunDclLeafValue(fToken kw_token) {
		super(kw_token);
	}
}
