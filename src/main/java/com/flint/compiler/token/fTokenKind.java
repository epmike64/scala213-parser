package com.flint.compiler.token;


public enum fTokenKind {
	T_EOF(),
	T_ERROR(),
	T_NL(),
	T_ID(fTokenTag.NAMED),
	T_INT_LIT(fTokenTag.NUMERIC),
	T_LNG_LIT(fTokenTag.NUMERIC),
	T_FLOAT_LIT(fTokenTag.NUMERIC),
	T_DBLE_LIT(fTokenTag.NUMERIC),
	T_CHR_LIT(fTokenTag.NUMERIC),
	T_STR_LIT(fTokenTag.STRING),
	T_BOOL_LIT(fTokenTag.NAMED),

	T_ABSTRACT("abstract"),
	T_CASE("case"),
	T_CATCH("catch"),
	T_CLASS("class"),
	T_DEF("def"),
	T_DO("do"),
	T_ELSE("else"),
	T_EXTENDS("extends"),
	T_FALSE("false"),
	T_FINALLY("finally"),
	T_FINAL("final"),
	T_FOR("for"),
	T_IF("if"),
	T_IMPLICIT("implicit"),
	T_IMPORT("import"),
	T_LAZY("lazy"),
	T_MATCH("match"),
	T_NEW("new"),
	T_NULL("null"),
	T_OBJECT("object"),
	T_OVERRIDE("override"),
	T_PACKAGE("package"),
	T_PRIVATE("private"),
	T_PROTECTED("protected"),
	T_RETURN("return"),
	T_SEALED("sealed"),
	T_SUPER("super"),
	T_THIS("this"),
	T_THROW("throw"),
	T_TRAIT("trait"),
	T_TRUE("true"),
	T_TRY("try"),
	T_TYPE("type"),
	T_VAL("val"),
	T_VAR("var"),
	T_T_WHILE("while"),
	T_T_WITH("with"),
	T_T_YIELD("yield"),

	T_LCURL("{"),
	T_RCURL("}"),
	T_LPAREN("("),
	T_RPAREN(")"),
	T_LBRACKET("["),
	T_RBRACKET("]"),
	T_SEMI(";"),
	T_COMMA(","),
	T_DOT("."),
	T_FAT_ARROW("=>"),
	T_LEFT_ARROW("<-"),
	T_UPPER_BOUND("<:"),
	T_LESS_PERCENT("<%"),
	T_LOWER_BOUND(">:");


	fTokenKind() {
		this(null, fTokenTag.DEFAULT);
	}

	fTokenKind(String name) {
		this(name, fTokenTag.DEFAULT);
	}

	fTokenKind(fTokenTag tag) {
		this(null, tag);
	}

	fTokenKind(String name, fTokenTag tag) {
		this.tkName = name;
		this.tag = tag;
	}

	public final String tkName;
	public final fTokenTag tag;
}
