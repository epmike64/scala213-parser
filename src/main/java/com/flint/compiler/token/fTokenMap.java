package com.flint.compiler.token;

public class fTokenMap {
	private static final java.util.Map<String, fTokenKind> map = new java.util.HashMap<>();

	static {
		for (fTokenKind token : fTokenKind.values()) {
			if(token.tkName != null) map.put(token.tkName, token);
		}
	}

	public static fTokenKind lookupKind(String name) {
		return map.getOrDefault(name, fTokenKind.T_ID);
	}
}