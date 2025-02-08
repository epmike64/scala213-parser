package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.fVariable;
import com.flint.compiler.token.type.fToken;
import com.flint.compiler.token.fTokenKind;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class PatDefLeafValue extends TokenLeafValue {
	public fVariable.DefDcl defDcl;
	public fVariable.StoreType storeType;
	public ProdRootLeafN pattern2sLeafN;
	public ProdRootLeafN typeLeafN;
	public ProdRootLeafN exprLeafN;

	public PatDefLeafValue(fToken kw_token) {
		super(kw_token);
		assert kw_token.kind == fTokenKind.T_VAL;
	}

	@Override
	public String toString() {
		return ""+token.kind+"{pos=" + token.pos +'}';
	}
}
