package com.flint.compiler.tree.operators.nodes;

import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.operators.values.OperatorValue;

/**
 * Operator Node
 * Left & Right could operator or leaf
 * Parent could be operator or left parenthesis
 */
public class OperatorNode extends CommonOpNode {
	public OperatorNode(CommonOpNode parent, fTree.BsNode left, fTree.BsNode right, OperatorValue value) {
		super(parent, fTree.fTreeNKind.N_ID_OPERATOR, left, right, value);
	}

	public CommonOpNode parent() {
		return (CommonOpNode) parent;
	}

	@Override
	public OperatorValue val() {
		return (OperatorValue) value;
	}

	@Override
	public String toString() {
		return "OP{" + val().op_token.name() +", pos=" + val().op_token.pos + "}";
	}
}
