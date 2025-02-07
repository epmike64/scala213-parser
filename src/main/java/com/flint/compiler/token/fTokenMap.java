package com.flint.compiler.token;
import java.util.HashMap;
import java.util.Map;

public class fTokenMap {
	private static final Map<String, fTokenKind> map = new HashMap<>();

	static {
		for (fTokenKind token : fTokenKind.values()) {
			if(token.tkName != null) map.put(token.tkName, token);
		}
	}

	public static fTokenKind lookupKind(String name) {
		return map.getOrDefault(name, fTokenKind.T_ID);
	}
}