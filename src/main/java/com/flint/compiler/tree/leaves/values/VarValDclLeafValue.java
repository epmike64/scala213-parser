package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.fVariable;
import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

import java.util.ArrayList;
import java.util.List;

public class VarValDclLeafValue extends TokenLeafValue{
	public fVariable.StoreType storeType;
	public List<String> ids;
	public ProdRootLeafN typeLeafN;
	public VarValDclLeafValue(fToken token) {
		  super(token);
	 }
}
