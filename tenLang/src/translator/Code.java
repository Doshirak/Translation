package translator;

import com.sun.corba.se.spi.orb.Operation;
import parser.Operand;
import parser.OperationNode;
import parser.OperationType;
import variables.Int;

import java.util.ArrayList;

public class Code {
    private int index;
    private ArrayList<Integer> next = new ArrayList<Integer>();
    private OperationNode operation;

    public Code(int index, OperationNode operation) {
        this.index = index;
        this.operation = operation;
    }

    public int getIndex() {
        return index;
    }

    public ArrayList<Integer> getNext() {
        return next;
    }

    public void addNext(int i) {
        next.add(i);
    }

    public OperationNode getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(operation.getType()).append(" ");
        for (Operand opearand : operation.getOperands()) {
            builder.append(opearand).append(" ");
        }
        return builder.toString();
    }

}
