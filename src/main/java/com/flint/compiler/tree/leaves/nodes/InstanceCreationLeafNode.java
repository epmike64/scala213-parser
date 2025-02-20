package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.InstanceCreationLeafValue;

public class InstanceCreationLeafNode extends CommonLeafNode {
	public InstanceCreationLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_INSTANCE_CREATION_LEAF, new InstanceCreationLeafValue(token));
	}

	public InstanceCreationLeafValue val() {
		return (InstanceCreationLeafValue) value;
	}
}
