package reader;

public class Position {
    private int row;
    private int col;

    public Position() {
        this(1, 1);
    }

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Position(Position position) {
        this(position.getRow(), position.getCol());
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void newLine() {
        ++row;
        col = 1;
    }

    public void next() {
        ++col;
    }

    @Override
    public String toString() {
        return "(" + row + ":" + col + ")";
    }
}
