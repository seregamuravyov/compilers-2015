package optimize;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergey on 31.05.15.
 */
public class OrExprNode extends ExprNode {
    public OrExprNode(Node left, Node right) {
        super(left, right);
    }

    @Override
    public Node simplify() {
        left = left.simplify();
        right = right.simplify();

        if (left instanceof BoolNode && right instanceof BoolNode) {
            Boolean a = (Boolean) ((BoolNode)left).getValue();
            Boolean b = (Boolean) ((BoolNode)right).getValue();
            return new BoolNode(a || b);
        }

        if (left instanceof BoolNode &&  (Boolean) ((BoolNode) left).getValue()
                || right instanceof BoolNode && (Boolean) ((BoolNode) right).getValue()) {
            return new BoolNode(true);
        }
        return this;
    }

    @Override
    public CodeNode generateCode(int labelCounter) {

        List<String> code = new ArrayList<>();

        CodeNode leftGenerate = left.generateCode(labelCounter);
        labelCounter = leftGenerate.getLabelCounter();

        CodeNode rightGenerate = right.generateCode(labelCounter);
        labelCounter = rightGenerate.getLabelCounter();

        if (leftGenerate.getType().equals("bool") && rightGenerate.getType().equals("bool")) {
            labelCounter += 4;
            code.add("pop ebx");
            code.add("pop edx");
            code.add("cmp edx, 1");
            code.add("je L" + labelCounter);
            code.add("cmp ebx, 1");
            code.add("jne L" + (labelCounter - 1));
            code.add("L" + (labelCounter) + ":");
            code.add("mov eax, 1");
            code.add("jmp L" + (labelCounter + 1));
            code.add("L" + (labelCounter - 1) + ":");
            code.add("mov eax, 0");
            code.add("L" + (labelCounter + 1) + ":");
            labelCounter += 4;
            code.add("push eax");
            return new CodeNode("bool", code, labelCounter);

        } else {
            throw new IllegalArgumentException("Both values must be boolean");
        }
        //return null;
    }
}
