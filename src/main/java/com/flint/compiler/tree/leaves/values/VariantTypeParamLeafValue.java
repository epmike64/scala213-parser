package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.OpChar;
import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class VariantTypeParamLeafValue extends TokenLeafValue {
	public OpChar variant = OpChar.INVALID;
	public ProdRootLeafN typeParamLeafN;
	public VariantTypeParamLeafValue(fToken kw_token) {
		super(kw_token);
	}
}
