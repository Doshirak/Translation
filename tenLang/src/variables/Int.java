package variables;

public class Int {
    private String name;
    private int value;

    public Int(int value) {
        this.value = value;
    }

    public Int(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
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
