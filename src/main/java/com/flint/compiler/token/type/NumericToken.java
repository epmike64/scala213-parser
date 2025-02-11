package com.flint.compiler.token.type;

import com.flint.compiler.token.fTokenKind;
import com.flint.compiler.token.fTokenTag;

public class NumericToken extends StringToken {
	/** The 'radix' value of this token */
	public final int radix;

	public NumericToken(fTokenKind kind, int pos, int endPos, String stringVal, int radix) {
		super(kind, pos, endPos, stringVal);
		this.radix = radix;
		assert kind.tag == fTokenTag.NUMERIC;
	}

	@Override
	public int radix() {
		return radix;
	}

	@Override
	public String toString() {
		return "NumericToken{" +
				"radix=" + radix +
				", stringVal='" + stringVal + '\'' +
				", kind=" + kind +
				", pos=" + pos +
				", endPos=" + endPos +
				'}';
	}
}