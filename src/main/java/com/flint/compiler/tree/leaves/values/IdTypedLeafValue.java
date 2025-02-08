package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.NamedToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class IdTypedLeafValue extends NamedTokenLeafValue {
	public ProdRootLeafN typeLeafN;
	public IdTypedLeafValue(NamedToken name) {
		super(name);
	}
}
