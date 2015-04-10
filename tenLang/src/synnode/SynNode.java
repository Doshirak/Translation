package synnode;
import errorhandler.ErrorHandler;
import grammar.NotTerm;
import grammar.Rule;
import lexer.Lexem;
import java.util.ArrayList;

public class SynNode {
    private NotTerm notTerm;
    private ArrayList<SynNode> children = new ArrayList<SynNode>();
    private ErrorHandler errorHandler;
    public SynNode(NotTerm notTerm) {
        this.notTerm = notTerm;
    }
    public SynNode(NotTerm notTerm, ErrorHandler errorHandler) {
        this.notTerm = notTerm;
        this.errorHandler = errorHandler;
    }
    public void read(ArrayList<Lexem> lexems) {
        for (Rule rule : notTerm.getTermRules()) {
            if (rule.parse(lexems, children)) {
                return;
            }
        }
        for (Rule rule : notTerm.getNoTermRules()) {
            if (rule.parse(lexems, children)) {
                return;
            }
        }
    }
    public ArrayList<SynNode> getChildren() {
        return children;
    }
    public NotTerm getNotTerm() {
        return notTerm;
    }
}
