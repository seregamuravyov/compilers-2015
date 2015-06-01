package optimize;

import javafx.util.Pair;

import java.util.ArrayList;
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
    public CodeNode generateCode(Additional add) {
        List<String> code = new ArrayList<>();

        CodeNode leftGenerate = left.generateCode(add);
        add = leftGenerate.getAdd();

        CodeNode rightGenerate = right.generateCode(add);
        add = rightGenerate.getAdd();

        if (leftGenerate.getType().equals("bool") && rightGenerate.getType().equals("bool")) {
            add.setLabelVarCounter(add.getLabelVarCounter() + 4);
            int labelCounter = add.getLabelVarCounter();
            code.add("pop ebx");
            code.add("pop edx");

            code.add("cmp edx, 1");
            code.add("jne L" + labelCounter);
            code.add("cmp ebx, 1");
            code.add("jne L" + labelCounter);
            code.add("mov eax, 1");
            code.add("jmp L" + (labelCounter + 1));

            code.add("L" + (labelCounter) + ":");
            code.add("mov eax, 0");

            code.add("L" + (labelCounter + 1) + ":");
            code.add("push eax");
            add.setLabelVarCounter(add.getLabelVarCounter() + 4);
            return new CodeNode("bool", code, add);

        } else {
            throw new IllegalArgumentException("Both values must be boolean");
        }
    }
}
