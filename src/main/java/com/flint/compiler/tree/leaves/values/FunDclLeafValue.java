package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class FunDclLeafValue extends TokenLeafValue {
	public ProdRootLeafN funSigLeafN;
	public ProdRootLeafN funSigTypeLeafN;
	public ProdRootLeafN funSigExprLeafN;
	public ProdRootLeafN funSigBlockLeafN;
	public ProdRootLeafN thisParamsLeafN;
	public FunDclLeafValue(fToken kw_token) {
		super(kw_token);
	}
}
