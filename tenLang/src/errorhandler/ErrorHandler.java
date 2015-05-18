package errorhandler;

import reader.Position;

import java.util.ArrayList;

public class ErrorHandler {
    private ArrayList<Error> errors = new ArrayList<Error>();
    public ErrorHandler(){}
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
    public void addError(String s) {
        errors.add(new Error(s));
    }
    public void addError(Error error) {
        errors.add(error);
    }
    public void write() {
        for (Error error : errors) {
            System.out.println(error);
        }
    }
    public ArrayList<Error> getErrors() {
        return errors;
    }

    public void addError(String s, Position position) {
        errors.add(new Error(s, position));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Error error : errors) {
            builder.append(error.toString() + "\n");
        }
        return builder.toString();
    }
}
