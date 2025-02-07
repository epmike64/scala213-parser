package com.flint.compiler.token;

import java.util.HashMap;
import java.util.Map;

public class fModifierMap {

	private static final Map<String, fModifierKind> map = new HashMap<>();

	static {
		for (fModifierKind t : fModifierKind.values()) {
			map.put(t.modname, t);
		}
	}

	public static fModifierKind getModifierKind(String name) {
		return map.getOrDefault(name, null);
	}
}
