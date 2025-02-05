package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.lang.Lang;
import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class ClassParamLeafValue extends TokenLeafValue {

	//Modifiers
	public String paramName;
	public Lang.LifeScope lifeScope = Lang.LifeScope.DEFAULT;
	public ProdRootLeafN paramTypeLeafN;
	public ProdRootLeafN exprLeafN;

	public ClassParamLeafValue(fToken kw_token) {
		super(kw_token);
	}
}
