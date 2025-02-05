package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class TraitDefLeafValue extends TokenLeafValue {
	public String traitName;
	public ProdRootLeafN variantTypeParamsLeafN;
	public ProdRootLeafN traitParentsLeafN;
	public ProdRootLeafN templateBodyLeafN;
	public boolean extends_;
	public TraitDefLeafValue(fToken kw_token) {
		super(kw_token);
	}
}
