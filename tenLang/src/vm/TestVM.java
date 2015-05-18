package vm;

import context.Context;
import grammar.Grammar;

import java.io.File;
import java.io.FileNotFoundException;

public class TestVM {
    public static void main (String args[]) throws FileNotFoundException {
        Grammar grammar = new Grammar();
        grammar.read(new File("grammar3"));
        Context context = new Context(new File("parallelProg"));
        VirtualMachine vm = new VirtualMachine(context, grammar);
        vm.run();
    }
}
