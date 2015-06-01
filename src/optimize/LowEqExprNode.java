package optimize;

import javafx.util.Pair;

import java.util.List;

/**
 * Created by sergey on 31.05.15.
 */
public class LowEqExprNode extends ExprNode {
    public LowEqExprNode(Node left, Node right) {
        super(left, right);
    }

    @Override
    public Node simplify() {
        return null;
    }

    @Override
    public CodeNode generateCode(int labelCounter) {
        return null;
    }
}
