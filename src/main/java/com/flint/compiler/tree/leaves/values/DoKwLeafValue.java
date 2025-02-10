package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class DoKwLeafValue extends TokenLeafValue {
	public ProdRootLeafN doBodyLeafN;
	public ProdRootLeafN doCondLeafN;
	public DoKwLeafValue(fToken kw_token) {
		  super(kw_token);
	 }
}
