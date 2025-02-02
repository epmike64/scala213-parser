package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.tree.fTree;

public class CommonLeafNode extends fTree.BsNode {
	public CommonLeafNode(fTree.BsNode parent, fTree.fTreeNKind kind, fTree.BsNodeValue v) {
		super(parent, kind, null, null, v);
	}


	@Override
	public String toString() {
		return "" + value;
	}

	@Override
	public void setLeft(fTree.BsNode left) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRight(fTree.BsNode right) {
		throw new UnsupportedOperationException();
	}

	@Override
	public fTree.BsNode left() {
		return null;
	}

	@Override
	public fTree.BsNode right() {
		return null;
	}
}
