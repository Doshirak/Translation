package parser;

import synanalizer.SynNode;

import java.util.ArrayList;
import java.util.Map;

public class OperationNode {
    private SynNode synNode;
    private ArrayList<OperationNode> children = new ArrayList<OperationNode>();
    private OperationType type;
    private ArrayList<Operand> operands = new ArrayList<Operand>();

    public OperationNode(SynNode synNode, OperationType type) {
        this.synNode = synNode;
        this.type = type;
    }

    public OperationNode(OperationType type) {
        this.type = type;
    }

    public void addChild(OperationNode node) {
        children.add(node);
    }

    public void addOperand(Operand operand) {
        operands.add(operand);
    }

    public void addOperand(String name) {
        operands.add(new Operand(name));
    }

    public void addOperand(int value) {
        operands.add(new Operand(value));
    }

    public boolean isTest() {
        return (type == OperationType.testEqual ||
                type == OperationType.testLess ||
                type == OperationType.testBigger ||
                type == OperationType.testEqualLess ||
                type == OperationType.testEqualBigger);
    }

    public ArrayList<OperationNode> getChildren() {
        return children;
    }

    public OperationType getType() {
        return type;
    }

    public ArrayList<Operand> getOperands() {
        return operands;
    }

    private int getValue(Operand operand, Map<String, Integer> variables) {
        if (operand.isValue()) {
            return operand.getValue();
        } else {
            return variables.get(operand.getName());
        }
    }

    public int exec(Map<String, Integer> variables) {
        if (type == OperationType.pass) {
            return 0;
        }
        if (operands.size() > 0) {
            String resultName = operands.get(0).getName();
            int result = 0;
            if (type == OperationType.assign) {
                Operand second = operands.get(1);
                result = getValue(second, variables);
            } else if (type == OperationType.sum) {
                Operand second = operands.get(1);
                Operand third = operands.get(2);
                result = getValue(second, variables) + getValue(third, variables);
            } else if (type == OperationType.sub) {
                Operand second = operands.get(1);
                Operand third = operands.get(2);
                result = getValue(second, variables) - getValue(third, variables);
            } else if (type == OperationType.mul) {
                Operand second = operands.get(1);
                Operand third = operands.get(2);
                result = getValue(second, variables) * getValue(third, variables);
            } else if (type == OperationType.div) {
                Operand second = operands.get(1);
                Operand third = operands.get(2);
                result = getValue(second, variables) / getValue(third, variables);
            } else if (type == OperationType.testEqual) {
                Operand second = operands.get(1);
                Operand third = operands.get(2);
                result = (getValue(second, variables) == getValue(third, variables)) ? 1 : 0;
            } else if (type == OperationType.testBigger) {
                Operand second = operands.get(1);
                Operand third = operands.get(2);
                result = (getValue(second, variables) > getValue(third, variables)) ? 1 : 0;
            } else if (type == OperationType.testLess) {
                Operand second = operands.get(1);
                Operand third = operands.get(2);
                result = (getValue(second, variables) < getValue(third, variables)) ? 1 : 0;
            } else if (type == OperationType.testEqualBigger) {
                Operand second = operands.get(1);
                Operand third = operands.get(2);
                result = (getValue(second, variables) >= getValue(third, variables)) ? 1 : 0;
            } else if (type == OperationType.testEqualLess) {
                Operand second = operands.get(1);
                Operand third = operands.get(2);
                result = (getValue(second, variables) <= getValue(third, variables)) ? 1 : 0;
            }
            variables.put(resultName, result);
            return result;
        } else {
//            if (type == OperationType.loop) {
//
//            } else if (type == OperationType.endLoop) {
//
//            }
            return 1;
        }
    }
}
