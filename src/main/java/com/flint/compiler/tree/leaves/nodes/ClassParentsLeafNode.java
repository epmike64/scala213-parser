package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.ClassParentsLeafValue;

public class ClassParentsLeafNode extends CommonLeafNode {
	public ClassParentsLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_CLASS_PARENTS_LEAF, new ClassParentsLeafValue(token));
	}

	@Override
	public ClassParentsLeafValue val() {
		return (ClassParentsLeafValue) value;
	}
}
