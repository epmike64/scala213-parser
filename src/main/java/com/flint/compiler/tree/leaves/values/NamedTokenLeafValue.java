package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.NamedToken;

public class NamedTokenLeafValue extends TokenLeafValue {
	public NamedTokenLeafValue(NamedToken name) {
		super(name);
	}

	public NamedToken token() {
		return (NamedToken) token;
	}
}
