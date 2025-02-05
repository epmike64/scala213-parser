package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.TemplateBodyLeafValue;

public class TemplateBodyLeafNode extends CommonLeafNode{
	public TemplateBodyLeafNode(fTree.BsNode parent, fTree.fTreeNKind kind, TemplateBodyLeafValue value) {
		super(parent, kind, value);
	}
}
