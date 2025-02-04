package com.flint.compiler.token;

import com.flint.compiler.token.type.fToken;

public class fOperatorMap {

	private static final java.util.Map<String, fOperatorKind> map = new java.util.HashMap<>();

	static {
		for (fOperatorKind t : fOperatorKind.values()) {
			map.put(t.opname, t);
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
				break;
			default:
				throw new AssertionError("Token is not an operator");
		}
		return map.getOrDefault(token.name(), null);
	}
}
