package com.flint.compiler.lang;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class LangCheck {

	// Set of Scala reserved keywords
	private static final Set<String> SCALA_KEYWORDS = new HashSet<>(Arrays.asList(
			"abstract", "case", "catch", "class", "def", "do", "else", "extends", "false",
			"final", "finally", "for", "forSome", "if", "implicit", "import", "lazy",
			"match", "new", "null", "object", "override", "package", "private", "protected",
			"return", "sealed", "super", "this", "throw", "trait", "true", "try", "type",
			"val", "var", "while", "with", "yield"
	));

	// Regex patterns for Scala method names
	private static final Pattern VALID_ALPHA_METHOD = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");
	private static final Pattern VALID_SYMBOLIC_METHOD = Pattern.compile("^[!#%&*+/:<=>?@\\^|~-]+$");
	private static final Pattern VALID_BACKTICK_METHOD = Pattern.compile("^`[^`]+`$");

	public static boolean isValidScalaMethodName(String methodName) {
		if (methodName == null || methodName.isEmpty()) {
			return false;  // Empty or null names are invalid
		}

		// Check if the method name is a reserved keyword (unless enclosed in backticks)
		if (SCALA_KEYWORDS.contains(methodName)) {
			return false;
		}

		// Check if the method name is an alphanumeric name
		if (VALID_ALPHA_METHOD.matcher(methodName).matches()) {
			return true;
		}

		// Check if the method name is a valid symbolic operator
		if (VALID_SYMBOLIC_METHOD.matcher(methodName).matches()) {
			return true;
		}

		// Check if the method name is enclosed in backticks (valid for any name)
		if (VALID_BACKTICK_METHOD.matcher(methodName).matches()) {
			return true;
		}

		// Check if the name starts with a number (invalid)
		if (Character.isDigit(methodName.charAt(0))) {
			return false;
		}

		// Invalid if it contains spaces and is not enclosed in backticks
		if (methodName.contains(" ") && !methodName.startsWith("`")) {
			return false;
		}

		// Invalid if it contains a dot (.)
		if (methodName.contains(".")) {
			return false;
		}

		return false; // If none of the valid cases matched, it's invalid
	}

	/*public static void main(String[] args) {
		// Test cases
		String[] testCases = {
				"validMethod", "_validMethod123", "123Invalid", "def", "my.method", "space method",
				"`space method`", "+", "++", "==", "<=", "::", ":=", "=", "`class`"
		};

		for (String testCase : testCases) {
			System.out.printf("Method Name: %-15s | Valid: %b%n", testCase, isValidScalaMethodName(testCase));
		}
	}*/
}
