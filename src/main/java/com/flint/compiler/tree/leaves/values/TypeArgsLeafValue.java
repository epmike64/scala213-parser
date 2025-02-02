package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.NamedToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class TypeArgsLeafValue extends NamedTokenLeafValue {
	public ProdRootLeafN typeArgs;
	public TypeArgsLeafValue(NamedToken typeNameTok) {
		super(typeNameTok);
	}
}
