package com.flint;

import com.flint.compiler.dot.DotScriptWriter;
import com.flint.compiler.fParser;
import com.flint.compiler.fReader;
import com.flint.compiler.fScanner;
import com.flint.compiler.fTokenizer;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;
import com.flint.compiler.tree.operators.nodes.CommonOpNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Hello world!
 *
 */
public class App 
{
    final String parseTest001 = "src/main/resources/ParseTest001.txt";
    public static void main(String[] args) {
        App app = new App();
        try {
            //app.testTokenizer();
            app.testParser();
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private fTokenizer getTokenizer() {
        char[] cbuf = readFileToCharArray(parseTest001);
        fReader r = new fReader(cbuf, cbuf.length);
        return new fTokenizer(r);
    }

    public void testParser() {
        fTokenizer tknz = getTokenizer();
        fScanner scanner = new fScanner(tknz);
        fParser parser = new fParser(scanner);
        ProdRootLeafN n = parser.compilationUnit();
        System.out.println("> Done parsing !");
        //parser.printTreePostOrder(n, 1);
        DotScriptWriter dsw = new DotScriptWriter();
        String dotScript = dsw.generateDotFile("src/main/resources/ParseTest001.dot", n, readFileToString(parseTest001));
         System.out.println("dotScript>>>");
         System.out.println(dotScript);
    }

    public static String readFileToString(String filePath) {
         try {
               // Read all bytes from the file and convert them to a String
               return new String(Files.readAllBytes(Paths.get(filePath)));
         } catch (IOException e) {
             System.err.println("An error occurred while reading the file: " + e.getMessage());
             return null;
         }
    }
    public static char[] readFileToCharArray(String filePath) {
        try {
            // Read all bytes from the file and convert them to a String
            String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
            System.out.println(fileContent);
            // Convert the String to a char array
            return fileContent.toCharArray();
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
            return null;
        }
    }
}
