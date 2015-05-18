package vm;

import context.Context;
import grammar.Grammar;
import translator.Code;
import translator.Translator;
import variables.Int;

import java.util.ArrayList;
import java.util.Map;

public class VirtualMachine {
    private Context context;
    private Translator translator;
    private ArrayList<VMContext> contexts = new ArrayList<VMContext>();

    public VirtualMachine(Context context, Grammar grammar) {
        this.context = context;
        translator = new Translator(context, grammar);
        translator.translate();
        contexts.add(new VMContext(translator, contexts));
    }

    public void run() {
        int execCount = 0;
        do {
            execCount = 0;
            int contextCount = contexts.size();
            for (int i = 0;i < contextCount; ++i) {
                VMContext context = contexts.get(i);
                if (context.hasNextCode()) {
                    context.run();
                    ++execCount;
                }
            }
        } while(execCount > 0);
        for (int i = 0;i < contexts.size();++i) {
            VMContext context = contexts.get(i);
            System.out.println("context " + i + " :");
            for (Map.Entry<String, Integer> entry : context.getVariables().entrySet()) {
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
        }
    }
}
