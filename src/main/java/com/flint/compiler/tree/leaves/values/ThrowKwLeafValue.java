package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class ThrowKwLeafValue  extends TokenLeafValue {
	public ProdRootLeafN throwLeafN;
	public ThrowKwLeafValue(fToken value) {
		  super(value);
	 }
}
