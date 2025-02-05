package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.VariantTypeParamLeafValue;

public class VariantTypeParamLeafNode extends CommonLeafNode {

	public VariantTypeParamLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_VARIANT_TYPE_PARAM_LEAF, new VariantTypeParamLeafValue(token));
	}

	public String typeName() {
		return val().token().name();
	}

	@Override
	public VariantTypeParamLeafValue val() {
		return (VariantTypeParamLeafValue) value;
	}

	@Override
	public String toString() {
		return "VariantTypeParam=" + value;
	}
}
