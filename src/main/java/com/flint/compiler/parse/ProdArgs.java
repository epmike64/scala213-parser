package com.flint.compiler.parse;

import com.flint.compiler.tree.operators.nodes.CommonOpNode;

public class ProdArgs {
	public CommonOpNode lastOpN;
	public AstNKind prevNKind;
	public boolean isContinue;
}
