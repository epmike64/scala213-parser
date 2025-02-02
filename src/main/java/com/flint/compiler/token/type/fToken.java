package com.flint.compiler.token.type;

import com.flint.compiler.token.fTokenKind;

public class fToken {

	public final int pos, endPos;
	public final fTokenKind kind;

	public fToken(fTokenKind kind, int pos, int endPos) {
		this.kind = kind;
		this.pos = pos;
		this.endPos = endPos;
	}

	public String name() {
		return kind.tkName;
	}

	public String stringVal() {
		throw new UnsupportedOperationException();
	}

	public int radix() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return "Token{" +
				"kind=" + kind +
				", pos=" + pos +
				", endPos=" + endPos +
				'}';
	}
}
