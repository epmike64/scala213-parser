package com.flint.compiler;

import com.flint.compiler.parse.ProdArgs;
import com.flint.compiler.token.*;
import com.flint.compiler.token.type.NamedToken;
import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree.*;
import com.flint.compiler.tree.leaves.nodes.*;
import com.flint.compiler.tree.operators.nodes.*;
import com.flint.compiler.tree.operators.values.OperatorValue;
import static com.flint.compiler.token.fTokenKind.*;

public class fParser {
	private fToken prevToken;
	private fToken token;
	private fLexer lexer;
	private static final fToken Semicolon = new NamedToken(T_SEMI, -1, -1, ";", OpChar.INVALID);

	public fParser(fLexer lexer) {
		this.lexer = lexer;
		next();
	}

	fToken next() {
		prevToken = token;
		token = lexer.nextToken();
		System.out.println("Token: " + token);
		return prevToken;
	}

	private void acceptOpChar(OpChar opChar) {
		if (token.opChar() == opChar) {
			next();
		} else {
			throw new AssertionError("Expected " + opChar + " but found " + token.opChar());
		}
	}

	private void accept(fTokenKind kind) {
		if (token.kind != kind) {
			throw new AssertionError("Expected " + kind + " but found " + token.kind);
		}
		next();
	}

	fToken lookAhead(int n) {
		return lexer.lookAhead(n);
	}

	boolean isLa(int la, fTokenKind... types) {
		fToken laToken = lookAhead(la);
		for (fTokenKind type : types) {
			if (laToken.kind == type) {
				return true;
			}
		}
		return false;
	}

	OpChar laOpChar(int n) {
		fToken laTok = lookAhead(n);
		if (laTok.kind == T_ID && laTok.name().length() == 1) {
			char ch = laTok.name().charAt(0);
			switch (ch) {
				case '+':
					return OpChar.PLUS;
				case '-':
					return OpChar.MINUS;
				case '*':
					return OpChar.STAR;
				case '/':
					return OpChar.FORWARD_SLASH;
				case '\\':
					return OpChar.BACKSLASH;
				case '%':
					return OpChar.PERCENT;
				case '&':
					return OpChar.AMPERSAND;
				case '|':
					return OpChar.PIPE;
				case '^':
					return OpChar.CARET;
				case '<':
					return OpChar.LT;
				case '>':
					return OpChar.GT;
				case '=':
					return OpChar.ASSIGN;
				case '!':
					return OpChar.BANG;
				case '~':
					return OpChar.TILDE;
				case '?':
					return OpChar.QUESTION;
				case ':':
					return OpChar.COLON;
				case '@':
					return OpChar.AT;
				case '#':
					return OpChar.POUND;
				default:
					throw new AssertionError("Expected operator but found " + token.kind);
			}
		}
		throw new AssertionError("Expected operator but found " + token.kind);
	}

	private boolean isLaOpChar(int la, fTokenKind tk, OpChar... ops) {
		fToken laTok = lookAhead(la);
		if (laTok.kind == tk && laTok.name().length() == 1) {
			char ch = laTok.name().charAt(0);
			for (OpChar op : ops) {
				if (ch == op.opchar) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isColonOpT(int la) {
		return isLaOpChar(la, T_ID, OpChar.COLON);
	}

	private boolean isAssignOpT(int la) {
		return isLaOpChar(la, T_ID, OpChar.ASSIGN);
	}

	private boolean isPipeOpT(int la) {
		return isLaOpChar(la, T_ID, OpChar.PIPE);
	}

	fOperatorKind getOperatorKind(fToken token) {
		return fOperatorMap.getOperatorKind(token);
	}

	int skipLPar() {
		int count = 0;
		while (token.kind == fTokenKind.T_LPAREN) {
			count++;
			next();
		}
		return count;
	}

	void skipSemi() {
		while (token.kind == fTokenKind.T_SEMI) {
			next();
		}
	}

	int skipRPar() {
		int count = 0;
		while (token.kind == fTokenKind.T_RPAREN) {
			count++;
			next();
		}
		return count;
	}

	void expectOneOf(int la, fTokenKind... types) {
		fToken _laToken = lookAhead(la);
		for (fTokenKind type : types) {
			if (_laToken.kind == type) {
				return;
			}
		}
		throw new RuntimeException("Unexpected token: " + token.kind);
	}

	fTreeNKind getPrevNKind(ProdArgs a, fTreeNKind replace) {
		fTreeNKind temp = a.prevNKind;
		a.prevNKind = replace;
		return temp;
	}

	private void pattern1TID(ProdArgs a) {
		switch (a.prevNKind) {
			case N_ROOT: {
				// ROOT="case x"
				prodFirstIdLeaf(a, T_ID, T_LPAREN, T_RPAREN, T_SEMI, T_NL);
				a.isContinue = true;
				return;
			}
			case N_ID_OPERATOR: {
				// SimplePatternA {OP SimplePatternB }
				// x="SimplePatternB"
				prodRightIdLeaf(a, T_ID, T_LPAREN, T_RPAREN, T_FAT_ARROW, T_SEMI, T_NL);
				a.isContinue = true;
				return;
			}
			case N_ID_LEAF: {
				// SimplePattern {OP SimplePattern}
				// x="OP"
				a.lastOpN = insertOpNode(a.lastOpN, token);
				if (isColonOpT(0)) {
					// x=": Type"
					a.prevNKind = fTreeNKind.N_ID_LEAF;
					next();
					a.lastOpN.setRight(typeProd());
					expectOneOf(0, T_ID, T_SEMI, T_NL);
					//Pattern1 = "x : type { | Pattern1 }"
					a.isContinue = false;

				} else {
					a.prevNKind = fTreeNKind.N_ID_OPERATOR;
					next();
					expectOneOf(0, T_ID, T_FAT_ARROW, T_SEMI, T_NL);
					// SimplePatternA {OP SimplePatternB }
					// expect "SimplePatternB",
					a.isContinue = true;
				}
				return;
			}
			default:
				throw new RuntimeException("Unexpected Previous  NodeKind: " + a.prevNKind);
		}
	}

	private void typeTID(ProdArgs a) {
		switch (a.prevNKind) {
			case N_ROOT: {
				// ROOT=": x"
				prodFirstIdLeaf(a, T_COMMA, T_ID, T_LBRACKET, T_LPAREN, T_RPAREN, T_FAT_ARROW, T_SEMI, T_NL);
				a.isContinue = true;
				return;
			}
//			case N_ID_OPERATOR: {
//				//  ID_LEAF after COMMA
//				// ", x"
//				prodRightIdLeaf(a, T_COMMA, T_ID, T_RBRACKET, T_LPAREN, T_RPAREN, T_FAT_ARROW, T_ELSE, T_SEMI, T_NL);
//				a.isContinue = true;
//				return;
//			}
			default:
				throw new RuntimeException("Unexpected Previous  NodeKind: " + a.prevNKind);
		}
	}

	private void prodFirstIdLeaf(ProdArgs a, fTokenKind... expectTypes) {
		assert a.lastOpN.NKind() == fTreeNKind.N_ROOT;
		a.prevNKind = fTreeNKind.N_ID_LEAF;
		a.lastOpN.setRight(new IdLeafNode(a.lastOpN, (NamedToken) token));
		next();
		expectOneOf(0, expectTypes);
	}

	private void prodFirstCommonLeaf(ProdArgs a, CommonLeafNode leafNode, boolean isNext, int la, fTokenKind... expectTypes) {
		assert a.lastOpN.NKind() == fTreeNKind.N_ROOT;
		a.prevNKind = fTreeNKind.N_ID_LEAF;
		a.lastOpN.setRight(leafNode);
		if (isNext) {
			next();
		}
		if (expectTypes != null) {
			expectOneOf(la, expectTypes);
		}
	}

	private void prodRightCommonLeaf(ProdArgs a, CommonLeafNode leafNode, boolean isNext, int la, fTokenKind... expectTypes) {
		assert a.lastOpN.right() == null;
		a.prevNKind = fTreeNKind.N_ID_LEAF;
		a.lastOpN.setRight(leafNode);
		if (isNext) {
			next();
		}
		if (expectTypes != null) {
			expectOneOf(la, expectTypes);
		}
	}


	private void prodRightIdLeaf(ProdArgs a, fTokenKind... expectTypes) {
		assert a.lastOpN.right() == null;
		a.prevNKind = fTreeNKind.N_ID_LEAF;
		a.lastOpN.setRight(new IdLeafNode(a.lastOpN, (NamedToken) token));
		next();
		expectOneOf(0, expectTypes);
	}

	private void prodLocalRootIdOp(ProdArgs a) {
		a.prevNKind = fTreeNKind.N_ID_OPERATOR;
		a.lastOpN = insertOpNode(a.lastOpN, token);
	}

	private void expressionTID(ProdArgs a) {
		switch (a.prevNKind) {
			case N_ROOT: {
				// ID_LEAF="x"
				prodFirstIdLeaf(a, T_COMMA, T_ID, T_LPAREN, T_RPAREN, T_ELSE, T_FAT_ARROW, T_SEMI, T_NL);
				a.isContinue = true;
				return;
			}

			case N_ID_OPERATOR: {
				//  ID_LEAF=x after ( ID_OPERATOR, COMMA )
				// "+ x" or ", x"
				prodRightIdLeaf(a, T_COMMA, T_ID, T_LPAREN, T_RPAREN, T_FAT_ARROW, T_ELSE, T_SEMI, T_NL);
				a.isContinue = true;
				return;
			}

			case N_ID_LEAF:
			case N_FUN_CALL_LEAF: {
				// ID_OPERATOR after ( LEAF, FUN_CALL_LEAF )
				// "x +", "x = ", "x : ", "func(x) + ", "func(x) = ", "func(x) : "
				prodLocalRootIdOp(a);

				if (isColonOpT(0)) {
					next();
					// ID_OPERATOR is COLON
					// " x : 'type' "
					// Parse 'Type' Prod
					a.lastOpN.setRight(typeProd());
					expectOneOf(0, T_ID, T_SEMI, T_NL);
					a.isContinue = true;
					return;

				} else if (isAssignOpT(0)) {
					next();
					// ID_OPERATOR is ASSIGN
					// " x = 'expression' "
					// " func(x) = 'expression' "
					// Parse 'Expression' Prod
					a.lastOpN.setRight(expressionProd());
					expectOneOf(0, T_SEMI, T_NL);
					a.isContinue = true;
					return;

				} else {
					next();
					// ID_OPERATOR is MATH, FUNC_CALL
					// "x +", "func(x) + "
					expectOneOf(0, T_ID, T_IF, T_LPAREN, T_NL);
					a.isContinue = true;
					return;
				}
			}
			default:
				throw new RuntimeException("Unexpected Previous  NodeKind: " + a.prevNKind);
		}
	}

	private void expressionIF(ProdArgs a) {
		switch (getPrevNKind(a, fTreeNKind.N_IF_KW_LEAF)) {
			case N_ROOT:
			case N_ID_OPERATOR: {
				IfKwLeafNode ifLeafNode = new IfKwLeafNode(a.lastOpN, token);
				assert a.lastOpN.right() == null;
				a.lastOpN.setRight(ifLeafNode);
				next();
				accept(T_LPAREN);
				ifLeafNode.val().ifCondLeafN = expressionProd();
				accept(T_RPAREN);
				ifLeafNode.val().ifBodyLeafN = expressionProd();
				if (token.kind == T_ELSE) {
					ElseKwLeafNode elseLeafNode = new ElseKwLeafNode(a.lastOpN, token);
					next();
					a.lastOpN.setRight(elseLeafNode);
					elseLeafNode.val().ifLeafN = ifLeafNode;
					elseLeafNode.val().elseLeafN = expressionProd();
				}
				a.isContinue = true;
				return;
			}
			default:
				throw new RuntimeException("Unexpected token: " + token.kind);
		}
	}

	private ProdRootLeafN pattern() {
		ProdRootLeafN rootLeaf = pattern1Prod();
		while (isPipeOpT(0)) {
			next();
			rootLeaf = pattern1Prod(rootLeaf, fTreeNKind.N_ID_OPERATOR, prevToken);
		}
		return rootLeaf;
	}

	private ProdRootLeafN pattern2() {
		throw new RuntimeException("Not implemented");
	}

	private ProdRootLeafN pattern2s() {
		ProdRootLeafN rootLeaf = pattern2();
		while (token.kind == T_COMMA) {
			next();
			//rootLeaf = pattern2(rootLeaf, fTreeNKind.N_ID_OPERATOR, prevToken);
		}
		return rootLeaf;
	}

	private ProdRootLeafN exprs(ProdRootLeafN rootLeaf) {
		rootLeaf = expressionProd(rootLeaf, fTreeNKind.N_ID_LEAF, null);
		while (token.kind == T_COMMA) {
			next();
			rootLeaf = expressionProd(rootLeaf, fTreeNKind.N_ID_OPERATOR, prevToken);
		}
		return rootLeaf;
	}

	private ProdRootLeafN types(ProdRootLeafN rootLeaf) {
		rootLeaf = typeProd(rootLeaf, fTreeNKind.N_ID_LEAF, null);
		while (token.kind == T_COMMA) {
			next();
			rootLeaf = typeProd(rootLeaf, fTreeNKind.N_ID_OPERATOR, prevToken);
		}
		return rootLeaf;
	}

	private void typeLBracket(ProdArgs a) {
		switch (a.prevNKind) {
			case N_ID_LEAF: {
				// TypeArgs: "x[type, type]"
				TypeLeafNode typeWithTypeArgs = new TypeLeafNode(a.lastOpN, (NamedToken) prevToken);
				a.lastOpN.setRight(typeWithTypeArgs);
				accept(T_LBRACKET);
				typeWithTypeArgs.val().typeArgsLeafN = types(null);
				accept(T_RBRACKET);
				a.prevNKind = fTreeNKind.N_TYPE_LEAF;
				expectOneOf(0, T_ID, T_COMMA, T_RPAREN, T_FAT_ARROW, T_SEMI, T_NL); //RPAREN: func()()
				a.isContinue = true;
				return;
			}
		}
	}

	private void typeLParen(ProdArgs a) {
		switch (a.prevNKind) {
			case N_ROOT: {
				// ROOT means after  ":"
				// LPAREN after COLON
				// "x: (((type1, (type2, type3)), type4))"
				int lpar = skipLPar();
				ProdRootLeafN subExpr = null;
				loop:
				while (lpar > 0) {
					subExpr = types(subExpr);
					switch (token.kind) {
						case T_RPAREN: {
							do {
								lpar--;
								next();
							} while (lpar > 0 && token.kind == T_RPAREN);
							continue loop;
						}
						default:
							throw new RuntimeException("Unmatched Left Paren. Right parent expected. But, got: " + token);
					}
				}
				assert a.lastOpN.right() == null;
				a.lastOpN.setRight(subExpr);
				a.prevNKind = fTreeNKind.N_ID_LEAF;
				expectOneOf(0, T_ID, T_COMMA, T_FAT_ARROW, T_RPAREN, T_SEMI, T_NL);
				a.isContinue = true;
				return;
			}

			default:
				throw new RuntimeException("Unexpected token: " + token.kind);
		}
	}

	private void expressionLParen(ProdArgs a) {

		switch (a.prevNKind) {
			case N_ID_LEAF: {
				//LPAREN after ID_LEAF -> FUNC CALL: "func("
				FunCallLeafNode funCallLeaf = new FunCallLeafNode(a.lastOpN, (NamedToken) prevToken);
				a.lastOpN.setRight(funCallLeaf);
				accept(T_LPAREN);
				funCallLeaf.val().funcArgsLeafN = exprs(null);
				accept(T_RPAREN);
				a.prevNKind = fTreeNKind.N_FUN_CALL_LEAF;
				expectOneOf(0, T_ID, T_COMMA, T_FAT_ARROW, T_SEMI, T_NL); //RPAREN: func()()
				a.isContinue = true;
				return;
			}
			case N_ROOT:
			case N_ID_OPERATOR: {
				int lpar = skipLPar();
				ProdRootLeafN subExpr = null;
				loop:
				while (lpar > 0) {
					//Paren wrapped (also, might be comma separated) sub-expression
					subExpr = exprs(subExpr);
					switch (token.kind) {
						case T_RPAREN: {
							do {
								lpar--;
								next();
							} while (lpar > 0 && token.kind == T_RPAREN);
							continue loop;
						}
						default:
							throw new RuntimeException("Unmatched Left Paren. Right parent expected. But, got: " + token);
					}
				}
				assert a.lastOpN.right() == null;
				a.lastOpN.setRight(subExpr);
				a.prevNKind = fTreeNKind.N_ID_LEAF;
				expectOneOf(0, T_ID, T_COMMA, T_IF, T_ELSE, T_FAT_ARROW, T_RPAREN, T_SEMI, T_NL);
				a.isContinue = true;
				return;
			}
			default:
				throw new RuntimeException("Unexpected token: " + token.kind);
		}
	}

	private void typeFatArrow(ProdArgs a) {
		switch (getPrevNKind(a, fTreeNKind.N_FAT_ARROW)) {
			case N_ROOT:
			case N_ID_OPERATOR:
			case N_ID_LEAF: {
				// FAT_ARROW after (ROOT ':') or (ID_OPERATOR ',') or (ID_LEAF funArgTypes)
				// "x: => type"
				// "type, => type"
				//  "funArgTypes => type"
				a.lastOpN = insertOpNode(a.lastOpN, token);
				next();
				assert a.lastOpN.right() == null;
				a.lastOpN.setRight(typeProd());
				expectOneOf(0, T_COMMA, T_RPAREN, T_RBRACKET, T_SEMI, T_NL);
				return;
			}
			default:
				throw new RuntimeException("Unexpected token: " + token.kind);
		}
	}

	private void expressionFatArrow(ProdArgs a) {
		switch (getPrevNKind(a, fTreeNKind.N_FAT_ARROW)) {
			case N_ID_LEAF: {
				a.lastOpN = insertOpNode(a.lastOpN, token);
				assert a.lastOpN.right() == null;
				next();
				a.lastOpN.setRight(expressionProd());
				//break loop;
				return;
			}
			default:
				throw new RuntimeException("Unexpected token: " + token.kind);
		}
	}

	private void postfixExprNew(ProdArgs a) {
		accept(T_NEW);
	}


	private void postfixExprLCurl(ProdArgs a) {
		accept(T_LCURL);
		if (token.kind != T_RCURL) {
			switch (a.prevNKind) {
				case N_ROOT: {
					switch (token.kind) {
						case T_CASE: {
							a.lastOpN.setRight(caseClassesProd());
							break;
						}
						default: {
							a.lastOpN.setRight(blockProd());
						}
					}
				}
				default:
					throw new RuntimeException("Unexpected prev NodeKind: " + a.prevNKind);
			}
		}
		accept(T_RCURL);
	}


	private ProdRootLeafN postfixExprProd() {
		return postfixExprProd(null, null, null);
	}

	private ProdRootLeafN expressionProd() {
		return expressionProd(null, null, null);
	}

	private ProdRootLeafN typeProd() {
		return typeProd(null, null, null);
	}

	private ProdRootLeafN caseClassesProd() {
		return caseClassesProd(null, null, null);
	}

	private ProdRootLeafN blockProd() {
		return blockProd(null, null, null);
	}

	private ProdRootLeafN templateBodyProd() {
		return templateBody(null, null, null);
	}

	private ProdRootLeafN typeProd(ProdRootLeafN wrapSubExpr, fTreeNKind prevNKind, fToken opToken) {
		return commonProd(ProdRootOp.TYPE_PRD, wrapSubExpr, prevNKind, opToken);
	}

	private ProdRootLeafN postfixExprProd(ProdRootLeafN wrapSubExpr, fTreeNKind prevNKind, fToken opToken) {
		return commonProd(ProdRootOp.POSTFIX_EXPR_PRD, wrapSubExpr, prevNKind, opToken);
	}

	private ProdRootLeafN expressionProd(ProdRootLeafN wrapSubExpr, fTreeNKind prevNKind, fToken opToken) {
		return commonProd(ProdRootOp.EXPR_PRD, wrapSubExpr, prevNKind, opToken);
	}

	private ProdRootLeafN caseClassesProd(ProdRootLeafN wrapSubExpr, fTreeNKind prevNKind, fToken opToken) {
		return commonProd(ProdRootOp.CASE_CLASSES_PRD, wrapSubExpr, prevNKind, opToken);
	}

	private ProdRootLeafN blockProd(ProdRootLeafN wrapSubExpr, fTreeNKind prevNKind, fToken opToken) {
		return commonProd(ProdRootOp.BLOCK_PRD, wrapSubExpr, prevNKind, opToken);
	}

	private ProdRootLeafN templateBody(ProdRootLeafN wrapSubExpr, fTreeNKind prevNKind, fToken opToken) {
		return commonProd(ProdRootOp.TEMPLATE_BODY_PRD, wrapSubExpr, prevNKind, opToken);
	}

	private ProdRootLeafN pattern1Prod() {
		return pattern1Prod(null, null, null);
	}

	private ProdRootLeafN pattern1Prod(ProdRootLeafN wrapSubExpr, fTreeNKind prevNKind, fToken opToken) {
		return commonProd(ProdRootOp.PATTERN1_PRD, wrapSubExpr, prevNKind, opToken);
	}

	void varDef(ProdArgs a) {
		accept(T_VAR);
		patDef(a);
	}

	void patDef(ProdArgs a) {

		PatDefLeafNode leafNode = new PatDefLeafNode(a.lastOpN, token);
		setRightLeafProlog(a, T_VAL, leafNode);

		leafNode.val().pattern2sLeafN = pattern2s();
		if (isColonOpT(0)) {
			next();
			leafNode.val().typeLeafN = typeProd();
		}
		acceptOpChar(OpChar.ASSIGN);
		leafNode.val().exprLeafN = expressionProd();
		switch (getPrevNKind(a, fTreeNKind.N_ID_LEAF)) {
			case N_ROOT: {
				// ROOT="val x"
				prodFirstCommonLeaf(a, leafNode, false, 0, T_ID, T_SEMI, T_NL);
				break;
			}
			case N_ID_OPERATOR: {
				prodRightCommonLeaf(a, leafNode, false, 0, T_ID, T_SEMI, T_NL);
				break;
			}
			default:
				throw new RuntimeException("Unexpected Previous  NodeKind: " + a.prevNKind);
		}
	}

	ProdRootLeafN typeParams() {
		ProdArgs a = initRootNodeProlog(ProdRootOp.TYPE_PARAM_PRD);
		setRightLeaf(a, typeParam());
		while (token.kind == T_COMMA) {
			insertCommaOp(a, next());
			setRightLeaf(a, typeParam());
		}
		return prodRootLeafN(a);
	}

	ProdRootLeafN variantTypeParams() {

		ProdArgs a = initRootNodeProlog(ProdRootOp.TYPE_PARAM_PRD);
		setRightLeaf(a, variantTypeParam());

		while (token.kind == T_COMMA) {
			insertCommaOp(a, next());
			setRightLeaf(a, variantTypeParam());
		}
		return prodRootLeafN(a);
	}

	ProdRootLeafN variantTypeParam() {
		ProdArgs a = initRootNodeProlog(ProdRootOp.TYPE_PARAM_PRD);
		VariantTypeParamLeafNode leafNode = new VariantTypeParamLeafNode(a.lastOpN, token);
		setRightLeaf(a, leafNode);

		if (token.kind == T_ID) {
			switch (token.opChar()) {
				case MINUS:
				case PLUS:
					leafNode.val().variant = token.opChar();
					accept(T_ID);
					break;
				case INVALID:
					break;
				default:
					throw new RuntimeException("Unexpected token: " + token.kind);
			}
		}
		leafNode.val().typeParamLeafN = typeParam();
		return prodRootLeafN(a);
	}

	ProdRootLeafN typeParam() {
		ProdArgs a = initRootNodeProlog(ProdRootOp.TYPE_PARAM_PRD);
		TypeParamLeafNode leafNode = new TypeParamLeafNode(a.lastOpN, token);
		setRightLeaf(a, leafNode);

		accept(T_ID);
		leafNode.val().typeParamName = prevToken.name();
		if (token.kind == T_LBRACKET) {
			accept(T_LBRACKET);
			leafNode.val().variantTypeParamsLeafN = variantTypeParams();
			accept(T_RBRACKET);
		}
		if (token.kind == T_LOWER_BOUND) {
			next();
			leafNode.val().lowerBoundLeafN = typeProd();
		}
		if (token.kind == T_UPPER_BOUND) {
			next();
			leafNode.val().upperBoundLeafN = typeProd();
		}
		if (token.kind == T_LESS_PERCENT) {
			next();
			leafNode.val().lessPercentBoundLeafN = typeProd();
		}
		while (token.kind == T_COMMA) {
			next();
			leafNode.val().endTypeLeafNs.add(typeProd());
		}
		return prodRootLeafN(a);
	}

	ProdRootLeafN params() {
		ProdArgs a = initRootNodeProlog(ProdRootOp.PARAM_PRD);
		param(a);
		while (token.kind == T_COMMA) {
			next();
			insertCommaOp(a, prevToken);
			param(a);
		}
		return prodRootLeafN(a);
	}

	ProdRootLeafN classParams() {
		ProdArgs a = initRootNodeProlog(ProdRootOp.CLASS_PARAMS_PRD);
		classParam(a);
		while (token.kind == T_COMMA) {
			next();
			insertCommaOp(a, prevToken);
			classParam(a);
		}
		return prodRootLeafN(a);
	}


	void param(ProdArgs a) {

		ParamLeafNode leafNode = new ParamLeafNode(a.lastOpN, token);
		setRightLeaf(a, leafNode);
		accept(T_ID);
		leafNode.val().paramName = prevToken.name();
		if (isColonOpT(0)) {
			next();
			leafNode.val().paramTypeLeafN = typeProd();
		}
		if (isAssignOpT(0)) {
			next();
			leafNode.val().exprLeafN = expressionProd();
		}
	}

	void classParam(ProdArgs a) {

		ClassParamLeafNode leafNode = new ClassParamLeafNode(a.lastOpN, token);
		setRightLeaf(a, leafNode);
		//add Modifiers
		switch (token.kind) {
			case T_VAL:
				leafNode.val().storeType = fVariable.StoreType.VAL;
				next();
			case T_VAR:
				leafNode.val().storeType = fVariable.StoreType.VAR;
				next();
				break;
			default:
				break;
		}
		accept(T_ID);
		leafNode.val().paramName = prevToken.name();
		acceptOpChar(OpChar.COLON);
		leafNode.val().paramTypeLeafN = typeProd();
		if(isAssignOpT(0)){
			next();
			leafNode.val().exprLeafN = expressionProd();
		}
	}

	ProdRootLeafN paramClauses() {
		accept(T_LPAREN);
		ProdRootLeafN paramsLeafN = params();
		accept(T_RPAREN);
		return paramsLeafN;
	}

	ProdRootLeafN funSig() {
		ProdArgs a = initRootNodeProlog(ProdRootOp.FUN_SIG_PRD);
		accept(T_ID);
		FunSigLeafNode leafNode = new FunSigLeafNode(a.lastOpN, prevToken);
		a.lastOpN.setRight(leafNode);
		leafNode.val().funName = prevToken.name();
		if (token.kind == T_LBRACKET) {
			accept(T_LBRACKET);
			leafNode.val().typeParamsLeafN = typeParams();
			accept(T_RBRACKET);
		}
		leafNode.val().paramsLeafN = paramClauses();
		return prodRootLeafN(a);
	}

	void funDef(ProdArgs a) {
		FunDclLeafNode leafNode = new FunDclLeafNode(a.lastOpN, token);
		setRightLeafProlog(a, T_DEF, leafNode);

		switch (token.kind){
			case T_ID:
				leafNode.val().funSigLeafN = funSig();
				if(isAssignOpT(0)){
					next();
					leafNode.val().funSigTypeLeafN = typeProd();
					acceptOpChar(OpChar.ASSIGN);
					leafNode.val().funSigExprLeafN =expressionProd();
				} else {
					accept(T_LBRACKET);
					leafNode.val().funSigBlockLeafN = blockProd();
					accept(T_RBRACKET);
				}
				break;
			case T_THIS:
				accept(T_LPAREN);
				leafNode.val().thisParamsLeafN = params();
				accept(T_RPAREN);
				break;
			default:
				throw new RuntimeException("Unexpected token: " + token.kind);
		}
	}

	private void typeDef(ProdArgs a) {
		TypeDefLeafNode leafNode = new TypeDefLeafNode(a.lastOpN, token);
		setRightLeafProlog(a, T_TYPE, leafNode);
	}

	private void traitDef(ProdArgs a) {
		TraitDefLeafNode leafNode = new TraitDefLeafNode(a.lastOpN, token);
		setRightLeafProlog(a, T_TRAIT, leafNode);
		accept(T_ID);
		leafNode.val().traitName = prevToken.name();
		if(token.kind == T_LBRACKET){
			next();
			leafNode.val().variantTypeParamsLeafN = variantTypeParams();
			accept(T_RBRACKET);
		}
		if(token.kind == T_EXTENDS){
			next();
			leafNode.val().extends_ = true;
		}
		if(token.kind == T_ID){
			leafNode.val().traitParentsLeafN = typeProd();
		}
		if(token.kind == T_LCURL) {
			next();
			leafNode.val().templateBodyLeafN = templateBodyProd();
			accept(T_RCURL);
		}
	}

	void accessModifier() {
		ProdArgs a = initRootNodeProlog(ProdRootOp.ACCESS_MODIFIER_PRD);
		ModifierLeafNode leafNode = new ModifierLeafNode(a.lastOpN, token);
		switch (token.kind) {
			case T_PRIVATE:
				next();
				break;
			case T_PROTECTED:
				next();
				break;
			default:
				throw new RuntimeException("Unexpected token: " + token.kind);
		}
	}

	void classDef(ProdArgs a, boolean isCase) {
		ClassDefLeafNode leafNode = new ClassDefLeafNode(a.lastOpN, token);
		setRightLeafProlog(a, T_CLASS, leafNode);

		accept(T_ID);
		leafNode.val().className = prevToken.name();
		leafNode.val().isCase = isCase;
		if(token.kind == T_LBRACKET) {
			next();
			leafNode.val().typeParamsLeafN = variantTypeParams();
			accept(T_RBRACKET);
		}
		leafNode.val().classParamsLeafN = classParams();
		if(token.kind == T_EXTENDS){
			next();
			if(token.kind == T_LBRACKET){
				next();
				leafNode.val().templateBodyLeafN = templateBodyProd();
			}
		}
	}

	void objectDef(ProdArgs a, boolean isCase) {
		ObjectDefLeafNode leafNode = new ObjectDefLeafNode(a.lastOpN, token);
		setRightLeafProlog(a, T_OBJECT, leafNode);
		accept(T_ID);
		leafNode.val().objectName = prevToken.name();
		leafNode.val().isCase = isCase;
	}

	private void templateStatLoop(ProdArgs a){

		loop:
		while (true) {
			switch (token.kind){
				case T_IMPORT:
					importDef(a);
					break;
				case T_VAL:
					varDef(a);
					break;
				case T_VAR:
					varDef(a);
					break;
				case T_DEF:
					funDef(a);
					break;
				case T_TYPE:
					typeDef(a);
					break;
				case T_TRAIT:
					traitDef(a);
					break;
				case T_CLASS:
					classDef(a, false);
					break;
				case T_OBJECT:
					objectDef(a, false);
					break;
				default:
					setRightLeaf(a, expressionProd());
					break;
			}
			insertSemiOp(a);
		}

	}

	void valDcl(ProdArgs a) {
		throw new RuntimeException("Not implemented");
	}

	void varDcl(ProdArgs a) {
		throw new RuntimeException("Not implemented");
	}

	void funDcl(ProdArgs a) {
		throw new RuntimeException("Not implemented");
	}

	void typeDcl(ProdArgs a) {
		throw new RuntimeException("Not implemented");
	}

	void blockStatProdLoop(ProdArgs a, ProdRootOp prodRootOp) {

		assert prodRootOp == ProdRootOp.BLOCK_PRD || prodRootOp == ProdRootOp.TEMPLATE_BODY_PRD;
		boolean p = prodRootOp == ProdRootOp.BLOCK_PRD;
		boolean isCase = false;

		loop:
		while (true) {
			switch (token.kind) {
				case T_SEMI:
					skipSemi();
					continue loop;

				case T_RCURL:
					break loop;

				case T_IMPORT:
					importDef(a);
					break;

				case T_VAL: {
					if(p) patDef(a); else valDcl(a);
				}
				case T_VAR: {
					if(p) varDef(a); else varDcl(a);
					break;
				}

				case T_DEF: {
					if(p) funDef(a); else funDcl(a);
					break;
				}

				case T_TYPE: {
					if(p) typeDef(a); else typeDcl(a);
					break;
				}

				case T_TRAIT:
					traitDef(a);
					break;

				case T_CASE: {
					isCase = true;
					next();
					expectOneOf(0, T_CLASS, T_OBJECT);
					break;
				}

				case T_CLASS: {
					classDef(a, isCase);
					isCase = false;
					break;
				}

				case T_OBJECT: {
					objectDef(a, isCase);
					isCase = false;
					break;
				}

				default:
					setRightLeaf(a, expressionProd());
					break;
			}
			if (!isCase) {
				insertSemiOp(a);
			}
		}
	}

	void blockExprProdLoop(ProdArgs a) {

		switch (token.kind) {
			case T_CASE: {
				a.lastOpN.setRight(caseClassesProd());
				break;
			}
			case T_RCURL: {
				break;
			}
			default: {
				a.lastOpN.setRight(blockProd());
			}
		}
	}

	void pattern1LParen(ProdArgs a) {
		switch (a.prevNKind) {
			case N_ID_LEAF: {
				accept(T_LPAREN);
				assert a.lastOpN.right() == null;
				ConstrPatternLeafNode constrPatternLeafNode = new ConstrPatternLeafNode(a.lastOpN, prevToken);
				constrPatternLeafNode.val().patternLeafN = pattern();
				a.lastOpN.setRight(constrPatternLeafNode);
				accept(T_RPAREN);
				expectOneOf(0, T_ID, T_SEMI, T_NL);
			}
			default:
				throw new RuntimeException("Unexpected token: " + token.kind);
		}
	}

	void pattern1ProdLoop(ProdArgs a) {
		loop:
		while (true) {
			a.isContinue = false;
			switch (token.kind) {
				case T_ID: {
					pattern1TID(a);
					if (a.isContinue) continue;
					break loop;
				}
				case T_LPAREN: {
					pattern1LParen(a);
					assert a.isContinue == true;
					continue;
				}
				default:
					break loop;
			}
		}
	}

	void typeProdLoop(ProdArgs a) {
		loop:
		while (true) {
			a.isContinue = false;
			switch (token.kind) {
				case T_ID: {
					typeTID(a);
					assert a.isContinue == true;
					continue;
				}
				case T_FAT_ARROW: {
					typeFatArrow(a);
					break loop;
				}
				case T_LPAREN: {
					typeLParen(a);
					assert a.isContinue == true;
					continue;
				}
				case T_LBRACKET: {
					typeLBracket(a);
					assert a.isContinue == true;
					continue;
				}
				case T_RBRACKET:
				case T_RPAREN:
				case T_COMMA:
				default:
					break loop;
			}
		}
	}

	void caseClassesCase(ProdArgs a) {

		CaseKwLeafNode leafNode = new CaseKwLeafNode(a.lastOpN, token);
		setRightLeafProlog(a, T_CASE, leafNode);

		leafNode.val().patternLeafN = pattern();
		if (token.kind == T_IF) {
			next();
			leafNode.val().guardLeafN = postfixExprProd();
		}
		accept(T_FAT_ARROW);
		leafNode.val().blockLeafN = blockProd();
		expectOneOf(0, T_CASE, T_RCURL, T_NL, T_SEMI);
	}

	void caseClassesProdLoop(ProdArgs a) {
		loop:
		while (true) {
			a.isContinue = false;
			switch (token.kind) {
				case T_CASE: {
					caseClassesCase(a);
					continue;
				}
				case T_RCURL:
				default:
					break loop;
			}
		}
	}

	void postfixExprProdLoop(ProdArgs a) {

		switch (token.kind) {
			case T_NEW:
				postfixExprNew(a);
				break;
			case T_LCURL:
				postfixExprLCurl(a);
				break;
			default:
				break;
		}

	}

	void expressionProdLoop(ProdArgs a) {
		loop:
		while (true) {
			a.isContinue = false;
			switch (token.kind) {

				case T_ID: {//ID, OPERATOR
					expressionTID(a);
					assert a.isContinue == true;
					continue;
				}

				case T_LPAREN: {
					expressionLParen(a);
					assert a.isContinue == true;
					continue;
				}

				case T_IF: {
					expressionIF(a);
					assert a.isContinue == true;
					continue;
				}

				case T_FAT_ARROW: {
					expressionFatArrow(a);
					break loop;
				}

				case T_NEW:
				case T_LCURL: {
					a.lastOpN.setRight(postfixExprProd());
					break loop;
				}

				case T_SEMI:
				case T_RPAREN:
				case T_COMMA:
				default:
					break loop;
			}
		}
	}


	private ProdRootLeafN commonProd(ProdRootOp prodRootOp, ProdRootLeafN wrapSubExpr, fTreeNKind prevNKind, fToken opToken) {

		ProdArgs a = new ProdArgs();

		if (wrapSubExpr == null) {
			a.lastOpN = new RootOpN(prodRootOp);
			a.prevNKind = fTreeNKind.N_ROOT;

		} else {//wrapped with parenthesis sub-expression
			if (prevNKind == fTreeNKind.N_ID_OPERATOR) {
				// COMMA, PIPE
				fOperatorKind opKind = null;
				switch (opToken.kind) {
					case T_COMMA:
						opKind = fOperatorKind.O_COMMA;
						break;
					case T_ID:
						if (isPipeOpT(0)) {
							opKind = fOperatorKind.O_PIPE;
							break;
						}
					default:
						throw new RuntimeException("Unexpected Operator Token: " + opToken.kind);
				}
				a.lastOpN = new OperatorNode(a.lastOpN, wrapSubExpr, null, new OperatorValue(opKind, opToken));

			} else {
				a.lastOpN = new RootOpN(prodRootOp);
				a.lastOpN.setRight(wrapSubExpr);
			}
			a.prevNKind = prevNKind;
		}

		switch (prodRootOp) {
			case EXPR_PRD:
				expressionProdLoop(a);
				break;
			case POSTFIX_EXPR_PRD:
				postfixExprProdLoop(a);
				break;
			case TYPE_PRD:
				typeProdLoop(a);
				break;
			case BLOCK_EXPR_PRD:
				blockExprProdLoop(a);
				break;
			case CASE_CLASSES_PRD:
				caseClassesProdLoop(a);
				break;

			case BLOCK_PRD:
				blockStatProdLoop(a, ProdRootOp.BLOCK_PRD);
				break;

			case TEMPLATE_BODY_PRD:
				blockStatProdLoop(a, ProdRootOp.TEMPLATE_BODY_PRD);
				break;

			case PATTERN1_PRD:
				pattern1ProdLoop(a);
				break;
			default:
				throw new RuntimeException("Unexpected ProdRootOp: " + prodRootOp);
		}

		while (a.lastOpN.parent() != null) {
			a.lastOpN = a.lastOpN.parent();
		}
		return prodRootLeafN(a);
	}

	CommonOpNode insertOpNode(CommonOpNode lastOpN, fToken op_token) {
		fOperatorKind k = getOperatorKind(op_token);
		if (k.isRightAssociative || k.precedence() > lastOpN.precedence()) {
			lastOpN = insertHigherPrecedenceOpNode(lastOpN, k, op_token);
		} else {
			lastOpN = insertLessOrEqualPrecedenceOpNode(lastOpN, k, op_token);
		}
		return lastOpN;
	}

	CommonOpNode insertLessOrEqualPrecedenceOpNode(CommonOpNode lastOpN, fOperatorKind k, fToken op_token) {
		CommonOpNode lastOpParent = lastOpN.parent();
		OperatorNode lowerEq = new OperatorNode(lastOpParent, lastOpN, null, new OperatorValue(k, op_token));
		if (lastOpParent.left() == lastOpN) {
			lastOpParent.setLeft(lowerEq);
		} else if (lastOpParent.right() == lastOpN) {
			lastOpParent.setRight(lowerEq);
		} else {
			throw new RuntimeException("'LastOpN' parent node not pointing to the 'lastOpN'");
		}
		lastOpN.parent = lowerEq;
		lowerEq.setLeft(lastOpN);
		return lowerEq;
	}

	CommonOpNode insertHigherPrecedenceOpNode(CommonOpNode lastOpN, fOperatorKind k, fToken op_token) {
		CommonOpNode higher = new OperatorNode(lastOpN, lastOpN.right(), null, new OperatorValue(k, op_token));
		lastOpN.setRight(higher);
		return higher;
	}

	private void tmplDef(ProdArgs a){
		boolean isCase = false;
		if(token.kind == T_CASE){
			isCase = true;
			next();
			expectOneOf(0, T_CLASS, T_OBJECT);
		}
		switch (token.kind){
			case T_TRAIT:
				traitDef(a);
				break;
			case T_CLASS:
				classDef(a, isCase);
				break;
			case T_OBJECT:
				objectDef(a, isCase);
				break;
			default:
				throw new RuntimeException("Unexpected token: " + token.kind);
		}
	}

	private void importDef(ProdArgs a){
		ImportDefLeafNode leafNode = new ImportDefLeafNode(a.lastOpN, token);
		setRightLeafProlog(a, T_IMPORT,leafNode);
		accept(T_ID);
		leafNode.val().importPath = prevToken.name();
	}


	private void setRightLeafProlog(ProdArgs a, fTokenKind accKind, CommonLeafNode leafNode) {
		accept(accKind);
		setRightLeaf(a, leafNode);
	}

	private void setRightLeaf(ProdArgs a, CommonLeafNode leafNode) {
		assert a.lastOpN.right() == null;
		a.lastOpN.setRight(leafNode);
		a.prevNKind = fTreeNKind.N_ID_LEAF;
	}

	private void insertSemiOp(ProdArgs a, fToken t) {
		assert t.kind ==  T_SEMI;
		_insertOpNode(a, t);
	}

	private void insertCommaOp(ProdArgs a, fToken t) {
		assert t.kind ==  T_COMMA;
		_insertOpNode(a, t);
	}

	private void _insertOpNode(ProdArgs a, fToken t) {
		a.lastOpN = insertOpNode(a.lastOpN, t);
		a.prevNKind = fTreeNKind.N_ID_OPERATOR;
	}

	private void insertSemiOp(ProdArgs a) {
		a.lastOpN = insertOpNode(a.lastOpN, Semicolon);
		a.prevNKind = fTreeNKind.N_ID_OPERATOR;
	}

	private ProdArgs initRootNodeProlog(ProdRootOp prodRootOp){
		ProdArgs a = new ProdArgs();
		a.lastOpN = new RootOpN(prodRootOp);
		a.prevNKind = fTreeNKind.N_ROOT;
		return a;
	}

	private ProdRootLeafN prodRootLeafN(ProdArgs a){
		return new ProdRootLeafN(null, a.lastOpN);
	}

	public ProdRootLeafN compilationUnit() {
		ProdArgs a = initRootNodeProlog(ProdRootOp.COMP_UNIT_PRD);

		while (token.kind != T_EOF) {
			switch (token.kind) {
				case T_CASE:
				case T_TRAIT:
				case T_CLASS: {
					tmplDef(a);
					break;
				}
				case T_IMPORT: {
					importDef(a);
					break;
				}
			}
			insertSemiOp(a);
		}
		return prodRootLeafN(a);
	}
}
