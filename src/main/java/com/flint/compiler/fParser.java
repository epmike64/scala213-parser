package com.flint.compiler;

import com.flint.compiler.parse.ProdArgs;
import com.flint.compiler.token.OpChar;
import com.flint.compiler.token.fOperatorKind;
import com.flint.compiler.token.fOperatorMap;
import com.flint.compiler.token.fTokenKind;
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

	public fParser(fLexer lexer) {
		this.lexer = lexer;
		next();
	}

	void next() {
		prevToken = token;
		token = lexer.nextToken();
		System.out.println("Token: " + token);
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
				// ROOT means after  "case"
				// ID_LEAF "x"
				prodFirstIdLeaf(a, T_ID, T_LPAREN, T_RPAREN, T_SEMI, T_NL);
				a.isContinue = true;
				return;
			}
			case N_ID_OPERATOR: {
				// SimplePatternA {OP SimplePatternB }
				// SimplePatternB

			}
			case N_ID_LEAF: {
				// SimplePattern {OP SimplePattern}
				a.lastOpN = insertOpNode(a.lastOpN, token);
				if (isColonOpT(0)) {
					// Id : Type
					a.prevNKind = fTreeNKind.N_ID_LEAF;
					next();
					a.lastOpN.setRight(typeProd());
					expectOneOf(0, T_ID, T_SEMI, T_NL);
					//Pattern1 = id: type { | Pattern1 }
					a.isContinue = false;

				} else {
					a.prevNKind = fTreeNKind.N_ID_OPERATOR;
					next();
					expectOneOf(0, T_ID, T_SEMI, T_NL);
					//Pattern3 = SimplePattern (id SimplePattern)*
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
				// ROOT means after  ":"
				// ID_LEAF
				// "x"
				a.prevNKind = fTreeNKind.N_ID_LEAF;
				assert a.lastOpN.NKind() == fTreeNKind.N_ROOT;
				a.lastOpN.setRight(new IdLeafNode(a.lastOpN, (NamedToken) token));
				next();
				expectOneOf(0, T_COMMA, T_ID, T_LBRACKET, T_LPAREN, T_RPAREN, T_FAT_ARROW, T_SEMI, T_NL);
				a.isContinue = true;
				return;
			}
			case N_ID_OPERATOR: {
				//  ID_LEAF after COMMA
				// ", x"
				a.prevNKind = fTreeNKind.N_ID_LEAF;
				assert a.lastOpN.right() == null;
				IdLeafNode rightLeaf = new IdLeafNode(a.lastOpN, (NamedToken) token);
				a.lastOpN.setRight(rightLeaf);
				next();
				expectOneOf(0, T_COMMA, T_ID, T_RBRACKET, T_LPAREN, T_RPAREN, T_FAT_ARROW, T_ELSE, T_SEMI, T_NL);
				a.isContinue = true;
				return;
			}
			default:
				throw new RuntimeException("Unexpected Previous  NodeKind: " + a.prevNKind);
		}
	}

	private void prodFirstIdLeaf(ProdArgs a, fTokenKind... expectTypes) {/*
			assert a.lastOpN.NKind() == fTreeNKind.N_ROOT;
			a.prevNKind = fTreeNKind.N_ID_LEAF;
			a.lastOpN.setRight(new IdLeafNode(a.lastOpN, (NamedToken) token));
			next();
			expectOneOf(0, T_COMMA, T_ID, T_LPAREN, T_RPAREN, T_ELSE, T_FAT_ARROW, T_SEMI, T_NL);
	*/
		// Prev NodeKind is ROOT
		// ID_LEAF  "x"
		assert a.lastOpN.NKind() == fTreeNKind.N_ROOT;
		a.prevNKind = fTreeNKind.N_ID_LEAF;
		a.lastOpN.setRight(new IdLeafNode(a.lastOpN, (NamedToken) token));
		next();
		expectOneOf(0, expectTypes);
	}

	private void prodRightIdLeaf(ProdArgs a, fTokenKind... expectTypes) {/*
		a.prevNKind = fTreeNKind.N_ID_LEAF;
				assert a.lastOpN.right() == null;
				IdLeafNode rightLeaf = new IdLeafNode(a.lastOpN, (NamedToken) token);
				a.lastOpN.setRight(rightLeaf);
				next();
				expectOneOf(0, T_COMMA, T_ID, T_LPAREN, T_RPAREN, T_FAT_ARROW, T_ELSE, T_SEMI, T_NL);
				a.isContinue = true;
				return;
		*/
		a.prevNKind = fTreeNKind.N_ID_LEAF;
		assert a.lastOpN.right() == null;
		a.lastOpN.setRight(new IdLeafNode(a.lastOpN, (NamedToken) token));
		next();
		expectOneOf(0, expectTypes);
	}

	private void prodIdOp(ProdArgs a) {
		a.prevNKind = fTreeNKind.N_ID_OPERATOR;
		a.lastOpN = insertOpNode(a.lastOpN, token);
	}

	private void expressionTID(ProdArgs a) {
		switch (a.prevNKind) {
			case N_ROOT: {
				// ID_LEAF="x"
				prodFirstIdLeaf(a,  T_COMMA, T_ID, T_LPAREN, T_RPAREN, T_ELSE, T_FAT_ARROW, T_SEMI, T_NL);
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
				prodIdOp(a);

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

	private void pattern1IFGuard(ProdArgs a) {
		switch (getPrevNKind(a, fTreeNKind.N_IF_KW_LEAF)) {
			case N_ID_LEAF: {
				// IF Case Pattern GUARD
				IfKwPatternGuardLeafNode ifLeafNode = new IfKwPatternGuardLeafNode(a.lastOpN, token);
				assert a.lastOpN.right() == null;
				a.lastOpN.setRight(ifLeafNode);
				next();
				ifLeafNode.val().ifCondLeafN = postfixExprProd();
				accept(T_MATCH);
				accept(T_LCURL);
				ifLeafNode.val().ifBodyLeafN = caseClassesProd();
				accept(T_RCURL);
			}
			default:
				throw new RuntimeException("Unexpected token: " + token.kind);
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
				typeWithTypeArgs.val().typeArgs = types(null);
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
				funCallLeaf.val().funcArgs = exprs(null);
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

	private ProdRootLeafN blockStatProd() {
		return blockStatProd(null, null, null);
	}

	private ProdRootLeafN blockStatProd(ProdRootLeafN wrapSubExpr, fTreeNKind prevNKind, fToken opToken) {
		return commonProd(ProdRootOp.BLOCK_STAT_PRD, wrapSubExpr, prevNKind, opToken);
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

	private ProdRootLeafN pattern1Prod() {
		return pattern1Prod(null, null, null);
	}

	private ProdRootLeafN pattern1Prod(ProdRootLeafN wrapSubExpr, fTreeNKind prevNKind, fToken opToken) {
		return commonProd(ProdRootOp.PATTERN1_PRD, wrapSubExpr, prevNKind, opToken);
	}

	void blockStatProdLoop(ProdArgs a) {
		throw new RuntimeException("Not implemented");
	}

	void blockProdLoop(ProdArgs a) {
		throw new RuntimeException("Not implemented");
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

	void pattern1ProdLoop(ProdArgs a) {
		loop:
		while (true) {
			a.isContinue = false;
			switch (token.kind) {
				case T_ID: {
					pattern1TID(a);
					if(a.isContinue) continue;
					break loop;
				}
				case T_LPAREN: {
//					pattern1IFGuard(a);
//					assert a.isContinue == true;
//					continue;
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
		accept(T_CASE);
		a.lastOpN.setRight(pattern());
		if (token.kind == T_IF) {
			pattern1IFGuard(a);
		}
		assert token.kind == T_FAT_ARROW;
		a.lastOpN = insertOpNode(a.lastOpN, token);
		next();
		assert a.lastOpN.right() == null;
		a.lastOpN.setRight(blockProd());
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
				assert opToken.kind == T_COMMA;
				a.lastOpN = new OperatorNode(a.lastOpN, wrapSubExpr, null, new OperatorValue(fOperatorKind.O_COMMA, opToken));
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
				blockProdLoop(a);
				break;
			case BLOCK_STAT_PRD:
				blockStatProdLoop(a);
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

		return new ProdRootLeafN(null, a.lastOpN);
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

	public CommonOpNode compilationUnit() {
		RootOpN root = new RootOpN(ProdRootOp.COMP_UNIT_PRD);
//		root.setRight(expressionProd());
//		root.setRight(type());
		root.setRight(pattern1Prod());
		return root;
	}
}
