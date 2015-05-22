package translator;

import context.Context;
import parser.OperationNode;
import grammar.Grammar;
import parser.OperationType;
import parser.Parser;
import variables.Int;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

public class Translator {
    private Context context;
    private Grammar grammar;
    private Parser parser;
    private ArrayList<Code> program = new ArrayList<Code>();
    private Stack<Integer> loopStack = new Stack<Integer>();

    public Translator(Context context, Grammar grammar) {
        this.context = context;
        this.grammar = grammar;
        parser = new Parser(context, grammar);
        parser.parse();
    }

    public void translate() {
        Integer index = -1;
        translateOperations(parser.getOperations(), null, index);
    }

    private int translateOperations(OperationNode operation, Code parent, Integer index) {
        Code code = new Code(++index, operation);
        if (operation.getType() == OperationType.loop) {
            loopStack.push(index);
        }
        if (operation.getType() == OperationType.endLoop) {
            code.getNext().add(loopStack.pop());
        }
        if (parent != null) {
            parent.addNext(index);
        }
        program.add(code);
        for (OperationNode child : operation.getChildren()) {
            index = translateOperations(child, code, index);
        }
        return index;
    }

    public void write() {
        for (Code code : program) {
            System.out.print(code.getIndex() + " " + code + " goto ");
            for (Integer next : code.getNext()) {
                System.out.print(next + " ");
            }
            System.out.println();
        }
    }

    public ArrayList<Code> getProgram() {
        return program;
    }

    public ArrayList<String> getIntegers() {
        return parser.getIntegers();
    }
}
