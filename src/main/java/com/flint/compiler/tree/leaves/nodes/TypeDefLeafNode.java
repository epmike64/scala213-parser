package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.TypeDefLeafValue;

public class TypeDefLeafNode extends CommonLeafNode {
	public TypeDefLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_TYPE_DEF_LEAF, new TypeDefLeafValue(token));
	}
}
