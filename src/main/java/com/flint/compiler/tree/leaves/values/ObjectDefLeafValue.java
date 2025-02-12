package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.fTokenKind;
import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

public class ObjectDefLeafValue extends TokenLeafValue {
	public String className;
	public boolean isCase;
	public boolean extends_;
	public ProdRootLeafN templateBodyLeafN;
	public ProdRootLeafN classParentsLeafN;

	public ObjectDefLeafValue(fToken kw_token) {
		super(kw_token);
		check();
	}

	protected void check(){
		assert token.kind == fTokenKind.T_OBJECT;
	}
}
