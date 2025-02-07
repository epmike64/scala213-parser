package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class ClassParentsLeafValue extends TokenLeafValue {
	public ProdRootLeafN constrType;
	public ProdRootLeafN constrArgExprs;

	public ClassParentsLeafValue(fToken kw_token) {
		super(kw_token);
	}
}
