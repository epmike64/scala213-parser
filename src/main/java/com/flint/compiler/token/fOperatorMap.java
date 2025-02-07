package com.flint.compiler.token;

import com.flint.compiler.lang.LangCheck;
import com.flint.compiler.token.type.fToken;

import java.util.HashMap;
import java.util.Map;

public class fOperatorMap {
	private static final Map<String, fOperatorKind> map = new HashMap<>();

	static {
		for (fOperatorKind token : fOperatorKind.values()) {
			if(!token.opname.startsWith("@")) map.put(token.opname, token);
		}
	}


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
				fOperatorKind op = map.get(token.name());
				if(op != null) return op;
				if(LangCheck.isValidMethName(token.name())) {
					if(token.name().endsWith(":")) {
						return fOperatorKind.O_ID_SMBLC_RIGHT_ASSC;
					}
					return fOperatorKind.O_ID_SMBLC_LEFT_ASSC;
				}
				throw new RuntimeException("Invalid Symbolic Method Name: "+token.name());
			default:
				throw new AssertionError("Token is not an operator");
		}
	}
}
