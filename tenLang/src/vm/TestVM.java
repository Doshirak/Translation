package vm;

import context.Context;
import grammar.Grammar;

import java.io.File;
import java.io.FileNotFoundException;

public class TestVM {
    public static void main (String args[]) throws FileNotFoundException {
        Grammar grammar = new Grammar();
        grammar.read(new File("grammar4"));
        Context context = new Context(new File("assignProg"));
        VirtualMachine vm = new VirtualMachine(context, grammar);
        vm.run();
    }
}
