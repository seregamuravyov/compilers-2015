package optimize;

import javafx.util.Pair;

import java.util.List;

/**
 * Created by sergey on 31.05.15.
 */
public class AndExprNode extends ExprNode {

    public AndExprNode(Node left, Node right) {
        super(left, right);
    }

    @Override
    public Node simplify() {

        left = left.simplify();
        right = right.simplify();

        if (left instanceof BoolNode && right instanceof BoolNode) {
            Boolean a = (Boolean)((BoolNode) left).getValue();
            Boolean b = (Boolean)((BoolNode) right).getValue();
            return new BoolNode(a && b);
        }

        if (left instanceof BoolNode && !(Boolean)((BoolNode) left).getValue()
                || right instanceof BoolNode && !(Boolean)((BoolNode) right).getValue()) {
            return new BoolNode(false);
        }

        return this;
    }

    @Override
    public Pair<int[], Pair<String, List<String>>> generateCode(int varCounter, int helpCounter, int constCounter, List<String> constants) {
        return null;
    }
}
