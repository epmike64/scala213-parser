package com.flint.compiler.tree.leaves.nodes;

import com.flint.compiler.token.type.NamedToken;
import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.values.TypeArgsLeafValue;

public class TypeLeafNode extends CommonLeafNode {

	 public TypeLeafNode(fTree.BsNode parent, NamedToken typeNameTok) {
		  super(parent, fTree.fTreeNKind.N_TYPE_LEAF, new TypeArgsLeafValue(typeNameTok));
	 }

	 public ProdRootLeafN typeArgs() {
		  return val().typeArgsLeafN;
	 }

	public String typeName() {
		return val().token().name();
	}


	@Override
	 public TypeArgsLeafValue val() {
		  return (TypeArgsLeafValue) value;
	 }

	 @Override
	 public String toString() {
		  return "TypeArgs=" + value;
	 }
}
