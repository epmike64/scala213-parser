package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.NamedToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class FunCallLeafValue extends NamedTokenLeafValue {
	public ProdRootLeafN funcArgs;
	public FunCallLeafValue(NamedToken funNameTok) {
		super(funNameTok);
	}
}
