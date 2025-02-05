package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.ClassParamLeafValue;

public class ClassParamLeafNode extends CommonLeafNode {
	public ClassParamLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_CLASS_PARAM_LEAF, new ClassParamLeafValue(token));
	}

	public ClassParamLeafValue val() {
		return (ClassParamLeafValue) value;
	}
}
