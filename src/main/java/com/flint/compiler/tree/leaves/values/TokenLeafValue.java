package com.flint.compiler.tree.leaves.values;

import com.flint.compiler.token.type.fToken;
import com.flint.compiler.tree.fTree;

public class TokenLeafValue implements fTree.BsNodeValue {
	final public fToken token;

	public TokenLeafValue(fToken token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return ""+token.kind+"{'"+ token.name() +"',pos=" + token.pos +'}';
	}

	public fToken token() {
		return token;
	}
}
