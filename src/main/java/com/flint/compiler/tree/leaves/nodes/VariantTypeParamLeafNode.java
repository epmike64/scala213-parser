package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.NamedToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.VariantTypeParamLeafValue;

public class VariantTypeParamLeafNode extends CommonLeafNode {

	public VariantTypeParamLeafNode(fTree.BsNode parent, NamedToken typeNameTok) {
		super(parent, fTree.fTreeNKind.N_VARIANT_TYPE_PARAM_LEAF, new VariantTypeParamLeafValue(typeNameTok));
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
