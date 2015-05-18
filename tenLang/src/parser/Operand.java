package parser;

public class Operand {
    private boolean hasValue;
    private String name;
    private int value;

    public Operand(String name) {
        this.name = name;
        hasValue = false;
    }

    public Operand(int value) {
        this.value = value;
        hasValue = true;
    }

    public boolean isValue() {
        return hasValue;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (name == null) {
            return Integer.toString(value);
        } else {
            return name;
        }
    }
}
