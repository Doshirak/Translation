package parser;

import com.sun.corba.se.spi.orb.Operation;
import context.Context;
import grammar.Grammar;
import synanalizer.SynAnalyzer;
import synanalizer.SynNode;
import variables.VarType;
import variables.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    private Context context;
    private SynAnalyzer analyzer;
    private Map<String, Variable> varNames = new HashMap<String, Variable>();
    private ArrayList<String> variables = new ArrayList<String>();
    private int regUsed = 0;
    private int regSize = 0;
    private OperationNode operations;
    private ArrayList<String> unary = new ArrayList<String>();
    private ArrayList<String> binary = new ArrayList<String>();

    public Parser(Context context, Grammar grammar) {
        this.context = context;
        analyzer = new SynAnalyzer(context, grammar);
        analyzer.read();
        unary.add("<Term>");
        unary.add("<Factor>");
        unary.add("<Power>");
        unary.add("<Atom>");
        binary.add("<Sum>");
        binary.add("<Sub>");
        binary.add("<Mul>");
        binary.add("<Div>");
        binary.add("<Power>");
    }

    public void parse() {
        parseNode(analyzer.getHead());
    }

    public void addVar(Variable variable) {
        Variable old = varNames.get(variable.getName());
        if (old != null) {
            if (variable.getType() != null) {
                if (!old.equals(variable))
                    context.getErrorHandler().addError("variable '" + variable.getName() + "' has different types");
            }
        } else {
            varNames.put(variable.getName(), variable);
            if (variable.getType() == VarType.Int) {
                variables.add(variable.getName());
            }
        }
    }

    Operand getRegister() {
        regUsed++;
        String name = "reg" + regUsed;
        // add exception if reg var name is used
        if (regUsed > regSize) {
            variables.add(name);
            regSize++;
        }
        return new Operand(name);
    }

    void freeReg() {
        regUsed--;
    }

    private void parseNode(SynNode node) {
        if (node.getValue().equals("<Declaration>")) {
            parseVariable(node);
        } else if (node.getValue().equals("<ExecCode>")) {
            operations = new OperationNode(node, OperationType.pass);
            parseOperations(node.getChildren().get(0), operations);
        } else {
            for (SynNode child : node.getChildren()) {
                parseNode(child);
            }
        }
    }

    private void parseVariable(SynNode node) {
        SynNode first = node.getChildren().get(0);
        if (first.getValue().equals("var")) {
            String varName = node.getChildren().get(1).getValue();
            SynNode type = node.getChildren().get(3);
            VarType varType = null;
            if (type.getChildren().get(0).getValue().equals("int")) {
                varType = VarType.Int;
            } else if (type.getChildren().get(0).getValue().equals("array")) {
                varType = VarType.Array;
            }
            Variable variable = new Variable(varName, varType);
            addVar(variable);
        }
    }

    private OperationNode parseOperations(SynNode synNode, OperationNode operation) {
        if (synNode.getValue().equals("<ExecCode>")) {
            return parseOperations(synNode.getChildren().get(0), operation);
        } else if (synNode.getValue().equals("<ConsecCode>")) {
            SynNode firstSyn = synNode.getChildren().get(1);
            SynNode secondSyn = synNode.getChildren().get(2);
            OperationNode first = parseOperations(firstSyn, operation);
            parseOperations(secondSyn, first);
            return first;
        } else if (synNode.getValue().equals("<ParallelCode>")) {
            SynNode firstSyn = synNode.getChildren().get(5);
            SynNode secondSyn = synNode.getChildren().get(6);
            parseOperations(firstSyn, operation);
            parseOperations(secondSyn, operation);
        } else if (synNode.getValue().equals("<LoopCode>")) {
            OperationNode childOp = new OperationNode(synNode, OperationType.loop);
            operation.addChild(childOp);
            SynNode firstSyn = synNode.getChildren().get(2);
            OperationNode leave = parseOperations(firstSyn, childOp);
            OperationNode endLoop = new OperationNode(OperationType.endLoop);
            leave.addChild(endLoop);
        } else if (synNode.getValue().equals("<Body>")) {
            return parseBody(synNode.getChildren().get(0), operation);
        }
        return null;
    }

    private OperationNode parseBody(SynNode node, OperationNode operation) {
        if (node.getValue().equals("<Assignment>")) {
            OperationNode childOp = new OperationNode(node, OperationType.assign);
            SynNode varSyn = node.getChildren().get(0);
            SynNode exprNode = node.getChildren().get(2);
            childOp.addOperand(varSyn.getValue());
            OperationNode nextChildOp = parseExpr(exprNode, operation);
            childOp.addOperand(nextChildOp.getOperands().get(0));
            nextChildOp.addChild(childOp);
            return childOp;
        } else if (node.getValue().equals("<Test>")) {
            OperationNode childOp = new OperationNode(node, OperationType.test);
            operation.addChild(childOp);
            return childOp;
        } else if (node.getValue().equals("<Operator>")) {
            OperationNode childOp = new OperationNode(node, OperationType.operator);
            operation.addChild(childOp);
            return childOp;
        }
        return null;
    }

    private OperationNode parseExpr(SynNode node, OperationNode parent) {
        SynNode child = node.getChildren().get(0);
        if (unary.contains(child.getValue())) {
            return parseExpr(child, parent);
        }
        if (binary.contains(child.getValue())) {
            SynNode arg1 = child.getChildren().get(1);
            SynNode arg2 = child.getChildren().get(2);
            OperationNode first = parseExpr(arg1, parent);
            OperationNode second = parseExpr(arg2, first);
            OperationNode operation = null;
            if (child.getValue().equals("<Sum>")) {
                operation = new OperationNode(OperationType.sum);
            }
            if (child.getValue().equals("<Sub>")) {
                operation = new OperationNode(OperationType.sub);
            }
            if (child.getValue().equals("<Mul>")) {
                operation = new OperationNode(OperationType.mul);
            }
            if (child.getValue().equals("<Div>")) {
                operation = new OperationNode(OperationType.div);
            }
            if (child.getValue().equals("<Power>")) {
                operation = new OperationNode(OperationType.pow);
            }
            operation.addOperand(getRegister());
            operation.addOperand(first.getOperands().get(0));
            operation.addOperand(second.getOperands().get(0));
            second.addChild(operation);
            freeReg();
            freeReg();
            return operation;
        }
        if (child.isTerm()) {
            OperationNode childOp = new OperationNode(child, OperationType.assign);
            childOp.addOperand(getRegister());
            if (child.getTerm().variable()) {
                childOp.addOperand(child.getValue());
            } else {
                childOp.addOperand(Integer.parseInt(child.getValue()));
            }
            parent.addChild(childOp);
            return childOp;
        }
        return null;
    }

    public void writeOperations() {
        writeNode(operations, 0);
    }

    private void writeNode(OperationNode oper, int h) {
        for (int i = 0; i < h; ++i) {
            System.out.print("-");
        }
        System.out.print(oper.getType() + " ");
        for (Operand operand : oper.getOperands()) {
            System.out.print(operand + " ");
        }
        System.out.println();
        for (OperationNode child : oper.getChildren()) {
                writeNode(child, h + 1);
        }
    }

    public OperationNode getOperations() {
        return operations;
    }

    public Map<String, Variable> getVariables() {
        return varNames;
    }

    public ArrayList<String> getIntegers() {
        return variables;
    }
}
