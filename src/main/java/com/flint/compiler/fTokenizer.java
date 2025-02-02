package com.flint.compiler;

import com.flint.compiler.token.fTokenKind;
import com.flint.compiler.token.fTokenMap;
import com.flint.compiler.token.type.NamedToken;
import com.flint.compiler.token.type.NumericToken;
import com.flint.compiler.token.type.StringToken;
import com.flint.compiler.token.type.fToken;

import static com.flint.compiler.util.LayoutCharacters.*;

public class fTokenizer {

	final fReader reader;
	protected int radix;

	/**
	 * The token's name, set by nextToken().
	 */
	protected String tname;
	fTokenKind tk;

	/**
	 * The position where a lexical error occurred;
	 */
	protected int errPos = -1;

	public fTokenizer(fReader reader) {
		this.reader = reader;
	}

	fToken readToken() {
		reader.sp = 0;
		tname = null;
		radix = 0;

		int pos = 0;
		int endPos = 0;

		loop:
		while (true) {

			assert reader.sp == 0 && tname == null && radix == 0;
			pos = reader.bp;

			switch (reader.ch) {
				case EOI:
					tk = fTokenKind.T_EOF;
					break loop;
				case SP:
				case TAB:
				case FF:
					do {
						reader.scanChar();
					} while (reader.ch == SP || reader.ch == TAB || reader.ch == FF);
					continue;

				case CR:
					reader.scanChar();
					continue;

				case LF:
					reader.scanChar(); tk = fTokenKind.T_NL; break loop;

				case ',':
					reader.scanChar(); tk = fTokenKind.T_COMMA; break loop;
				case ';':
					reader.scanChar(); tk = fTokenKind.T_SEMI; break loop;
				case '(':
					reader.scanChar(); tk = fTokenKind.T_LPAREN; break loop;
				case ')':
					reader.scanChar(); tk = fTokenKind.T_RPAREN; break loop;
				case '[':
					reader.scanChar(); tk = fTokenKind.T_LBRACKET; break loop;
				case ']':
					reader.scanChar(); tk = fTokenKind.T_RBRACKET; break loop;
				case '{':
					reader.scanChar(); tk = fTokenKind.T_LCURL; break loop;
				case '}':
					reader.scanChar(); tk = fTokenKind.T_RCURL; break loop;

				case 's':
					if(reader.peekChar() == '"'){
						reader.scanChar();
						pos = reader.bp;
						scanLiteralString(pos, true);
						break loop;
					}
				case 'A': case 'B': case 'C': case 'D': case 'E':
				case 'F': case 'G': case 'H': case 'I': case 'J':
				case 'K': case 'L': case 'M': case 'N': case 'O':
				case 'P': case 'Q': case 'R': case 'S': case 'T':
				case 'U': case 'V': case 'W': case 'X': case 'Y':
				case 'Z':
				case 'a': case 'b': case 'c': case 'd': case 'e':
				case 'f': case 'g': case 'h': case 'i': case 'j':
				case 'k': case 'l': case 'm': case 'n': case 'o':
				case 'p': case 'q': case 'r':  case 't':
				case 'u': case 'v': case 'w': case 'x': case 'y':
				case 'z':
				case '$': case '_':
					scanIdent(pos);
					break loop;
				default:

					if (isOpChar(reader.ch)) {
						scanIdent(pos);
						break loop;
					}

					throw new RuntimeException("illegal.char");
			}

		}
		endPos = reader.bp;
		switch (tk.tag) {
			case DEFAULT: return new fToken(tk, pos, endPos);
			case NAMED: return new NamedToken(tk, pos, endPos, tname);
			case STRING: return new StringToken(tk, pos, endPos, tname);
			case NUMERIC: return new NumericToken(tk, pos, endPos, tname, radix);
			default: throw new AssertionError();
		}
	}

	private void scanLiteralString(int pos, boolean isInterpolated)  {
		reader.scanChar();
		while (reader.ch != '\"' && reader.ch != CR && reader.ch != LF && reader.bp < reader.buflen)
			scanLitChar(pos);
		if (reader.ch == '\"') {
			reader.scanChar();
			tname = reader.name();
			tk = fTokenKind.T_STR_LIT;
		} else {
			throw new RuntimeException("unclosed.str.lit");
		}
	}

	private void scanLitChar(int pos)  {
		if (reader.ch == '\\') {
			if (reader.peekChar() == '\\') {
				reader.skipChar();
				reader.putChar('\\', true);
			} else {
				reader.scanChar();
				switch (reader.ch) {
					case '0': case '1': case '2': case '3':
					case '4': case '5': case '6': case '7':
						char leadch = reader.ch;
						int oct = reader.digit(pos, 8);
						reader.scanChar();
						if ('0' <= reader.ch && reader.ch <= '7') {
							oct = oct * 8 + reader.digit(pos, 8);
							reader.scanChar();
							if (leadch <= '3' && '0' <= reader.ch && reader.ch <= '7') {
								oct = oct * 8 + reader.digit(pos, 8);
								reader.scanChar();
							}
						}
						reader.putChar((char) oct);
						break;
					case 'b':
						reader.putChar('\b', true); break;
					case 't':
						reader.putChar('\t', true); break;
					case 'n':
						reader.putChar('\n', true); break;
					case 'f':
						reader.putChar('\f', true); break;
					case 'r':
						reader.putChar('\r', true); break;
					case '\'':
						reader.putChar('\'', true); break;
					case '\"':
						reader.putChar('\"', true); break;
					case '\\':
						reader.putChar('\\', true); break;
					default:
						throw new RuntimeException("illegal.esc.char");
				}
			}
		} else if (reader.bp != reader.buflen) {
			reader.putChar(true);
		}
	}

	private void scanDigits(int pos, int digitRadix)  {
		do {
			reader.putChar(true);
		} while (reader.digit(pos, digitRadix) >= 0);
	}

	private boolean isOpChar(char ch) {
		switch (ch) {
			case '!': case '#': case '%': case '&': case '*': case '+': case '-': case '/': case ':':
			case '<': case '=': case '>': case '?': case '@': case '\\': case '^': case '|': case '~':
				return true;
			default:
				return false;
		}
	}

	private void scanIdent(int pos)  {

		boolean seenOpChar = false;
		if (isOpChar(reader.ch)) {
			seenOpChar = true;
		}

		loop:
		while (true) {
			char prev = reader.ch;
			reader.putChar(true);

			switch (reader.ch) {
				case 'A': case 'B': case 'C': case 'D': case 'E':
				case 'F': case 'G': case 'H': case 'I': case 'J':
				case 'K': case 'L': case 'M': case 'N': case 'O':
				case 'P': case 'Q': case 'R': case 'S': case 'T':
				case 'U': case 'V': case 'W': case 'X': case 'Y':
				case 'Z': case '$':
				case 'a': case 'b': case 'c': case 'd': case 'e':
				case 'f': case 'g': case 'h': case 'i': case 'j':
				case 'k': case 'l': case 'm': case 'n': case 'o':
				case 'p': case 'q': case 'r': case 's': case 't':
				case 'u': case 'v': case 'w': case 'x': case 'y':
				case 'z': case '_':
				case '0': case '1': case '2': case '3': case '4':
				case '5': case '6': case '7': case '8': case '9':
					if (seenOpChar) {
						break loop;
					}
					continue;

				default:
					if (isOpChar(reader.ch)) {
						if (prev == '_') {
							seenOpChar = true;
							continue;
						} else if(seenOpChar){
							continue;
						}
					}
					break loop;
			}
		}

		tname = reader.name();
		tk = fTokenMap.lookupKind(tname);
	}

}
