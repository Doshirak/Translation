package tests;

import context.Context;
import variables.Variable;
import grammar.Grammar;
import junit.framework.TestCase;
import lexer.Lexem;
import lexer.Lexer;
import reader.Buffer;
import synanalizer.SynAnalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Test extends TestCase {

    StringBuilder builder;

    @Override
    public void setUp() {
        builder = new StringBuilder();
    }

    @Override
    public void tearDown() {
        System.out.print(builder.toString());
    }

    public void testBuffer() {
        Context context = new Context("int : a");
        Buffer buffer = new Buffer(context);
        while (!buffer.empty())
            buffer.next();
        assertFalse(context.getErrorHandler().hasErrors());
    }

    public void testLexer() {
        builder.append("testLexer\n");
        Context context = new Context("int : a");
        Lexer lexer = new Lexer(context);
        ArrayList<Lexem> lexems = lexer.read();
        assertFalse(context.getErrorHandler().hasErrors());
        for (Lexem lexem : lexems) {
            builder.append(lexem).append("\n");
        }
    }

    public void testLexemsCount() {
        Context context = new Context("int : a");
        Lexer lexer = new Lexer(context);
        assertEquals(lexer.read().size(), 3);
    }

    public void testComment() {
        Context context = new Context("//comment \n a = a / b");
        Lexer lexer = new Lexer(context);
        lexer.read();
        assertFalse(context.getErrorHandler().hasErrors());
    }

    public void testWrongVarName() {
        Context context = new Context("int : a~");
        Lexer lexer = new Lexer(context);
        lexer.read();
        assertTrue(context.getErrorHandler().hasErrors());
    }

    public void testGrammar() {
        Grammar grammar = new Grammar();
        grammar.read("<Text> ::=\n" +
                "\t<Declaration> \"program:\" <ExecCode> ;\n");
    }

    public void testSynAnalizer() throws FileNotFoundException {
        Grammar grammar = new Grammar();
        grammar.read(new File("grammar"));
        Context context = new Context("int : a");
        SynAnalyzer analyzer = new SynAnalyzer(context, grammar);
        analyzer.read();
        assertFalse(context.getErrorHandler().hasErrors());
    }

}
