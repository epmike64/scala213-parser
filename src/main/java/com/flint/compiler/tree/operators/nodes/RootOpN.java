package com.flint.compiler.tree.operators.nodes;

import com.flint.compiler.token.fOperatorKind;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.operators.values.OperatorValue;

public class RootOpN extends CommonOpNode {
	final ProdRootOp prodT;

	public RootOpN(ProdRootOp prodT) {
		super(null, fTree.fTreeNKind.N_ROOT, null, null, new OperatorValue(fOperatorKind.O_ROOT, null));
		this.prodT = prodT;
	}

	@Override
	public String toString() {
		return "";
	}

	@Override
	public void setLeft(fTree.BsNode left) {
		throw new UnsupportedOperationException();
	}
}
