package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;
import com.flint.compiler.token.fVariable.StoreType;

public class ClassParamLeafValue extends TokenLeafValue {

	//Modifiers
	public String paramName;
	public StoreType storeType = StoreType.TEMP;
	public ProdRootLeafN paramTypeLeafN;
	public ProdRootLeafN exprLeafN;

	public ClassParamLeafValue(fToken kw_token) {
		super(kw_token);
	}
}
