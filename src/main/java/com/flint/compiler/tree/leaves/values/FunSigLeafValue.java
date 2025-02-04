package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class FunSigLeafValue extends TokenLeafValue {
	public String funName;
	public ProdRootLeafN typeParamsLeafN;
	public ProdRootLeafN paramsLeafN;
	public FunSigLeafValue(fToken kw_token) {
		super(kw_token);
	}

	@Override
	public String toString() {
		return ""+token.kind+"{pos=" + token.pos +'}';
	}
}
