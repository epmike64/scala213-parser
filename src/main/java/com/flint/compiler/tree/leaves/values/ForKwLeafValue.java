package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class ForKwLeafValue  extends TokenLeafValue {
	public ProdRootLeafN enumeratorsLeafN;
	public ProdRootLeafN yieldLeafN;
	public ForKwLeafValue(fToken value) {
		  super(value);
	 }
}
