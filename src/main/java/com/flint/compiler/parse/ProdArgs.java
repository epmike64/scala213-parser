package com.flint.compiler.parse;

import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.operators.nodes.CommonOpNode;

public class ProdArgs {
	public CommonOpNode lastOpN;
	public fTree.fTreeNKind prevNKind;
	public boolean isContinue;
}
