package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.ClassDefLeafValue;

public class ClassDefLeafNode extends CommonLeafNode {
	public ClassDefLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_CLASS_DEF_LEAF, new ClassDefLeafValue(token));
	}
}
