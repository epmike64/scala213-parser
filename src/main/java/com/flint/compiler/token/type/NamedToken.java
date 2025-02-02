package com.flint.compiler.token.type;

import com.flint.compiler.token.fTokenKind;
import com.flint.compiler.token.fTokenTag;

public class NamedToken extends fToken {
	/** The name of this token */
	public final String name;

	public NamedToken(fTokenKind kind, int pos, int endPos, String name) {
		super(kind, pos, endPos);
		this.name = name;
	}

	public void checkKind() {
		if (kind.tag != fTokenTag.NAMED) {
			throw new AssertionError("Bad token kind - expected " + fTokenTag.NAMED);
		}
	}

	@Override
	public String toString() {
		return "NamedToken{" +
				"name='" + name + '\'' +
				", kind=" + kind +
				", pos=" + pos +
				", endPos=" + endPos +
				'}';
	}

	@Override
	public String name() {
		return name;
	}
}