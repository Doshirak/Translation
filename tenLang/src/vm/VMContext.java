package vm;

import parser.OperationNode;
import parser.OperationType;
import translator.Code;
import translator.Translator;
import variables.Int;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VMContext {
    private ArrayList<Code> program;
    private Code code;
    private Map<String, Integer> variables = new HashMap<String, Integer>();
    private ArrayList<VMContext> contexts;
    private boolean nextCode = true;

    public VMContext(Translator translator, ArrayList<VMContext> contexts) {
        this.contexts = contexts;
        code = translator.getProgram().get(0);
        program = translator.getProgram();
        for (String name : translator.getIntegers()) {
            variables.put(name, 0);
        }
    }

    public VMContext(VMContext vmContext, Integer codeIndex) {
        program = vmContext.program;
        code = program.get(codeIndex);
        for (Map.Entry<String, Integer> entry : vmContext.variables.entrySet()) {
            variables.put(entry.getKey(), entry.getValue());
        }
        contexts = vmContext.contexts;
        nextCode = true;
    }

    //
    public void run() {
        OperationNode operation = code.getOperation();
        int result = operation.exec(variables);
        if (operation.isTest()) {
            if (result == 0) {
                nextCode = false;
                return;
            }
        }
        if (code.getNext().size() > 0) {
            for (int i = 1;i < code.getNext().size();++i) {
                VMContext context = new VMContext(this, code.getNext().get(i));
                contexts.add(context);
            }
            int next = code.getNext().get(0);
            code = program.get(next);
        } else {
            nextCode = false;
        }
    }

    public boolean hasNextCode() {
        return nextCode;
    }

    public Map<String, Integer> getVariables() {
        return variables;
    }
}
