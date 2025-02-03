package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.TraitDefLeafValue;

public class TraitDefLeafNode  extends CommonLeafNode {
	public TraitDefLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_TRAIT_DEF_LEAF, new TraitDefLeafValue(token));
	}
}
