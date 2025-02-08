package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.NamedToken;
import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.IdTypedLeafValue;

public class IdTypedLeafNode extends CommonLeafNode {
	public IdTypedLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_ID_TYPED_LEAF, new IdTypedLeafValue((NamedToken) token));
	}

	public IdTypedLeafValue val() {
		return (IdTypedLeafValue) value;
	}
}
