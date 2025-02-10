package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

import java.util.ArrayList;
import java.util.List;

public class GeneratorLeafValue extends TokenLeafValue{
	public boolean isCase;
	public ProdRootLeafN leftArgPattern1LeafN;
	public ProdRootLeafN rightArgExprLeafN;
	public final List<GeneratorExprAfter> generatorExprAfterList = new ArrayList<>();

	public static class GeneratorExprAfter {
		public ProdRootLeafN guard;
		public ProdRootLeafN pattern1LeafN;
		public ProdRootLeafN exprLeafN;
	}

	public GeneratorLeafValue(fToken kw_token) {
		super(kw_token);
	}
}
