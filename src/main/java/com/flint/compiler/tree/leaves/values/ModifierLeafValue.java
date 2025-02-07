package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.fModifier;
import com.flint.compiler.token.type.fToken;

public class ModifierLeafValue extends TokenLeafValue {
	private fModifier.fModifierKind modifierKind;
	private fModifier.fAccessQualifier accessQualifier;

	public ModifierLeafValue(fToken kw_token) {
		super(kw_token);
	}
	public fModifier.fModifierKind getModifierKind() {
		return modifierKind;
	}

	public void setModifierKind(fModifier.fModifierKind mk) {
		assert mk != null;
		this.modifierKind = mk;
	}

	public fModifier.fAccessQualifier getAccessQualifier() {
		return accessQualifier;
	}

	public void setAccessQualifier(fModifier.fAccessQualifier aq) {
		assert aq != null;
		this.accessQualifier = aq;
	}

}
