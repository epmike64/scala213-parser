package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.TemplateStatLeafValue;

public class TemplateStatLeafNode extends CommonLeafNode {
	public TemplateStatLeafNode(fTree.BsNode parent, fToken value) {
		super(parent, fTree.fTreeNKind.N_TEMPLATE_STAT_LEAF, new TemplateStatLeafValue(value));
	}

	public TemplateStatLeafValue val() {
		return (TemplateStatLeafValue) value;
	}
}
