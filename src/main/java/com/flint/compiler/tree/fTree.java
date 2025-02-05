package com.flint.compiler.tree;

public interface fTree {

	interface BsNodeValue extends fTree {
	}

	enum fTreeNKind {
		N_ROOT, N_ID_OPERATOR, N_TEMPLATE_STAT_LEAF, N_CLASS_PARAM_LEAF, N_TYPE_PARAM_LEAF, N_PARAM_LEAF, N_VARIANT_TYPE_PARAM_LEAF, N_FUN_SIG_LEAF, N_CLASS_DEF_LEAF, N_TYPE_DEF_LEAF, N_TRAIT_DEF_LEAF, N_OBJECT_DEF_LEAF, N_IF_KW_LEAF, N_FUN_DCL_LEAF, N_CASE_KW_LEAF, N_PAT_DEF_LEAF, N_CONSTR_PATTERN_LEAF, N_GUARD_LEAF, N_PROD_ROOT_LEAF, N_ELSE_KW_LEAF, N_ID_LEAF, N_FUN_CALL_LEAF, N_CASE_LEAF, N_TYPE_LEAF, N_COMMA, N_FAT_ARROW, N_LPAREN, N_LCURL /*N_RPAREN*/
	}

	/**
	 * Base Node for the tree
	 */
	abstract class BsNode {
		public BsNode parent;
		protected BsNode left;
		protected BsNode right;
		protected BsNodeValue value;
		public final fTreeNKind kind;

		public BsNode(BsNode parent, fTreeNKind kind, BsNode left, BsNode right, BsNodeValue value) {
			this.parent = parent;
			this.left = left;
			this.right = right;
			this.value = value;
			this.kind = kind;
		}

		public BsNodeValue val() {
			return value;
		}

		@Override
		public String toString() {
			return value.toString();
		}

		public abstract void setLeft(BsNode left);

		public abstract void setRight(BsNode right);

		public abstract BsNode left();

		public abstract BsNode right();
	}
}
