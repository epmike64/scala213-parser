package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.ModifierLeafValue;

public class ModifierLeafNode extends CommonLeafNode {
	public ModifierLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_MODIFIER_LEAF, new ModifierLeafValue(token));
	}

	@Override
	public ModifierLeafValue val() {
		return (ModifierLeafValue) value;
	}
}
