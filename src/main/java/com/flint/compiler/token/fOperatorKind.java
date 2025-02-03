package com.flint.compiler.token;

public enum fOperatorKind {

	O_COLON(":", 7, true),
	O_FAT_ARROW("=>", 6, false),
	O_MULTIPLY("*", 5, false),
	O_DIVIDE("/", 5, false),
	O_MODULO("%", 5, false),
	O_PLUS("+", 4, false),
	O_MINUS("-", 4, false),
	O_IF("if", 3, false),
	O_ELSE("else", 3, false),
	O_PIPE("|", 2, false),
	O_COMMA(",", 2, false),
	O_LEFT_PAREN("(", 2, false),
	O_ASSIGN("=", 1, false),
	O_ROOT("", 0, false),

	O_RIGHT_PAREN(")", -1, false);

	fOperatorKind(String name, int precedence, boolean isRightAssociative) {
		this.opname = name;
		this._prec = precedence;
		this.isRightAssociative = isRightAssociative;
	}
	public  final String opname;
	private final int _prec;
	public final boolean isRightAssociative;
	public int precedence() {
		if(_prec < 0){
			throw new UnsupportedOperationException();
		}
		return _prec;
	}
}
