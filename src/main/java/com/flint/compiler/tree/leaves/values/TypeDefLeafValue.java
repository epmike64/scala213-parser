package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.fTokenKind;
import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class TypeDefLeafValue extends TokenLeafValue {
	public String typeName;
	public ProdRootLeafN variantTypeParamsLeafN;
	public ProdRootLeafN typeLeafN;
	public TypeDefLeafValue(fToken kw_token) {
		super(kw_token);
		assert kw_token.kind == fTokenKind.T_TYPE;
	}
}
