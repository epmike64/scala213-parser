package com.flint.compiler;

import com.flint.compiler.token.type.fToken;

public interface fLexer {
	fToken nextToken() ;
	fToken lookAhead(int n) ;
}
