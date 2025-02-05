package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.fTokenKind;
import com.flint.compiler.token.type.fToken;

public class ImportDefLeafValue extends TokenLeafValue {
	public String importPath;
	public ImportDefLeafValue(fToken token) {
		super(token);
		assert token.kind == fTokenKind.T_IMPORT;
	}
}
