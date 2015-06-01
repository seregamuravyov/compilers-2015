package optimize;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergey on 31.05.15.
 */
public class IntNode implements Node {

    public IntNode(Integer value) {
        this.value = value;
    }

    private Integer value;

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean isValue() {
        return true;
    }

    @Override
    public Node simplify() {
        return this;
    }

    @Override
    public CodeNode generateCode(Additional add) {
        List<String> code = new ArrayList<>();

        code.add("push " + value);
        return new CodeNode("int", code, add);
    }
}
