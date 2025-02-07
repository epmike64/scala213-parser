package com.flint.compiler.token;

import com.flint.compiler.lang.LangCheck;
import com.flint.compiler.token.type.fToken;

public class fOperatorMap {

	public static fOperatorKind getOperatorKind(fToken token) {
		switch (token.kind) {
			case T_IF:
				return fOperatorKind.O_IF;
			case T_ELSE:
				return fOperatorKind.O_ELSE;
			case T_COMMA:
				return fOperatorKind.O_COMMA;
			case T_FAT_ARROW:
				return fOperatorKind.O_FAT_ARROW;
			case T_SEMI:
				return fOperatorKind.O_SEMI;
			case T_ID:
				if(LangCheck.isValidMethName(token.name())) {
					if(token.name().endsWith(":")) {
						return fOperatorKind.O_ID_SYM_RIGHT_ASSC;
					}
					return fOperatorKind.O_ID_SYM_LEFT_ASSC;
				}
				throw new RuntimeException("Invalid Symbolic Method Name: "+token.name());
			default:
				throw new AssertionError("Token is not an operator");
		}
	}
}
