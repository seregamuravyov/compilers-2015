package optimize;

import javafx.util.Pair;

import java.util.List;

/**
 * Created by sergey on 31.05.15.
 */
public class EqExprNode extends ExprNode {
    public EqExprNode(Node left, Node right) {
        super(left, right);
    }

    @Override
    public Node simplify() {

        left = left.simplify();
        right = right.simplify();

        if (left instanceof IntNode && right instanceof IntNode) {
            Integer a = (Integer) ((IntNode) left).getValue();
            Integer b = (Integer) ((IntNode) right).getValue();
            return new BoolNode(a.equals(b));
        }

        if (left instanceof StringNode && right instanceof StringNode) {
            String a = (String)((StringNode) left).getValue();
            String b = (String) ((StringNode) right).getValue();
            return new BoolNode(a.equals(b));
        }

        if (left instanceof BoolNode && right instanceof BoolNode) {
            Boolean a = (Boolean) ((BoolNode) left).getValue();
            Boolean b = (Boolean) ((BoolNode) right).getValue();
            return new BoolNode(a.booleanValue() == b.booleanValue());
        }

        return this;
    }

    @Override
    public CodeNode generateCode(int lableCounter) {
        return null;
    }
}
