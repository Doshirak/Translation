package vm;

import context.Context;
import grammar.Grammar;

import java.io.File;
import java.io.FileNotFoundException;

public class TestVM {
    public static void main (String args[]) throws FileNotFoundException {
        Grammar grammar = new Grammar();
        grammar.read(new File("grammar5"));
        Context context = new Context(new File("loopProg"));
        VirtualMachine vm = new VirtualMachine(context, grammar);
        vm.run();
    }
}
