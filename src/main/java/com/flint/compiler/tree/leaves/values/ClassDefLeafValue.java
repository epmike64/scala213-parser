package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class ClassDefLeafValue extends TokenLeafValue {
	public String className;
	public boolean isCase;
	public ProdRootLeafN typeParamsLeafN;
	public ProdRootLeafN classParamsLeafN;
	public ProdRootLeafN extendsLeafN;
	public ProdRootLeafN templateBodyLeafN;

	public ClassDefLeafValue(fToken kw_token) {
		super(kw_token);
	}
}
