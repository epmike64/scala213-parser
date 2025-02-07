package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.fTokenKind;
import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class ClassDefLeafValue extends TokenLeafValue {
	public String className;
	public boolean isCase;
	public ProdRootLeafN typeParamsLeafN;
	public ProdRootLeafN accessModifierLeafN;
	public ProdRootLeafN classParamsLeafN;
	public boolean extends_;
	public ProdRootLeafN templateBodyLeafN;
	public ProdRootLeafN classParentsLeafN;

	public ClassDefLeafValue(fToken kw_token) {
		super(kw_token);
		assert kw_token.kind == fTokenKind.T_CLASS;
	}
}
