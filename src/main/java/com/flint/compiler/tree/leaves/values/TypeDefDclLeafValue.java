package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.fTokenKind;
import com.flint.compiler.token.fVariable;
import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class TypeDefDclLeafValue extends TokenLeafValue {
	public fVariable.DefDcl defDcl;
	public String typeName;
	public ProdRootLeafN variantTypeParamsLeafN;
	public ProdRootLeafN defTypeLeafN;
	public ProdRootLeafN dclLowerTypeLeafN;
	public ProdRootLeafN dclUpperTypeLeafN;
	public TypeDefDclLeafValue(fToken kw_token) {
		super(kw_token);
		assert kw_token.kind == fTokenKind.T_TYPE;
	}
}
