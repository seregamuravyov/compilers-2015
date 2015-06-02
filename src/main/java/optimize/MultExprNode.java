package optimize;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergey on 31.05.15.
 */
public class MultExprNode extends ExprNode {
    public MultExprNode(Node left, Node right) {
        super(left, right);
    }

    @Override
    public Node simplify() {
        left = left.simplify();
        right = right.simplify();

        if (left instanceof IntNode && right instanceof IntNode) {
            Integer a = (Integer)((IntNode) left).getValue();
            Integer b = (Integer)((IntNode) right).getValue();
            return new IntNode(a * b);
        }

        if (left instanceof IntNode && ((IntNode) left).getValue().equals(0)) {
            return new IntNode(0);
        }

        if (right instanceof IntNode && ((IntNode) right).getValue().equals(0)) {
            return new IntNode(0);
        }

        return this;
    }

    @Override
    public CodeNode generateCode(Additional add) {

        List<String> code = new ArrayList<>();

        CodeNode leftGenerate = left.generateCode(add);
        add = leftGenerate.getAdd();

        CodeNode rightGenerate = right.generateCode(add);
        add = rightGenerate.getAdd();

        if(leftGenerate.getType().equals("int") && rightGenerate.getType().equals("int")){
            code.add("pop ebx");
            code.add("pop eax");

            code.add("imul ebx");

            code.add("push eax");
            return new CodeNode("int", code, add);
        } else {
            throw new IllegalArgumentException("Both values must be integer");
        }
    }
}
