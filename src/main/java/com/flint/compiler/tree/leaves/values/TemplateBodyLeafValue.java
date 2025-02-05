package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class TemplateBodyLeafValue extends TokenLeafValue {
	ProdRootLeafN templateStatsLeafN;
	public TemplateBodyLeafValue(fToken kw_token) {
		super(kw_token);
	}

	@Override
	public String toString() {
		return ""+token.kind+"{pos=" + token.pos +'}';
	}
}
