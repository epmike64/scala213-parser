package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;

public class ImportSelectorLeafValue extends TokenLeafValue {
	public String rename;
	public ImportSelectorLeafValue(fToken token) {
		super(token);
	}
}
