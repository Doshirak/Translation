package lexer;

import reader.Position;

public class Lexem {
    private LexemType type;
    private String value;
    private Position position;

    public Lexem(LexemType type, String value, Position position) {
        this.type = type;
        this.value = value;
        this.position = position;
    }

    @Override
    public String toString() {
        return type + " " + value + " " + position;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof Lexem))return false;
        Lexem b = (Lexem)o;
        return type.equals(b.type) && value.equals(b.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public Position getPosition() {
        return position;
    }

    public LexemType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
