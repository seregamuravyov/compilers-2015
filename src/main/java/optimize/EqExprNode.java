package optimize;

import javafx.util.Pair;

import java.util.ArrayList;
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
            code.add("je" + " L" + labelCounter);
            code.add("mov eax, 0");
            code.add("jmp L" + (labelCounter + 1));

            code.add("L" + (labelCounter) + ":");
            code.add("mov eax, 1");

            code.add("L" + (labelCounter + 1) + ":");
            code.add("push eax");
            add.setLabelVarCounter(add.getLabelVarCounter() + 4);
            return new CodeNode("bool", code, add);

        } else {
            if (leftGenerate.getType().equals("string") && rightGenerate.getType().equals("string")) {
                add.setLabelVarCounter(add.getLabelVarCounter() + 4);
                labelCounter = add.getLabelVarCounter();
                code.add("call strcmp");
                code.add("add esp, 8");
                code.add("mov ebx, eax");
                code.add("cmp ebx, 0");
                code.add("je " + "L" + labelCounter);
                code.add("mov eax, 0");
                code.add("jmp L" + (labelCounter + 1));

                code.add("L" + (labelCounter) + ":");
                code.add("mov eax, 1");

                code.add("L" + (labelCounter + 1) + ":");
                code.add("push eax");
                add.setLabelVarCounter(add.getLabelVarCounter() + 4);
                return new CodeNode("bool", code, add);
            } else
                throw new IllegalArgumentException("Both values must be the same type");
        }
    }
}
