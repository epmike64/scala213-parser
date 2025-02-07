package com.flint.compiler.token;

import java.util.HashMap;
import java.util.Map;
import com.flint.compiler.token.fModifier.fModifierKind;
import com.flint.compiler.token.fModifier.fAccessQualifier;

public class fModifierMap {

	private static final Map<String, fModifierKind> modKMap = new HashMap<>();
	private static final Map<String, fAccessQualifier> accQmap = new HashMap<>();

	static {
		for (fModifierKind t : fModifier.fModifierKind.values()) {
			modKMap.put(t.modname, t);
		}
		for (fAccessQualifier t : fModifier.fAccessQualifier.values()) {
			accQmap.put(t.name(), t);
		}
	}

	public static fModifierKind getModifierKind(String name) {
		return modKMap.getOrDefault(name, null);
	}

	public static fAccessQualifier getAccessQualifier(String name) {
		return accQmap.getOrDefault(name, null);
	}
}
