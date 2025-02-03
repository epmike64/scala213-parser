package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;

public class ClassDefLeafValue extends TokenLeafValue {
	public String className;
	public boolean isCase;
	public ClassDefLeafValue(fToken kw_token) {
		super(kw_token);
	}
}
