package optimize;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergey on 31.05.15.
 */
public class BoolNode implements Node {

    public BoolNode(Boolean value) {
        this.value = value;
    }

    private Boolean value;
    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean isValue() {
        return false;
    }

    @Override
    public Node simplify() {
        return this;
    }

    private String getBooleanValue(Boolean s){
        if (s)
            return "1";
        else {
            if (!s)
                return "0";
            else
                throw new IllegalArgumentException();
        }
    }

    @Override
    public CodeNode generateCode(Additional add) {
        List<String> code = new ArrayList<>();

        code.add("push " + getBooleanValue(value));
        return new CodeNode("bool", code, add);
    }
}
