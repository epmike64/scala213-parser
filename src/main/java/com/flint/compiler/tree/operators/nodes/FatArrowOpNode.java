package com.flint.compiler.tree.operators.nodes;

import com.flint.compiler.token.fOperatorKind;
import com.flint.compiler.token.fTokenKind;
import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.operators.values.OperatorValue;

public class FatArrowOpNode extends CommonOpNode {
	public FatArrowOpNode(CommonOpNode parent,  fTree.BsNode left, fTree.BsNode right, fToken op_token) {
		super(parent, fTree.fTreeNKind.N_FAT_ARROW, left, right, new OperatorValue(fOperatorKind.O_FAT_ARROW, op_token));
		assert op_token.kind == fTokenKind.T_FAT_ARROW;
	}
}
