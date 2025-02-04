package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.NamedToken;
import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.TypeParamLeafValue;

public class TypeParamLeafNode extends CommonLeafNode {
	public TypeParamLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_TYPE_PARAM_LEAF, new TypeParamLeafValue(token));
	}

	@Override
	public TypeParamLeafValue val() {
		return (TypeParamLeafValue) value;
	}

}
