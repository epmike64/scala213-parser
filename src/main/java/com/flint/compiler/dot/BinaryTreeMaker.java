package com.flint.compiler.dot;

import com.flint.compiler.tree.fTree;
import com.flint.compiler.tree.leaves.nodes.*;
import com.flint.compiler.tree.leaves.values.IfKwLeafValue;
import com.flint.compiler.tree.operators.nodes.OperatorNode;

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

	public Node createBinaryTree(ProdRootLeafN root) {
		return addNode(root.opNode());
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
			case N_CLASS_DEF_LEAF:
				return addClassDef((ClassDefLeafNode)n);
			case N_CLASS_PARENTS_LEAF:
				return addClassParents((ClassParentsLeafNode)n);
			case N_CLASS_PARAM_LEAF:
				return addClassParam((ClassParamLeafNode)n);
				case N_VARIANT_TYPE_PARAM_LEAF:
				return addVariantTypeParam((VariantTypeParamLeafNode)n);
			case N_IMPORT_LEAF:
				return addImport((ImportDefLeafNode)n);
			case N_PAT_DEF_LEAF:
				return addPatDef((PatDefLeafNode)n);
			case N_TYPE_PARAM_LEAF:
				return addTypeParam((TypeParamLeafNode)n);
			case N_MODIFIER_LEAF:
				return addModifier((ModifierLeafNode)n);
			case N_PARAM_LEAF:
				return addParam((ParamLeafNode)n);
			case N_FUN_SIG_LEAF:
				return addFunSig((FunSigLeafNode)n);
			case N_TYPE_DEF_DCL_LEAF:
				return addTypeDef((TypeDefDclLeafNode)n);
			case N_TRAIT_DEF_LEAF:
				return addTraitDef((TraitDefLeafNode)n);
			case N_OBJECT_DEF_LEAF:
				return addObjectDef((ObjectDefLeafNode)n);
			case N_FUN_DCL_LEAF:
				return addFunDcl((FunDclDefLeafNode)n);
			case N_CASE_KW_LEAF:
				return addCaseKw((CaseKwLeafNode)n);
			case N_ID_PAREN_WRAP_PATTERNS_LEAF:
				return addConstrPattern((IdParenWrapPatternsLeafNode)n);
			case N_STABLE_ID_LEAF:
				return addStableId((StableIdLeafNode)n);
			case N_IMPORT_EXPR_LEAF:
				return addImportExpr((ImportExprLeafNode)n);
				case N_IMPORT_SELECTOR_LEAF:
				return addImportSelector((ImportSelectorLeafNode)n);
			default:
				throw new UnsupportedOperationException();
		}
	}

	Node addImportSelector(ImportSelectorLeafNode n) {
		String label = "ImportSelector: " + n.val().rename;
		return new Node(label, null, null);
	}

	Node addImportExpr(ImportExprLeafNode n) {
		String label = "ImportExpr: ";
		for(String id : n.val().ids) {
			label += id + ".";
		}
		Node left = addNode(n.val().importSelectorsLeafN);
		return new Node(label, left, null);
	}
	Node addStableId(StableIdLeafNode n) {
		String label = "StableId: " + n.val().token.name();
		if(n.val().classQualifier != null) {
			label += " ClassQualifier: " + n.val().classQualifier;
		}
		return new Node(label, null, null);
	}

	Node addConstrPattern(IdParenWrapPatternsLeafNode n) {

		Node left = addNode(n.val().patternsLeafN);
		Node right = null;//addNode(n.val().exprLeafN);
		return new Node("Constr Pattern", left, right);
	}

	Node addCaseKw(CaseKwLeafNode caseKwLeafNode) {
		Node left = addNode(caseKwLeafNode.val().patternLeafN);
		Node right = null;//addNode(caseKwLeafNode.val().exprLeafN);
		return new Node("Case", left, right);
	}

	Node addFunDcl(FunDclDefLeafNode funDclLeafNode) {
		Node left = addNode(funDclLeafNode.val().funSigLeafN);
		Node right = null;//addNode(funDclLeafNode.val().exprLeafN);
		return new Node("FunDcl", left, right);
	}

	Node addTraitDef(TraitDefLeafNode traitDefLeafNode) {
		Node left = null;//addNode(traitDefLeafNode.val().typeParamsLeafN);
		Node right = addNode(traitDefLeafNode.val().templateBodyLeafN);
		return new Node("TraitDef", left, right);
	}

	Node addObjectDef(ObjectDefLeafNode n) {
		String label = "ObjectDef: " + n.val().objectName;
		Node left = null;//addNode(n.val().objectNameLeafN);
		return new Node(label, left, null);
	}

	Node addTypeDef(TypeDefDclLeafNode n) {
		String label = "TypeDef: " + n.val().typeName;
		Node left = addNode(n.val().variantTypeParamsLeafN);
		Node right = addNode(n.val().defTypeLeafN);
		return new Node(label, left, right);
	}

	Node addFunSig(FunSigLeafNode funSigLeafNode) {
		String label = "FunSig: " + funSigLeafNode.val().funName;
		Node left = addNode(funSigLeafNode.val().typeParamsLeafN);
		Node right = addNode(funSigLeafNode.val().paramsLeafN);
		return new Node(label, left, right);
	}

	Node addModifier(ModifierLeafNode n) {
		String label = "Modifier: ";// + n.val().modifier;
		return new Node(label, null, null);
	}

	Node addParam(ParamLeafNode n) {
		String label = "Param: " + n.val().paramName;
		Node left = addNode(n.val().paramTypeLeafN);
		Node right = addNode(n.val().exprLeafN);
		return new Node(label, left, right);
	}

	Node addTypeParam(TypeParamLeafNode n) {
		String label = "TypeParam: " + n.val().typeParamName;
		Node left = addNode(n.val().variantTypeParamsLeafN);
		return new Node(label, left, null);
	}




	Node addPatDef(PatDefLeafNode patDefLeafNode) {
		Node left = addNode(patDefLeafNode.val().pattern2sLeafN);
		Node right = addNode(patDefLeafNode.val().typeLeafN);
		Node n1 = new Node("Pattern2 / Type", left, right);
		left = addNode(patDefLeafNode.val().exprLeafN);
		Node n2 = new Node("PatDef Expr", left, null);
		return new Node("PatDef of VAL", n1, n2);
	}

	Node addImport(ImportDefLeafNode importLeafNode) {
		String label = "Import: ";
		Node left = addNode(importLeafNode.val().importExprsLeafN);
		return new Node(label, left, null);
	}

	Node addVariantTypeParam(VariantTypeParamLeafNode n) {
		String label = "VariantTypeParam: " + n.val().getVariant().name();
		Node left = addNode(n.val().typeParamLeafN);
		return new Node(label, left, null);
	}

	Node addClassParam(ClassParamLeafNode n) {
		String label = "ClassParam: " + n.val().paramName;
		Node left = addNode(n.val().paramTypeLeafN);
		Node right = addNode(n.val().exprLeafN);
		return new Node(label, left, right);
	}

	Node addOp(OperatorNode opNode) {
		String label = "OP=<" + opNode.val().op_token.name() + ">";
		Node left = addNode(opNode.left());
		Node right = addNode(opNode.right());
		return new Node(label, left, right);
	}

	Node addClassParents(ClassParentsLeafNode classParentsLeaf) {
		String label = "Class Parents";
		ProdRootLeafN constrType = classParentsLeaf.val().constrType;
		ProdRootLeafN constrArgsExpr = classParentsLeaf.val().constrArgExprs;
		return new Node(label, null, null);
	}

	Node addClassDef(ClassDefLeafNode n) {
		String label = "Class: " + n.val().className + "\nCase: " + n.val().isCase;

		Node l1 = addNode(n.val().typeParamsLeafN);
		Node l2 = addNode(n.val().classParamsLeafN);
		Node n1 = new Node("Class Prop1",  l1,  l2);

		l1 = addNode(n.val().classParentsLeafN);
		l2 = addNode(n.val().templateBodyLeafN);

		Node n2 = new Node("Class Prop2", l1, l2);
		return new Node(label, n1, n2);
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
