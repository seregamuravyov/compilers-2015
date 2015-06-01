package optimize;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergey on 31.05.15.
 */
public class AddExprNode extends ExprNode {
    public AddExprNode(Node left, Node right) {
        super(left, right);
    }

    @Override
    public Node simplify() {

        left = left.simplify();
        right = right.simplify();

        if (left instanceof IntNode && right instanceof IntNode) {
            Integer a = (Integer)((IntNode) left).getValue();
            Integer b = (Integer)((IntNode) right).getValue();
            return new IntNode(a + b);
        }

        if (left instanceof StringNode && right instanceof StringNode) {
            String a = (String)((StringNode) left).getValue();
            String b = (String)((StringNode) right).getValue();
            return new StringNode(a + b);
        }

        //if (left.equals(right) && (left instanceof IntNode || left instanceof IdNode && ((IdNode)left).getType().Type.INT)) {
        if (left.equals(right) && (left instanceof IntNode)) {
            return new MultExprNode(left, new IntNode(2));
        }

        if (left instanceof IntNode && ((IntNode) left).getValue().equals(0)) {
            return right;
        }

        if (right instanceof IntNode && ((IntNode) right).getValue().equals(0)) {
            return left;
        }

        if (left instanceof StringNode && ((StringNode) left).getValue().equals("")) {
            return right;
        }

        if (right instanceof StringNode && ((StringNode) right).getValue().equals("")) {
            return left;
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
            code.add("pop edx");
            code.add("add" + " edx, ebx");
            code.add("mov eax, edx");
            code.add("push eax");
            return new CodeNode("int", code, add);
        } else {
            if (leftGenerate.getType().equals("string") && rightGenerate.getType().equals("string"))  {
                code.add("push 256");
                code.add("call malloc");
                code.add("add esp, 4");

                code.add("pop ebx");
                code.add("pop edx");

                code.add("push edx");
                code.add("push eax");
                code.add("call strcpy");
                code.add("add esp, 8");

                code.add("push ebx");
                code.add("push eax");
                code.add("call strcat");
                code.add("add esp, 8");

                code.add("push eax");
                return new CodeNode("string", code, add);
            } else {
                throw new IllegalArgumentException("Both values must be integer or strings for concatetaion");
            }
        }
    }
}
