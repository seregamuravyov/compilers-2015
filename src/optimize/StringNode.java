package optimize;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergey on 31.05.15.
 */
public class StringNode implements Node {

    public StringNode(String value) {
        this.value = value;
    }

    private String value;
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
        if (value.length() > 255) {
            value = value.substring(0, 255);
        }
        int tmpVarCounter;
        List<String> dataSection = null;

        add.setTmpVarCounter(add.getLabelVarCounter() + 1);
        tmpVarCounter = add.getTmpVarCounter();
        dataSection = add.getDataSection();
        String var = ("tmp" + tmpVarCounter);
        try {
            dataSection.add(var + ": dd \"" +  value + "\", 0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        code.add("push " + var);

        add.setTmpVarCounter(tmpVarCounter);
        add.setDataSection(dataSection);
        return new CodeNode("string", code, add);
    }
}
