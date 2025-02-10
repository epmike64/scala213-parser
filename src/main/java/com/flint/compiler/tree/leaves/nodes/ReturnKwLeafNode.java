package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.ReturnKwLeafValue;

public class ReturnKwLeafNode extends CommonLeafNode {
	 public ReturnKwLeafNode(fTree.BsNode parent, fToken token) {
		  super(parent, fTree.fTreeNKind.N_RETURN_KW_LEAF, new ReturnKwLeafValue(token));
	 }

	 public ReturnKwLeafValue val() {
		  return (ReturnKwLeafValue) value;
	 }
}
