package com.flint.compiler;

import com.flint.compiler.parse.ProdArgs;
import com.flint.compiler.token.*;
import com.flint.compiler.token.type.NamedToken;
import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree.*;
import com.flint.compiler.tree.leaves.nodes.*;
import com.flint.compiler.tree.leaves.values.GeneratorLeafValue;
import com.flint.compiler.tree.operators.nodes.*;
import com.flint.compiler.tree.operators.values.OperatorValue;


import static com.flint.compiler.token.fTokenKind.*;
import static com.flint.compiler.token.fVariable.DefDcl.*;
import static com.flint.compiler.token.fVariable.StoreType.VAL;
import static com.flint.compiler.token.fVariable.StoreType.VAR;
import static com.flint.compiler.tree.operators.nodes.ProdRootOp.*;

public class fParser {
	private fToken prevToken;
	private fToken token;
	private fLexer lexer;
	private static final fToken Semicolon = new NamedToken(T_SEMI, -1, -1, ";", OpChar.INVALID);
	private static final fToken Comma = new NamedToken(T_COMMA, -1, -1, ";", OpChar.INVALID);

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

	private fToken accept(fTokenKind kind) {
		if (token.kind != kind) {
			throw new AssertionError("Expected " + kind + " but found " + token.kind);
		}
		return next();
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


	private boolean isLaIdOpChar(int la, OpChar... ops) {
		return isLaOpChar(la, T_ID, ops);
	}

	private boolean isLaOpChar(int la, fTokenKind tk, OpChar... ops) {
		fToken laTok = lookAhead(la);
		if(laTok.kind == tk && laTok.opChar() != OpChar.INVALID) {
			for (OpChar op : ops) {
				if (laTok.opChar() == op) {
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
		while (token.kind == T_SEMI || token.kind == T_NL) {
			next();
		}
	}

	void skipNL() {
		while (token.kind == T_NL) {
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
		assert types.length > 0;
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

	private IdTypedLeafNode idTyped(ProdArgs a) {
		IdTypedLeafNode idTypedLeafNode = new IdTypedLeafNode(a.lastOpN, accept(T_ID));
		acceptOpChar(OpChar.COLON);
		idTypedLeafNode.val().typeLeafN = typeProd();
		return idTypedLeafNode;
	}

	private IdAtPattern3LeafNode idAtPattern3(ProdArgs a) {
		IdAtPattern3LeafNode n = new IdAtPattern3LeafNode(a.lastOpN, accept(T_ID));
		acceptOpChar(OpChar.AT);
		n.val().pattern3LeafN = pattern3Prod();
		return n;
	}

	private IdParenWrapPatternsLeafNode idParenWrapPatterns(ProdArgs a) {
		IdParenWrapPatternsLeafNode n = new IdParenWrapPatternsLeafNode(a.lastOpN, accept(T_ID));
		accept(T_LPAREN);
		n.val().patternsLeafN = patterns();
		accept(T_RPAREN);
		return n;
	}


	private void patternTID(ProdArgs a, ProdRootOp patternTyp) {
		assert patternTyp == PATTERN1_PRD || patternTyp == PATTERN2_PRD || patternTyp == PATTERN3_PRD;

		switch (a.prevNKind) {
			case N_ROOT: {
				fTokenKind[] expect = expect2Array(T_ID, T_LPAREN, T_RPAREN, T_FAT_ARROW, T_SEMI, T_NL);
				a.isContinue = false;

				if(patternTyp == PATTERN1_PRD) {
					if(isLaIdOpChar(1, OpChar.COLON)) {
						// x = "SimplePatternA : Type"
						setRootRightLeaf(a, idTyped(a), expect);
						return;
					}
				}

				if(patternTyp == PATTERN1_PRD || patternTyp == PATTERN2_PRD) {
					 if(isLaIdOpChar(1, OpChar.AT)) {
						// x = "SimplePatternA @ Pattern3"
						setRootRightLeaf(a, idAtPattern3(a), expect);
						return;
					}
				}

				a.isContinue = true;
				if(isLa(1, T_LPAREN)) {

					// x="SimplePatternA ( Patterns )"
					setRootRightLeaf(a, idParenWrapPatterns(a), expect);

				} else {
					// x = SimplePatternA
					addRightIdLeaf(a, expect);
				}
				return;
			}

			case N_ID_OPERATOR: {
				fTokenKind[] expect = expect2Array(T_ID, T_LPAREN, T_RPAREN, T_FAT_ARROW, T_SEMI, T_NL);
				// SimplePatternA {OP SimplePatternB }
				// x="SimplePatternB"
				if(isLa(1, T_LPAREN)) {
					// x="SimplePatternA ( Patterns )"
					setRootRightLeaf(a, idParenWrapPatterns(a), expect);

				} else {
					// x = SimplePatternA
					addRightIdLeaf(a, expect);
					a.isContinue = true;
				}
				return;
			}

			case N_ID_LEAF: {
				// SimplePattern {OP SimplePattern}
				// x="OP"
				insertIdOp(a, accept(T_ID), T_ID, T_FAT_ARROW, T_LPAREN, T_SEMI, T_NL);
				a.isContinue = true;
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
				addRootRightIdLeaf(a, T_COMMA, T_ID, T_LBRACKET, T_LPAREN, T_RPAREN, T_FAT_ARROW, T_SEMI, T_NL);
				a.isContinue = true;
				return;
			}
			default:
				throw new RuntimeException("Unexpected Previous  NodeKind: " + a.prevNKind);
		}
	}


	private void expressionTID(ProdArgs a) {
		// Prefix Operator not implemented !!
		switch (a.prevNKind) {
			case N_ROOT: case N_ID_OPERATOR:{
				if(isLa(1, T_LPAREN)){
					// x = "func(args)"
					FunCallLeafNode funCallLeaf = new FunCallLeafNode(a.lastOpN, accept(T_ID));
					setRightLeaf(a, funCallLeaf);
					accept(T_LPAREN);
					funCallLeaf.val().funcArgsLeafN = exprs(null);
					accept(T_RPAREN);
					if(isColonOpT(0)) {
						next();
						funCallLeaf.val().returnTypeLeafN = typeProd();
					}
				} else if(isLaIdOpChar(1, OpChar.COLON)) {
					// x = id : Type
					setRightLeaf(a, idTyped(a));
				} else {
					// x = id
					addRightIdLeaf(a);
				}
				expectOneOf(0, T_COMMA, T_ID, T_LPAREN, T_RPAREN, T_ELSE, T_FAT_ARROW, T_SEMI, T_NL);
				a.isContinue = true;
				return;
			}

			case N_ID_LEAF: {
				// ID_OPERATOR after ( LEAF, FUN_CALL_LEAF )
				// "x +", "x = ",  "func(x) + ", "func(x) = "
				boolean isAssign = isAssignOpT(0);
				insertIdOp(a, accept(T_ID));
				if (isAssign) {
					a.lastOpN.setRight(expressionProd());
					a.isContinue = true;
					return;
				}
				expectOneOf(0, T_COMMA, T_ID, T_LPAREN, T_RPAREN, T_ELSE, T_FAT_ARROW, T_SEMI, T_NL);
				return;
			}
			default:
				throw new RuntimeException("Unexpected Previous  NodeKind: " + a.prevNKind);
		}
	}

	private void expressionIF(ProdArgs a) {
		switch (getPrevNKind(a, fTreeNKind.N_IF_KW_LEAF)) {
			case N_ROOT:
			case N_ID_OPERATOR: {
				IfKwLeafNode ifLeafN = new IfKwLeafNode(a.lastOpN, accept(T_IF));
				accept(T_LPAREN);
				ifLeafN.val().ifCondLeafN = expressionProd();
				accept(T_RPAREN);
				skipNL();
				ifLeafN.val().ifBodyLeafN = expressionProd();
				if (token.kind == T_ELSE) {
					ElseKwLeafNode elseLeafN = new ElseKwLeafNode(a.lastOpN, accept(T_ELSE));
					setRightLeaf(a, elseLeafN);
					elseLeafN.val().ifLeafN = ifLeafN;
					skipSemi();
					elseLeafN.val().elseLeafN = expressionProd();
				} else {
					setRightLeaf(a, ifLeafN);
				}
				a.isContinue = true;
				return;
			}
			default:
				throw new RuntimeException("Unexpected token: " + token.kind);
		}
	}

	void expressionWhile(ProdArgs a) {
		switch (getPrevNKind(a, fTreeNKind.N_WHILE_KW_LEAF)) {
			case N_ROOT:
			case N_ID_OPERATOR: {
				WhileKwLeafNode whileLeafN = new WhileKwLeafNode(a.lastOpN, accept(T_WHILE));
				setRightLeaf(a, whileLeafN);
				accept(T_LPAREN);
				whileLeafN.val().whileCondLeafN = expressionProd();
				accept(T_RPAREN);
				skipNL();
				whileLeafN.val().whileBodyLeafN = expressionProd();
				a.isContinue = true;
				return;
			}
			default:
				throw new RuntimeException("Unexpected token: " + token.kind);
		}
	}

	void expressionFor(ProdArgs a) {
		ForKwLeafNode forLeafN = new ForKwLeafNode(a.lastOpN, accept(T_FOR));
		if(token.kind == T_LPAREN) {
			next();
			forLeafN.val().enumeratorsLeafN = expressionProd();
			accept(T_RPAREN);
		} else if(token.kind == T_LBRACKET) {
			next();
			forLeafN.val().enumeratorsLeafN = enumerators();
			accept(T_RBRACKET);
		} else {
			throw new RuntimeException("Unexpected token: " + token.kind);
		}
		skipNL();
		if(token.kind == T_YIELD) {
			next();
		}
		forLeafN.val().yieldLeafN = expressionProd();
	}

	ProdRootLeafN enumerators() {
		ProdArgs a = initRootNodeProlog(ProdRootOp.ENUMERATORS_PRD);
		generator(a); skipSemi();
		loop:
		while (true) {
			switch (token.kind){
				case T_RPAREN: case T_RCURL:
					break loop;
				default:
					generator(a);
					skipSemi(); break;
			}
		}
		return prodRootLeafN(a);
	}

	void generator(ProdArgs a) {
		GeneratorLeafNode genLeafN = new GeneratorLeafNode(a.lastOpN, token);
		setRightLeaf(a, genLeafN);
		if(token.kind == T_CASE){
			genLeafN.val().isCase = true;
		}
		genLeafN.val().leftArgPattern1LeafN = pattern1Prod();
		accept(T_LEFT_ARROW);
		genLeafN.val().rightArgExprLeafN = expressionProd();

		GeneratorLeafValue.GeneratorExprAfter genExprAfter = new GeneratorLeafValue.GeneratorExprAfter();

		loop:
		while (true){
			skipSemi();
			switch (token.kind) {
				case T_IF:
					next();
					genExprAfter.guard = postfixExprProd();
					genLeafN.val().generatorExprAfterList.add(genExprAfter);
					break;
				case T_ID: case T_LPAREN:
					genExprAfter.pattern1LeafN = pattern1Prod();
					acceptOpChar(OpChar.COLON);
					genExprAfter.exprLeafN = expressionProd();
					genLeafN.val().generatorExprAfterList.add(genExprAfter);
					break;
				default:
					break loop;
			}
		}
	}

	void expressionTry(ProdArgs a) {
		TryKwLeafNode tryLeafN = new TryKwLeafNode(a.lastOpN, accept(T_TRY));
		tryLeafN.val().tryBodyLeafN = expressionProd();
		if(token.kind == T_CATCH) {
			next();
			tryLeafN.val().catchLeafN = expressionProd();
		}
		if(token.kind == T_FINALLY) {
			next();
			tryLeafN.val().finallyLeafN = expressionProd();
		}
	}

	void expressionDo(ProdArgs a) {
		DoKwLeafNode doLeafN = new DoKwLeafNode(a.lastOpN, accept(T_DO));
		doLeafN.val().doBodyLeafN = expressionProd();
		skipSemi();
		accept(T_WHILE);
		accept(T_LPAREN);
		doLeafN.val().doCondLeafN = expressionProd();
		accept(T_RPAREN);
	}

	void expressionThrow(ProdArgs a) {
		ThrowKwLeafNode throwLeafN = new ThrowKwLeafNode(a.lastOpN, accept(T_THROW));
		throwLeafN.val().throwExprLeafN = expressionProd();
	}

	void expressionReturn(ProdArgs a) {
		ReturnKwLeafNode returnLeafN = new ReturnKwLeafNode(a.lastOpN, accept(T_RETURN));
		switch (token.kind) {
			case T_ID: case T_LPAREN: case T_LCURL: case T_IF: case T_WHILE: case T_FOR: case T_TRY: case T_DO: case T_THROW: case T_RETURN: case T_NEW:
				returnLeafN.val().returnExprLeafN = expressionProd();
				break;
			default:
		}
	}

	private ProdRootLeafN patterns() {
		ProdArgs a = initRootNodeProlog(ProdRootOp.PATTERNS_PRD);
		setRightLeaf(a, pattern());
		while (token.kind == T_COMMA) {
			insertCommaOp(a, next());
			setRightLeaf(a, pattern());
		}
		return prodRootLeafN(a);
	}

	private ProdRootLeafN pattern() {
		ProdRootLeafN rootLeaf = pattern1Prod();
		while (isPipeOpT(0)) {
			rootLeaf = pattern1Prod(rootLeaf, fTreeNKind.N_ID_OPERATOR, next());
		}
		return rootLeaf;
	}

	private ProdRootLeafN pattern2s() {
		ProdRootLeafN rootLeaf = pattern2Prod();
		while (token.kind == T_COMMA) {
			rootLeaf = pattern2Prod(rootLeaf, fTreeNKind.N_ID_OPERATOR, next());
		}
		return rootLeaf;
	}

	private ProdRootLeafN exprs(ProdRootLeafN rootLeaf) {
		rootLeaf = expressionProd(rootLeaf, fTreeNKind.N_ID_LEAF, null);
		while (token.kind == T_COMMA) {
			rootLeaf = expressionProd(rootLeaf, fTreeNKind.N_ID_OPERATOR, next());
		}
		return rootLeaf;
	}

	private ProdRootLeafN types(ProdRootLeafN rootLeaf) {
		rootLeaf = typeProd(rootLeaf, fTreeNKind.N_ID_LEAF, null);
		while (token.kind == T_COMMA) {
			rootLeaf = typeProd(rootLeaf, fTreeNKind.N_ID_OPERATOR, next());
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
				setRightLeaf(a, funCallLeaf);
				accept(T_LPAREN);
				funCallLeaf.val().funcArgsLeafN = exprs(null);
				accept(T_RPAREN);
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
				insertOpNode(a, token);
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
				insertOpNode(a, token);
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

	private ProdRootLeafN constrBlockProd() {
		return constrBlockProd(null, null, null);
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

	private ProdRootLeafN constrBlockProd(ProdRootLeafN wrapSubExpr, fTreeNKind prevNKind, fToken opToken) {
		return commonProd(CONSTR_BLOCK_PRD, wrapSubExpr, prevNKind, opToken);
	}

	private ProdRootLeafN blockProd(ProdRootLeafN wrapSubExpr, fTreeNKind prevNKind, fToken opToken) {
		return commonProd(BLOCK_PRD, wrapSubExpr, prevNKind, opToken);
	}

	private ProdRootLeafN templateBody(ProdRootLeafN wrapSubExpr, fTreeNKind prevNKind, fToken opToken) {
		return commonProd(ProdRootOp.TEMPLATE_BODY_PRD, wrapSubExpr, prevNKind, opToken);
	}

	private ProdRootLeafN pattern3Prod() {
		return pattern3Prod(null, null, null);
	}

	private ProdRootLeafN pattern2Prod() {
		return pattern2Prod(null, null, null);
	}

	private ProdRootLeafN pattern1Prod() {
		return pattern1Prod(null, null, null);
	}

	private ProdRootLeafN pattern1Prod(ProdRootLeafN wrapSubExpr, fTreeNKind prevNKind, fToken opToken) {
		return commonProd(PATTERN1_PRD, wrapSubExpr, prevNKind, opToken);
	}

	private ProdRootLeafN pattern2Prod(ProdRootLeafN wrapSubExpr, fTreeNKind prevNKind, fToken opToken) {
		return commonProd(ProdRootOp.PATTERN2_PRD, wrapSubExpr, prevNKind, opToken);
	}

	private ProdRootLeafN pattern3Prod(ProdRootLeafN wrapSubExpr, fTreeNKind prevNKind, fToken opToken) {
		return commonProd(ProdRootOp.PATTERN3_PRD, wrapSubExpr, prevNKind, opToken);
	}

	void patDef(ProdArgs a, fVariable.DefDcl defDcl, fVariable.StoreType storeType) {

		PatDefLeafNode leafNode = new PatDefLeafNode(a.lastOpN, next());
		setRightLeaf(a, leafNode);
		leafNode.val().defDcl = defDcl;
		leafNode.val().storeType = storeType;

		leafNode.val().pattern2sLeafN = pattern2s();
		if (isColonOpT(0)) {
			next();
			leafNode.val().typeLeafN = typeProd();
		}
		if (isAssignOpT(0)) {
			next();
			leafNode.val().exprLeafN = expressionProd();
		}
	}

	ProdRootLeafN typeParams() {
		accept(T_LBRACKET);
		ProdArgs a = initRootNodeProlog(ProdRootOp.TYPE_PARAM_PRD);
		setRightLeaf(a, typeParam());
		while (token.kind == T_COMMA) {
			insertCommaOp(a, next());
			setRightLeaf(a, typeParam());
		}
		accept(T_RBRACKET);
		return prodRootLeafN(a);
	}

	ProdRootLeafN variantTypeParams() {
		accept(T_LBRACKET);
		ProdArgs a = initRootNodeProlog(ProdRootOp.TYPE_PARAM_PRD);
		setRightLeaf(a, variantTypeParam());

		while (token.kind == T_COMMA) {
			insertCommaOp(a, next());
			setRightLeaf(a, variantTypeParam());
		}
		accept(T_RBRACKET);
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
					accept(T_ID);
					leafNode.val().setVariant(prevToken.opChar());
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
			leafNode.val().variantTypeParamsLeafN = variantTypeParams();
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

	ProdRootLeafN paramClauses() {
		ProdArgs a = initRootNodeProlog(ProdRootOp.PARAM_CLAUSES_PRD);
		while (token.kind == T_LPAREN) {
			ProdRootLeafN v = paramClause();
			if (v != null) {
				setRightLeaf(a, v);
				insertSemiOp(a);
			}
		}
		return prodRootLeafN(a);
	}

	ProdRootLeafN paramClause() {
		accept(T_LPAREN);
		ProdRootLeafN ps = null;
		if(token.kind != T_RPAREN) {
			ps = params();
		}
		accept(T_RPAREN);
		return ps;
	}

	ProdRootLeafN params() {
		ProdArgs a = initRootNodeProlog(ProdRootOp.PARAM_PRD);
		boolean isImplicit = false;
		if (token.kind == T_IMPLICIT) {
			isImplicit = true;
			next();
		}
		param(a, isImplicit);
		while (token.kind == T_COMMA) {
			insertCommaOp(a, next());
			param(a, false);
		}
		return prodRootLeafN(a);
	}

	ProdRootLeafN classParams() {
		accept(T_LPAREN);
		ProdArgs a = initRootNodeProlog(ProdRootOp.CLASS_PARAMS_PRD);
		classParam(a);
		while (token.kind == T_COMMA) {
			insertCommaOp(a, next());
			classParam(a);
		}
		accept(T_RPAREN);
		return prodRootLeafN(a);
	}


	void param(ProdArgs a, boolean isImplicit) {

		ParamLeafNode leafNode = new ParamLeafNode(a.lastOpN, token);
		setRightLeaf(a, leafNode);
		leafNode.val().paramName = accept(T_ID).name();
		leafNode.val().isImplicit = isImplicit;

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
		leafNode.val().modifiersLeafN = modifierProd(MODIFIER_PRD);
		switch (token.kind) {
			case T_VAL:
				leafNode.val().storeType = VAL;
				next();
			case T_VAR:
				leafNode.val().storeType = VAR;
				next();
				break;
			default:
				break;
		}
		leafNode.val().paramName = accept(T_ID).name();
		acceptOpChar(OpChar.COLON);
		leafNode.val().paramTypeLeafN = typeProd();
		if (isAssignOpT(0)) {
			next();
			leafNode.val().exprLeafN = expressionProd();
		}
	}

	ProdRootLeafN funSig() {
		ProdArgs a = initRootNodeProlog(ProdRootOp.FUN_SIG_PRD);
		FunSigLeafNode leafNode = new FunSigLeafNode(a.lastOpN, accept(T_ID));
		setRightLeaf(a, leafNode);
		leafNode.val().funName = prevToken.name();
		if (token.kind == T_LBRACKET) {
			leafNode.val().typeParamsLeafN = typeParams();
		}
		leafNode.val().paramsLeafN = paramClauses();
		return prodRootLeafN(a);
	}

	ProdRootLeafN blockExpr() {
		expectOneOf(0, T_LCURL);
		if(isLa(0, T_CASE)) {
			return caseClassesProd();
		}
		return blockProd();
	}


	ProdRootLeafN argumentExprs() {
		switch (token.kind) {
			case T_LPAREN: {
				ProdArgs a = null;
				accept(T_LPAREN);
				if (token.kind != T_RPAREN) {
					a = initRootNodeProlog(ProdRootOp.ARG_EXPRS_PRD);
					setRightLeaf(a, expressionProd());
					while (token.kind == T_COMMA) {
						insertCommaOp(a, next());
						setRightLeaf(a, expressionProd());
					}
				}
				accept(T_RPAREN);
				if (a != null) return prodRootLeafN(a);
				return null;
			}
			case T_LCURL: {
				return blockExpr();
			}
			default:
				return null;
		}
	}

	ProdRootLeafN selfInvocation() {
		accept(T_THIS);
		accept(T_LPAREN);
		ProdArgs a = null;
		if (token.kind != T_RPAREN) {
			a = initRootNodeProlog(ProdRootOp.SELF_INVOCATION_PRD);
			setRightLeaf(a, expressionProd());
			while (token.kind == T_COMMA) {
				insertCommaOp(a, next());
				setRightLeaf(a, expressionProd());
			}
		}
		accept(T_RPAREN);
		if (a != null) return prodRootLeafN(a);
		return null;
	}

	void funDef(ProdArgs a) {
		FunDclDefLeafNode leafNode = new FunDclDefLeafNode(a.lastOpN, accept(T_DEF));
		setRightLeaf(a, leafNode);
		leafNode.val().defDcl = fVariable.DefDcl.DEF;
		switch (token.kind) {
			case T_ID:
				leafNode.val().funSigLeafN = funSig();
				if(isColonOpT(0)){
					next();
					leafNode.val().funSigTypeLeafN = typeProd();
					acceptOpChar(OpChar.ASSIGN);
					leafNode.val().funSigExprLeafN = expressionProd();
				} else if(isAssignOpT(0)) {
					next();
					leafNode.val().funSigExprLeafN = expressionProd();
				} else {
					skipNL();
					leafNode.val().funSigBlockLeafN = blockProd();
				}
				break;
			case T_THIS:
				leafNode.val().thisParamsLeafN = paramClauses();
				if (isAssignOpT(0)) {
					next();
					if (token.kind == T_THIS) {
						leafNode.val().funSigBlockLeafN = selfInvocation();
					}
					leafNode.val().funSigBlockLeafN = blockProd();
				} else {
					leafNode.val().funSigBlockLeafN = blockProd();
				}
				break;
			default:
				throw new RuntimeException("Unexpected token: " + token.kind);
		}
	}

	private void typeDef(ProdArgs a) {
		TypeDefDclLeafNode leafNode = new TypeDefDclLeafNode(a.lastOpN, accept(T_TYPE));
		setRightLeaf(a, leafNode);

		leafNode.val().typeName = accept(T_ID).name();
		if (token.kind == T_LBRACKET) {
			leafNode.val().variantTypeParamsLeafN = variantTypeParams();
		}

		if(isAssignOpT(0)) {

			leafNode.val().defDcl = fVariable.DefDcl.DEF;
			next();
			leafNode.val().defTypeLeafN = typeProd();

		} else {

			leafNode.val().defDcl = fVariable.DefDcl.DCL;
			if (token.kind == T_LOWER_BOUND) {
				leafNode.val().dclLowerTypeLeafN = typeProd();
			}
			if (token.kind == T_UPPER_BOUND) {
				leafNode.val().dclUpperTypeLeafN = typeProd();
			}
		}
	}


	private void traitDef(ProdArgs a) {
		TraitDefLeafNode leafNode = new TraitDefLeafNode(a.lastOpN, accept(T_TRAIT));
		setRightLeaf(a, leafNode);
		leafNode.val().traitName = accept(T_ID).name();
		if (token.kind == T_LBRACKET) {
			leafNode.val().variantTypeParamsLeafN = variantTypeParams();
		}
		if (token.kind == T_EXTENDS) {
			next();
			leafNode.val().extends_ = true;
		}
		if (token.kind == T_ID) {
			leafNode.val().traitParentsLeafN = typeProd();
		}
		if (token.kind == T_LCURL) {
			leafNode.val().templateBodyLeafN = templateBodyProd();
		}
	}

	ProdRootLeafN modifierProd(ProdRootOp op) {
		switch (token.kind){
			case T_PRIVATE:
			case T_PROTECTED:
			case T_ABSTRACT:
			case T_FINAL:
			case T_SEALED:
			case T_IMPLICIT:
			case T_LAZY:
			case T_OVERRIDE: {
				break;
			}
			default: {
				return null;
			}
		}
		assert op == LOCAL_MODIFIER_PRD || op == ACCESS_MODIFIER_PRD || op == MODIFIER_PRD;
		ProdArgs a = initRootNodeProlog(op);
		loop:
		while (true) {
			switch (op){
				case LOCAL_MODIFIER_PRD: {
					if (_localModifier(a)) continue;
					break loop;
				}
				case ACCESS_MODIFIER_PRD: {
					if (_accessModifier(a)) continue;
					break loop;
				}
				case MODIFIER_PRD: {
					if(token.kind == T_OVERRIDE) {
						_addModifierLeafN(a); continue;
					}
					if (_localModifier(a)) continue;
					if (_accessModifier(a)) continue;
					break loop;
				}
				default:
					break loop;
			}
		}
		return prodRootLeafN(a);
	}

	ModifierLeafNode _addModifierLeafN(ProdArgs a){
		ModifierLeafNode leafNode = new ModifierLeafNode(a.lastOpN, token);
		setRightLeaf(a, leafNode);
		leafNode.val().setModifierKind(fModifierMap.getModifierKind(token.name()));
		next();
		insertCommaOp(a);
		return leafNode;
	}

	boolean _localModifier(ProdArgs a){
		switch (token.kind) {
			case T_ABSTRACT:
			case T_FINAL:
			case T_SEALED:
			case T_IMPLICIT:
			case T_LAZY: {
				_addModifierLeafN(a);
				return true;
			}
			default:
				return false;
		}
	}

	boolean _accessModifier(ProdArgs a) {
		switch (token.kind) {
			case T_PRIVATE:
			case T_PROTECTED: {
				ModifierLeafNode leafNode = _addModifierLeafN(a);
				if (token.kind == T_LBRACKET) {
					next();
					leafNode.val().setAccessQualifier(fModifierMap.getAccessQualifier(token.name()));
					accept(T_RBRACKET);
				}
				return true;
			}
			default:
				return false;
		}
	}

	ProdRootLeafN classParents() {
		ProdArgs a = initRootNodeProlog(ProdRootOp.CLASS_PARENTS_PRD);
		ClassParentsLeafNode leafNode = new ClassParentsLeafNode(a.lastOpN, token);
		setRightLeaf(a, leafNode);
		leafNode.val().constrType = typeProd();
		leafNode.val().constrArgExprs = argumentExprs();
		return prodRootLeafN(a);
	}

	void classDef(ProdArgs a, boolean isCase) {
		ClassDefLeafNode leafNode = new ClassDefLeafNode(a.lastOpN, accept(T_CLASS));
		setRightLeaf(a, leafNode);
		leafNode.val().className = accept(T_ID).name();
		leafNode.val().isCase = isCase;
		if (token.kind == T_LBRACKET) {
			leafNode.val().typeParamsLeafN = variantTypeParams();
		}
		leafNode.val().accessModifierLeafN = modifierProd(ACCESS_MODIFIER_PRD);
		if (token.kind == T_LPAREN) {
			leafNode.val().classParamsLeafN = classParams();
		}
		if (token.kind == T_EXTENDS) {
			next();
			leafNode.val().extends_ = true;
		}
		switch (token.kind) {
			case T_LCURL:
				leafNode.val().templateBodyLeafN = templateBodyProd();
				break;
			case T_ID:
			case T_LPAREN:
				leafNode.val().classParentsLeafN = classParents();
				if (token.kind == T_LCURL) {
					leafNode.val().templateBodyLeafN = templateBodyProd();
				}
				break;
			default:
				throw new RuntimeException("Unexpected token: " + token.kind);
		}
	}

	void objectDef(ProdArgs a, boolean isCase) {
		ObjectDefLeafNode leafNode = new ObjectDefLeafNode(a.lastOpN, accept(T_OBJECT));
		setRightLeaf(a, leafNode);
		accept(T_ID);
		leafNode.val().objectName = prevToken.name();
		leafNode.val().isCase = isCase;
	}

	void templateStatProdLoop(ProdArgs a) {
		accept(T_LCURL);

		loop:
		while (true) {
			switch (token.kind) {
				case T_NL:
				case T_SEMI: {
					next();
					break;
				}

				case T_RCURL:
					break loop;

				case T_IMPORT: {
					importDef(a);
					break;
				}

				case T_VAL: {
					patDef(a, DEF_DCL, VAL);
					break;
				}
				case T_VAR: {
					patDef(a, DEF_DCL, VAR);
					break;
				}

				case T_DEF: {
					funDef(a);
					break;
				}

				case T_TYPE: {
					typeDef(a);
					break;
				}

				case T_ID:
				case T_STR_LIT:
				case T_NEW:
				case T_LPAREN:
				case T_LCURL:
				case T_IF:
				case T_WHILE:
				case T_TRY:
				case T_DO:
				case T_FOR:
				case T_THROW:
				case T_RETURN: {
					setRightLeaf(a, expressionProd());
					break;
				}

				case T_OVERRIDE: case T_PRIVATE: case T_PROTECTED: case T_ABSTRACT: case T_FINAL: case T_SEALED: case T_IMPLICIT: case T_LAZY: {
					setRightLeaf(a, modifierProd(MODIFIER_PRD));
					expectOneOf(0, T_VAL, T_VAR, T_DEF, T_TYPE,  T_CASE, T_CLASS, T_OBJECT, T_TRAIT);
					continue ;
				}
				default:
					throw new RuntimeException("Unexpected token: " + token.kind);
			}
			insertCommaOp(a);
		}
		accept(T_RCURL);
	}

	void blockStatProdLoop(ProdArgs a, ProdRootOp prodRootOp) {

		accept(T_LCURL);
		assert prodRootOp == CONSTR_BLOCK_PRD || prodRootOp == BLOCK_PRD;

		boolean isCase = false;

		if (prodRootOp == CONSTR_BLOCK_PRD && token.kind == T_THIS) {
			setRightLeaf(a, selfInvocation());
		}

		loop:
		while (true) {
			switch (token.kind) {
				case T_NL:
				case T_SEMI: {
					next();
					continue;
				}

				case T_RCURL:
					break loop;

				case T_IMPORT:
					importDef(a);
					break;

				case T_VAL: {
					patDef(a, DEF, VAL);
					break;
				}
				case T_VAR: {
					patDef(a, DEF, VAR);
					break;
				}

				case T_DEF: {
					funDef(a);
					break;
				}

				case T_TYPE: {
					typeDef(a);
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

				case T_ID:
				case T_STR_LIT:
				case T_NEW:
				case T_LPAREN:
				case T_LCURL:
				case T_IF:
				case T_WHILE:
				case T_TRY:
				case T_DO:
				case T_FOR:
				case T_THROW:
				case T_RETURN: {
					setRightLeaf(a, expressionProd());
					break;
				}

				default:
					throw new RuntimeException("Unexpected token: " + token.kind);
			}
			if (!isCase) {
				insertSemiOp(a);
			}
		}
		accept(T_RCURL);
	}

	void pattern3LParen(ProdArgs a) {
		switch (a.prevNKind) {
			case N_ROOT: case N_ID_LEAF: {
				accept(T_LPAREN);
				if(token.kind != T_RPAREN) {
					ParenWrapPatternsLeafNode n = new ParenWrapPatternsLeafNode(a.lastOpN, prevToken);
					setRightLeaf(a, n);
					n.val().patternsLeafN = patterns();
				}
				accept(T_RPAREN);
				expectOneOf(0, T_ID, T_COMMA, T_LPAREN, T_RPAREN, T_FAT_ARROW, T_SEMI, T_NL);
				a.isContinue = true;
				return;
			}
			default:
				throw new RuntimeException("Unexpected token: " + token.kind);
		}
	}

	void patternProdLoop(ProdArgs a, ProdRootOp patternTyp) {
		assert patternTyp == PATTERN1_PRD || patternTyp == PATTERN2_PRD || patternTyp == PATTERN3_PRD;
		loop:
		while (true) {
			a.isContinue = false;
			switch (token.kind) {
				case T_ID: {
					if (isPipeOpT(0)) {
						if (a.prevNKind == fTreeNKind.N_ROOT || a.prevNKind == fTreeNKind.N_ID_OPERATOR) {
							throw new RuntimeException("Pipe | : " + token.kind);
						}
						break loop;
					}
					patternTID(a, patternTyp);
					if (a.isContinue) continue;
					break loop;
				}
				case T_LPAREN: {
					pattern3LParen(a);
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
					if(isAssignOpT(0)){
						break loop;
					}
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

		CaseKwLeafNode leafNode = new CaseKwLeafNode(a.lastOpN, accept(T_CASE));
		setRightLeaf(a, leafNode);

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
		accept(T_LCURL);
		loop:
		while (true) {
			switch (token.kind) {
				case T_CASE: {
					caseClassesCase(a);
					insertSemiOp(a);
					continue;
				}
				case T_RCURL:
					break loop;
				default:
					throw new RuntimeException("Unexpected token: " + token.kind);
			}
		}
		accept(T_RCURL);
	}

	void postfixExprProd(ProdArgs a) {

		switch (token.kind) {
			case T_NEW:
				postfixExprNew(a);
				break;
			case T_LCURL:
				if(isLa(1, T_CASE)) {
					a.lastOpN.setRight(caseClassesProd());
				} else {
					a.lastOpN.setRight(blockProd());
				}
				break;
			default:
				break;
		}
	}

	void expressionPostfixExpr(ProdArgs a){
		a.lastOpN.setRight(postfixExprProd());
		if(token.kind == T_MATCH){
			insertOpNode(a, next());
			setRightLeaf(a, caseClassesProd());
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

				case T_WHILE: {
					expressionWhile(a);
					assert a.isContinue == true;
					continue;
				}

				case T_TRY: {
					expressionTry(a);
					assert a.isContinue == true;
					continue;
				}

				case T_DO: {
					expressionDo(a);
					assert a.isContinue == true;
					continue;
				}

				case T_FOR: {
					expressionFor(a);
					assert a.isContinue == true;
					continue;
				}

				case T_THROW: {
					expressionThrow(a);
					assert a.isContinue == true;
					continue;
				}

				case T_RETURN: {
					expressionReturn(a);
					assert a.isContinue == true;
					continue;
				}

				case T_FAT_ARROW: {
					expressionFatArrow(a);
					break loop;
				}

				case T_NEW:
				case T_LCURL: {
					expressionPostfixExpr(a);
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
				postfixExprProd(a);
				break;

			case TYPE_PRD:
				typeProdLoop(a);
				break;

			case CASE_CLASSES_PRD:
				caseClassesProdLoop(a);
				break;

			case BLOCK_PRD:
				blockStatProdLoop(a, BLOCK_PRD);
				break;

			case CONSTR_BLOCK_PRD:
				blockStatProdLoop(a, CONSTR_BLOCK_PRD);
				break;

			case TEMPLATE_BODY_PRD:
				templateStatProdLoop(a);
				break;

			case PATTERN1_PRD:
				patternProdLoop(a, PATTERN1_PRD);
				break;

			case PATTERN2_PRD:
				patternProdLoop(a, PATTERN2_PRD);
				break;

			case PATTERN3_PRD:
				patternProdLoop(a,  PATTERN3_PRD);
				break;
			default:
				throw new RuntimeException("Unexpected ProdRootOp: " + prodRootOp);
		}

		while (a.lastOpN.parent() != null) {
			a.lastOpN = a.lastOpN.parent();
		}
		return prodRootLeafN(a);
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

	private void tmplDef(ProdArgs a) {
		boolean isCase = false;
		if (token.kind == T_CASE) {
			isCase = true;
			next();
			expectOneOf(0, T_CLASS, T_OBJECT);
		}
		switch (token.kind) {
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

	private void importDef(ProdArgs a) {
		ImportDefLeafNode leafNode = new ImportDefLeafNode(a.lastOpN, accept(T_IMPORT));
		setRightLeaf(a, leafNode);
		accept(T_ID);
		leafNode.val().importPath = prevToken.name();
	}

	CommonOpNode _insertOpNode(CommonOpNode lastOpN, fToken op_token) {
		fOperatorKind k = getOperatorKind(op_token);
		if (k.isRightAssociative || k.precedence() > lastOpN.precedence()) {
			lastOpN = insertHigherPrecedenceOpNode(lastOpN, k, op_token);
		} else {
			lastOpN = insertLessOrEqualPrecedenceOpNode(lastOpN, k, op_token);
		}
		return lastOpN;
	}

	private void insertOpNode(ProdArgs a, fToken t) {
		a.prevNKind = fTreeNKind.N_ID_OPERATOR;
		a.lastOpN = _insertOpNode(a.lastOpN, t);
	}

	private void insertIdOp(ProdArgs a,  fToken t, fTokenKind... types) {
		insertOpNode(a, t);
		if(types != null && types.length > 0) {
			expectOneOf(0, types);
		}
	}

	private void insertCommaOp(ProdArgs a, fToken t) {
		assert t.kind == T_COMMA;
		insertOpNode(a, t);
	}

	private void insertCommaOp(ProdArgs a) {
		insertOpNode(a, Comma);
	}

	private void insertSemiOp(ProdArgs a) {
		insertOpNode(a, Semicolon);
	}

	private ProdArgs initRootNodeProlog(ProdRootOp prodRootOp) {
		ProdArgs a = new ProdArgs();
		a.lastOpN = new RootOpN(prodRootOp);
		a.prevNKind = fTreeNKind.N_ROOT;
		return a;
	}

	private ProdRootLeafN prodRootLeafN(ProdArgs a) {
		return new ProdRootLeafN(null, a.lastOpN);
	}

	private void addRootRightIdLeaf(ProdArgs a, fTokenKind... expectTypes) {
		assert a.prevNKind == fTreeNKind.N_ROOT;
		addRightIdLeaf(a, expectTypes);
	}

	private void addRightIdLeaf(ProdArgs a, fTokenKind... expectTypes) {
		setRightLeaf(a, new IdLeafNode(a.lastOpN, accept(T_ID)), expectTypes);
	}

	private void setRightLeaf(ProdArgs a, CommonLeafNode n) {
		setRightLeaf(a, n, (com.flint.compiler.token.fTokenKind[])null);
	}

	private void setRootRightLeaf(ProdArgs a, CommonLeafNode n, fTokenKind... expectTypes) {
		assert a.prevNKind == fTreeNKind.N_ROOT;
		setRightLeaf(a, n, expectTypes);
	}

	private void setRightLeaf(ProdArgs a, CommonLeafNode n, fTokenKind... expectTypes) {
		assert a.lastOpN.right() == null;
		a.lastOpN.setRight(n);
		a.prevNKind = fTreeNKind.N_ID_LEAF;
		if (expectTypes != null && expectTypes.length > 0) {
			expectOneOf(0, expectTypes);
		}
	}

	private fTokenKind[] expect2Array(fTokenKind... expectTypes) {
		return expectTypes;
	}

	public ProdRootLeafN compilationUnit() {
		ProdArgs a = initRootNodeProlog(ProdRootOp.COMP_UNIT_PRD);
		while (token.kind != T_EOF) {
			switch (token.kind) {
				case T_SEMI:
				case T_NL: {
					next();
					continue;
				}
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
