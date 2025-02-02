package com.flint.compiler.token.type;

import com.flint.compiler.token.fTokenKind;
import com.flint.compiler.token.fTokenTag;

public class StringToken extends fToken {
	/** The string value of this token */
	public final String stringVal;

	public StringToken(fTokenKind kind, int pos, int endPos, String stringVal) {
		super(kind, pos, endPos);
		this.stringVal = stringVal;
	}

	protected void checkKind() {
		if (kind.tag != fTokenTag.STRING) {
			throw new AssertionError("Bad token kind - expected " + fTokenTag.STRING);
		}
	}

	@Override
	public String toString() {
		return "StringToken{" +
				"stringVal='" + stringVal + '\'' +
				", kind=" + kind +
				", pos=" + pos +
				", endPos=" + endPos +
				'}';
	}
}