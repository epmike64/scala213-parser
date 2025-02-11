package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

import java.util.ArrayList;
import java.util.List;

public class ImportExprLeafValue extends TokenLeafValue {
	public ProdRootLeafN importSelectorsLeafN;
	public final List<String> ids = new ArrayList<>();
	public ImportExprLeafValue(fToken token) {
		super(token);
	}
}
