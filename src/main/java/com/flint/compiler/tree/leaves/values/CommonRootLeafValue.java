package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.operators.nodes.CommonOpNode;

public class CommonRootLeafValue implements fTree.BsNodeValue {
	public final CommonOpNode opNode;

	public CommonRootLeafValue(CommonOpNode root) {
		this.opNode = root;
	}
}
