package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.ObjectDefLeafValue;

public class ObjectDefLeafNode extends CommonLeafNode {
	public ObjectDefLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_OBJECT_DEF_LEAF, new ObjectDefLeafValue(token));
	}

	public ObjectDefLeafValue val() {
		return (ObjectDefLeafValue) value;
	}
}
