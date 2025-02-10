package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.NamedToken;
import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.FunCallLeafValue;

/**
 * Function call
 */
public class FunCallLeafNode extends CommonLeafNode {

	public FunCallLeafNode(fTree.BsNode parent, fToken token) {
		super(parent, fTree.fTreeNKind.N_FUN_CALL_LEAF, new FunCallLeafValue((NamedToken)token));
	}

	public ProdRootLeafN funcArgs() {
		return val().funcArgsLeafN;
	}

	@Override
	public FunCallLeafValue val() {
		return (FunCallLeafValue) value;
	}

	public String functionName() {
		return val().token().name();
	}

	@Override
	public String toString() {
		return "FuncCall=" + value;
	}
}
