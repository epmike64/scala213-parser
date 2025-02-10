package com.flint.compiler.tree;

public interface fTree {

	interface BsNodeValue extends fTree {
	}

	enum fTreeNKind {
		N_ROOT, N_ID_OPERATOR,
		N_CLASS_PARENTS_LEAF,
		N_MODIFIER_LEAF,
		N_IMPORT_LEAF,
		N_CLASS_PARAM_LEAF,
		N_TYPE_PARAM_LEAF,
		N_PARAM_LEAF,
		N_VARIANT_TYPE_PARAM_LEAF,
		N_FUN_SIG_LEAF,
		N_CLASS_DEF_LEAF,
		N_TYPE_DEF_DCL_LEAF,
		N_TRAIT_DEF_LEAF,
		N_OBJECT_DEF_LEAF,
		N_IF_KW_LEAF,
		N_FUN_DCL_LEAF,
		N_CASE_KW_LEAF,
		N_PAT_DEF_LEAF,
		N_ID_PAREN_WRAP_PATTERNS_LEAF,
		N_PROD_ROOT_LEAF,
		N_ELSE_KW_LEAF,
		N_ID_LEAF,
		N_FUN_CALL_LEAF,
		N_CASE_LEAF,
		N_TYPE_LEAF,
		N_FAT_ARROW,

		N_VAR_VAL_DCL_LEAF,
		N_ID_TYPED_LEAF,
		N_ID_AT_PATTERN3_LEAF,
		N_PAREN_WRAP_PATTERNS_LEAF,
		N_DO_KW_LEAF,
		N_WHILE_KW_LEAF,
		N_FOR_KW_LEAF,
		N_RETURN_KW_LEAF,
		N_THROW_KW_LEAF,
		N_TRY_KW_LEAF,
		N_GENERATOR_LEAF
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
