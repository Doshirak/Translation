package errorhandler;

import reader.Position;

public class Error {
    private String message;
    private Position position;
    public Error(String message) {
        this.message = message;
        this.position = null;
    }
    public Error(String message, Position position) {
        this.message = message;
        this.position = new Position(position);
    }
    @Override
    public String toString() {
        if (position == null) {
            return "error : " + message;
        } else {
            return "error : " + message + " at " + position;
        }
    }
}
