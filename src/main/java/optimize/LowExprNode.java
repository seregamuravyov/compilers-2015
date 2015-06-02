package optimize;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergey on 31.05.15.
 */
public class LowExprNode extends ExprNode {
    public LowExprNode(Node left, Node right) {
        super(left, right);
    }

    @Override
    public Node simplify() {
        left = left.simplify();
        right = right.simplify();

        if (left instanceof IntNode && right instanceof IntNode) {
            Integer a = (Integer)((IntNode) left).getValue();
            Integer b = (Integer)((IntNode) right).getValue();
            return new BoolNode(a < b);
        }

        if (left instanceof StringNode && right instanceof StringNode) {
            Boolean a = (Boolean)((BoolNode) left).getValue();
            Boolean b = (Boolean)((BoolNode) right).getValue();
            return new BoolNode((a? 1 : 0)  < (b? 1 : 0));
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

        int labelCounter;

        if ((leftGenerate.getType().equals(rightGenerate.getType())) &&
                (leftGenerate.getType().equals("int") || leftGenerate.getType().equals("bool"))) {
            add.setLabelVarCounter(add.getLabelVarCounter() + 4);
            labelCounter = add.getLabelVarCounter();
            code.add("pop ebx");
            code.add("pop edx");

            code.add("cmp edx, ebx");
            code.add("jb " + " L" + labelCounter);
            code.add("mov eax, 0");
            code.add("jmp L" + (labelCounter + 1));

            code.add("L" + (labelCounter) + ":");
            code.add("mov eax, 1");

            code.add("L" + (labelCounter + 1) + ":");
            code.add("push eax");
            add.setLabelVarCounter(add.getLabelVarCounter() + 4);
            return new CodeNode("bool", code, add);
        } else {
            throw new IllegalArgumentException("Both values must be integer or boolean");
        }
    }
}
