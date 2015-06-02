package optimize;

/**
 * Created by sergey on 31.05.15.
 */
public abstract class ExprNode implements Node {
    protected Node left, right;

    public ExprNode(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Object getValue() {
        throw new UnsupportedOperationException("Operation do not have a value");
    }

    @Override
    public boolean isValue() {
        return false;
    }
}
