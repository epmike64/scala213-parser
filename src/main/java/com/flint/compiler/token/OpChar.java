package com.flint.compiler.token;

public enum OpChar {
	COLON(':'),
	ASSIGN('='),
	STAR('*'),
	FORWARD_SLASH('/'),
	PLUS('+'),
	MINUS('-'),
	LT('<'),
	GT('>'),
	PIPE('|'),
	AMPERSAND('&'),
	BANG('!'),
	POUND('#'),
	PERCENT('%'),
	QUESTION('?'),
	AT('@'),
	BACKSLASH('\\'),
	CARET('^'),
	TILDE('~');

	OpChar(char opchar) {
		this.opchar = opchar;
	}

	public final char opchar;
}