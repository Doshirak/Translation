package variables;

import java.util.ArrayList;

public class Variable {
    protected String name;
    protected VarType type;
    private ArrayList<Variable> array;

    public Variable(String varName, VarType varType) {
        name = varName;
        type = varType;
        if (type == VarType.Array) {
            array = new ArrayList<Variable>();
        }
    }

    public String getName() {
        return name;
    }
    public VarType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof Variable)) {
            return false;
        }
        Variable a = (Variable) o;
        return (a.getName().equals(name) && a.getType().equals(type));
    }
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
