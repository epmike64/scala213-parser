package com.flint.compiler;

import com.flint.compiler.token.fTokenKind;
import com.flint.compiler.token.type.fToken;
import com.flint.compiler.util.ArrayUtils;

public class fScanner implements fLexer {

	private final fTokenizer tokenizer;
	private fToken[] tokens = new fToken[256];
	private int currIndex = -1;
	private int eofIndex = -1;
	private int savedTokensIndex = -1;
	private int tokensSize;

	public fScanner(fTokenizer tokenizer) {
		this.tokenizer = tokenizer;
		scanToken();
	}

	//@Override
	public void savePos() {
		savedTokensIndex = currIndex;
	}


	public void resetPos() {
		currIndex = savedTokensIndex;
	}

	//@Override
	public void backtrack(int n) {
		assert n >= 0 && currIndex - n >= -1;
		currIndex -= n;
	}

	//@Override
	public boolean isEOF() {
		return eofIndex > -1 && currIndex >= eofIndex;
	}

	@Override
	public fToken nextToken()  {
		if(isEOF()){
			return tokens[currIndex];
		}
		if(++ currIndex < tokensSize){
			return tokens[currIndex];
		}
		scanToken();
		return tokens[currIndex];
	}

	private void scanToken() {
		assert !isEOF();
		tokens = ArrayUtils.ensureCapacity(tokens,  tokensSize);
		tokens[tokensSize++] =  tokenizer.readToken();
		int lastIx = tokensSize - 1;
		if(tokens[lastIx].kind == fTokenKind.T_EOF){
			eofIndex = lastIx;
		}
	}

	private void ensureLookahead(int n)  {
		assert n >= 0;
		int ensureSize = currIndex + 1 + n;
		while (tokensSize < ensureSize) {
			if(isEOF()) return;
			scanToken();
		}
	}

	@Override
	public fToken lookAhead(int n)  {
		ensureLookahead(n);
		if(eofIndex > -1 && currIndex + n >= eofIndex){
			return tokens[eofIndex];
		}
		return tokens[currIndex + n];
	}
}
