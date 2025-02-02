package com.flint.compiler.tree.operators.values;

import com.flint.compiler.token.fOperatorKind;
import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;

public class OperatorValue implements fTree.BsNodeValue {
	final public fOperatorKind kind;
	final public fToken op_token;

	public OperatorValue(fOperatorKind kind, fToken op_token) {
		this.kind = kind;
		this.op_token = op_token;
	}

	public int precedence() {
		return kind.precedence();
	}

	public boolean isRightAssociative() {
		return kind.isRightAssociative;
	}

	@Override
	public String toString() {
		return "" + kind;
	}

}
