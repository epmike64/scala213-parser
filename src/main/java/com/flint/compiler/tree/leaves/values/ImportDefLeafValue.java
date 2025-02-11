package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.fTokenKind;
import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;
import com.flint.compiler.tree.operators.nodes.ProdRootOp;

public class ImportDefLeafValue extends TokenLeafValue {
	public ProdRootLeafN importExprsLeafN;
	public ImportDefLeafValue(fToken token) {
		super(token);
		assert token.kind == fTokenKind.T_IMPORT;
	}
}
