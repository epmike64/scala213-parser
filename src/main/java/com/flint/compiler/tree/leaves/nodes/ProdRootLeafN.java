package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.CommonRootLeafValue;
import com.flint.compiler.tree.operators.nodes.CommonOpNode;

import static com.flint.compiler.tree.fTree.fTreeNKind.N_PROD_ROOT_LEAF;

public class ProdRootLeafN extends CommonLeafNode {
	public ProdRootLeafN(fTree.BsNode parent, CommonOpNode opNode) {
		super(parent, N_PROD_ROOT_LEAF, new CommonRootLeafValue(opNode));
	}

	@Override
	public CommonRootLeafValue val() {
		return (CommonRootLeafValue) value;
	}

	public CommonOpNode opNode() {
		return val().opNode;
	}
}
