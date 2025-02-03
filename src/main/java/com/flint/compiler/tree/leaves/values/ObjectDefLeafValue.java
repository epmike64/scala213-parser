package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;

public class ObjectDefLeafValue extends TokenLeafValue {
	public String objectName;
	public boolean isCase;
	public ObjectDefLeafValue(fToken kw_token) {
		super(kw_token);
	}
}
