package errorhandler;

import java.util.ArrayList;

public class ErrorHandler {
    private ArrayList<Error> errors = new ArrayList<Error>();
    public ErrorHandler(){}
    public void addError(Error error) {
        errors.add(error);
    }
    public void write() {
        for (Error error : errors) {
            System.out.println(error);
        }
    }
}
