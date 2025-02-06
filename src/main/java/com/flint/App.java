package com.flint;

import com.flint.compiler.dot.DotScriptWriter;
import com.flint.compiler.fParser;
import com.flint.compiler.fReader;
import com.flint.compiler.fScanner;
import com.flint.compiler.fTokenizer;
import com.flint.compiler.tree.leaves.nodes.ProdRootLeafN;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        App app = new App();
        try {
            app.testParser(args[0]);
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private fTokenizer getTokenizer(String filename) {
        char[] cbuf = readFileToCharArray(filename);
        fReader r = new fReader(cbuf, cbuf.length);
        return new fTokenizer(r);
    }

    public void testParser(String filename) {
        String filePath = "src/main/resources/" + filename;
        fTokenizer tknz = getTokenizer(filePath);
        fScanner scanner = new fScanner(tknz);
        fParser parser = new fParser(scanner);
        ProdRootLeafN n = parser.compilationUnit();
        System.out.println("> Done parsing !");
        DotScriptWriter dsw = new DotScriptWriter();
        String dotScript = dsw.generateDotFile(filePath + ".dot", n, readFileToString(filePath));
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
