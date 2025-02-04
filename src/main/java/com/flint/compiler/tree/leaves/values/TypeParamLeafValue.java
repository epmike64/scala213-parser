package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

import java.util.List;

public class TypeParamLeafValue extends TokenLeafValue{
	public String typeParamName;
	public ProdRootLeafN typeParamClauseLeafN;
	public ProdRootLeafN lowerBoundLeafN;
	public ProdRootLeafN upperBoundLeafN;
	public ProdRootLeafN lessPercentBoundLeafN;
	public List<ProdRootLeafN> endTypeLeafNs;
	public TypeParamLeafValue(fToken kw_token) {
		super(kw_token);
	}
}
