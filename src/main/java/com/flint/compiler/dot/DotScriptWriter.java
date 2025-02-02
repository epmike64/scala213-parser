package com.flint.compiler.dot;
import com.flint.compiler.dot.BinaryTreeMaker.Node;
import com.flint.compiler.tree.operators.nodes.CommonOpNode;

import java.io.FileWriter;

public class DotScriptWriter {

	public String generateDotFile(String filename, CommonOpNode root, String fileContent) {
		BinaryTreeMaker treeMaker= new  BinaryTreeMaker();
		String dotScript = createDotScript(treeMaker.createBinaryTree(root), fileContent);
		//write to file
		try (FileWriter writer = new FileWriter(filename)) {
			writer.write(dotScript);
			System.out.println("DOT file generated successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dotScript;
	}

	public static String createDotScript(Node root, String fileContent) {
		StringBuilder sb = new StringBuilder();
		sb.append("digraph G {\n");
		sb.append("graph [label=\"").append(fileContent).append("\", labelloc=\"t\"").append("];\n");
		sb.append("node [shape=circle];\n");
		sb.append("edge [dir=none];\n");
		writeNode(sb, root);
		sb.append("}\n");
		return sb.toString();
	}
	
	private static void writeNode(StringBuilder sb, Node node){
		if(node.left != null){
			sb.append(node.id).append(" [label=\"").append(node.label).append("\"];\n");
			sb.append(node.left.id).append(" [label=\"").append(node.left.label).append("\"];\n");
			sb.append(node.id).append(" -> ").append(node.left.id).append(";\n");
			writeNode(sb, node.left);
		}
		if(node.right != null){
			sb.append(node.id).append(" [label=\"").append(node.label).append("\"];\n");
			sb.append(node.right.id).append(" [label=\"").append(node.right.label).append("\"];\n");
			sb.append(node.id).append(" -> ").append(node.right.id).append(";\n");
			writeNode(sb, node.right);
		}
	}
}
