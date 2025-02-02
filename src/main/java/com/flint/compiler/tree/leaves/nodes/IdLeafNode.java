package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.NamedToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.NamedTokenLeafValue;

public class IdLeafNode extends CommonLeafNode {
	public IdLeafNode(fTree.BsNode parent, NamedToken token) {
		super(parent, fTree.fTreeNKind.N_ID_LEAF, new NamedTokenLeafValue(token));
	}

	public String varName() {
		return val().token().name();
	}

	@Override
	public NamedTokenLeafValue val() {
		return (NamedTokenLeafValue) value;
	}
}
