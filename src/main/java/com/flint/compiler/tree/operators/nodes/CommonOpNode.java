package com.flint.compiler.tree.operators.nodes;

import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.operators.values.OperatorValue;

/**
 *
 */
public abstract class CommonOpNode extends fTree.BsNode {
	public CommonOpNode(CommonOpNode parent, fTree.fTreeNKind k, fTree.BsNode left, fTree.BsNode right, OperatorValue value) {
		super(parent, k, left, right, value);
	}

	public fTree.fTreeNKind NKind() {
		return (fTree.fTreeNKind) kind;
	}

	public CommonOpNode parent() {
		return (CommonOpNode) parent;
	}

	public OperatorValue val() {
		return (OperatorValue) value;
	}

	public int precedence() {
		return ((OperatorValue) value).precedence();
	}

	@Override
	public void setLeft(fTree.BsNode left) {
		this.left = left;
	}

	@Override
	public void setRight(fTree.BsNode right) {
		this.right = right;
	}

	@Override
	public fTree.BsNode left() {
		return left;
	}

	@Override
	public fTree.BsNode right() {
		return right;
	}
}
