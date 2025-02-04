package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.ParamLeafValue;

public class ParamLeafNode extends CommonLeafNode  {

	public ParamLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_PARAM_LEAF, new ParamLeafValue(token));
	}

	public ParamLeafValue val() {
		return (ParamLeafValue) value;
	}
}
