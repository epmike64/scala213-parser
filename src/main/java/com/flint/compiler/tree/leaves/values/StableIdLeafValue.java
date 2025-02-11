package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;

public class StableIdLeafValue extends TokenLeafValue{
	public String classQualifier;
	public StableIdLeafValue(fToken token) {
		super(token);
	}
}
