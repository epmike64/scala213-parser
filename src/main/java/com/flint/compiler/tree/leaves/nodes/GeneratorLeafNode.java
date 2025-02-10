package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.GeneratorLeafValue;

public class GeneratorLeafNode extends CommonLeafNode{
	public GeneratorLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_GENERATOR_LEAF, new GeneratorLeafValue(token));
	}

	public GeneratorLeafValue val() {
		return (GeneratorLeafValue) value;
	}
}
