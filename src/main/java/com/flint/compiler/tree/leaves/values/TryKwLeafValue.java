package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class TryKwLeafValue  extends TokenLeafValue {
	public ProdRootLeafN tryBodyLeafN;
	public ProdRootLeafN catchLeafN;
	public ProdRootLeafN finallyLeafN;
	public TryKwLeafValue(fToken kw_token) {
		super(kw_token);
	}
}
