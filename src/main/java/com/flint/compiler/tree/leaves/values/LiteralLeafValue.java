package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.fTokenKind;
import com.flint.compiler.token.type.fToken;

public class LiteralLeafValue extends TokenLeafValue {
	public LiteralLeafValue(fToken token) {
		super(token);
		assert token.kind == fTokenKind.T_INT_LIT || token.kind == fTokenKind.T_FLOAT_LIT || token.kind == fTokenKind.T_STR_LIT || token.kind == fTokenKind.T_CHR_LIT || token.kind == fTokenKind.T_TRUE || token.kind == fTokenKind.T_FALSE || token.kind == fTokenKind.T_NULL;
	}
}
