package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;

import static com.flint.compiler.token.fTokenKind.T_NEW;

public class InstanceCreationLeafValue extends ObjectDefLeafValue {

	public InstanceCreationLeafValue(fToken token) {
		super(token);
		assert token.kind == T_NEW;
	}
}
