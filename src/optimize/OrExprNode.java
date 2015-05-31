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
    public Pair<int[], Pair<String, List<String>>> generateCode(int varCounter, int helpCounter, int constCounter, List<String> constants) {

        List<String> code = new ArrayList<>();

        Pair<int[], Pair<String, List<String>>> leftGenerate = left.generateCode(varCounter, helpCounter, constCounter, constants);
        varCounter = leftGenerate.getKey()[1];

        Pair<int[], Pair<String, List<String>>> rightGenerate = right.generateCode(varCounter, helpCounter, constCounter, constants);
        varCounter = rightGenerate.getKey()[1];

        if (leftGenerate.getValue().getKey().equals("bool") && rightGenerate.getValue().getKey().equals("bool")) {
            code.add("pop ebx");
            code.add("pop edx");
            code.add("cmp edx, 1");
            //code.add("je L" + localLabelCounter);
            code.add("cmp ebx, 1");
            //code.add("jne L" + (localLabelCounter - 1));
            //code.add("L" + (localLabelCounter) + ":");
            code.add("mov eax, 1");
            //code.add("jmp L" + (localLabelCounter + 1));
            //code.add("L" + (localLabelCounter - 1) + ":");
            code.add("mov eax, 0");
            //code.add("L" + (localLabelCounter + 1) + ":");
//            labelCounter += 4;
//            andExpr.getValue().add("push eax");
//            andExpr = new Pair<>("bool", andExpr.getValue());

        } else {
            throw new IllegalArgumentException("Both values must be boolean");
        }
        return null;
    }
}
