package com.flint.compiler.dot;

import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.nodes.*;
import com.flint.compiler.tree.leaves.values.IfKwLeafValue;
import com.flint.compiler.tree.operators.nodes.CommonOpNode;
import com.flint.compiler.tree.operators.nodes.OperatorNode;
import com.flint.compiler.tree.operators.nodes.RootOpN;

public class BinaryTreeMaker {

	public static class Node {
		static int idCounter = 1;
		final public String id;
		final String label;
		final Node left;
		final Node right;
		public Node(String label, Node left, Node right) {
			this.id = "n" + idCounter++;
			this.label = label;
			this.left = left;
			this.right = right;
		}
	}

	public Node createBinaryTree(CommonOpNode root) {
		assert root instanceof RootOpN;
		RootOpN rootOp = (RootOpN)root;
		return addNode(rootOp.right());
	}

	Node addNode(fTree.BsNode n) {
		if (n == null) {
			return null;
		}

		if(n.kind == fTree.fTreeNKind.N_PROD_ROOT_LEAF) {
			assert n.left() == null && n.right() == null;
			return  addNode(((ProdRootLeafN)n).opNode());
		}

		if(n.kind == fTree.fTreeNKind.N_ROOT) {
			assert n.left() == null;
			return addNode(n.right());
		}

		switch (n.kind) {
			case N_ID_OPERATOR:
				return addOp((OperatorNode)n);
			case N_ID_LEAF:
				return addId((IdLeafNode)n);
			case N_ELSE_KW_LEAF:
				return addElse((ElseKwLeafNode)n);
			case N_IF_KW_LEAF:
				return addIf(((IfKwLeafNode)n).val());
			case N_FUN_CALL_LEAF:
				return addFuncCall((FunCallLeafNode)n);
			case N_TYPE_LEAF:
				return addTypeLeaf(((TypeLeafNode)n));
			default:
				throw new UnsupportedOperationException();
		}
	}

	Node addOp(OperatorNode opNode) {
		String label = "OP: " + opNode.val().op_token.name();
		Node left = addNode(opNode.left());
		Node right = addNode(opNode.right());
		return new Node(label, left, right);
	}

	Node addTypeLeaf(TypeLeafNode typeLeaf) {
		String label = "Type: " + typeLeaf.typeName();
		ProdRootLeafN args = typeLeaf.typeArgs();
		Node last = addNode(args);
		return new Node(label, last, null);
	}

	Node addFuncCall(FunCallLeafNode fcLeafNode) {
		String label = "Func: " + fcLeafNode.functionName();
		ProdRootLeafN args = fcLeafNode.funcArgs();
		Node last = addNode(args);
		return new Node(label, last, null);
	}

	Node addId(IdLeafNode idLeafN) {
		String label = idLeafN.varName();
		return new Node(label, null, null);
	}

	Node addElse(ElseKwLeafNode elseLeafN) {
		Node _if = addIf(elseLeafN.ifKwLeafNode().val());
		Node elseBody = addNode(elseLeafN.elseKwLeafNode());
		return new Node("ELSE", _if, elseBody);
	}

	Node addIf(IfKwLeafValue ifKwLeafNode) {
		Node left = addNode(ifKwLeafNode.ifCondLeafN);
		Node right = addNode(ifKwLeafNode.ifBodyLeafN);
		return new Node("IF", left, right);
	}
}
