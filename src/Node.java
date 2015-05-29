import javafx.util.Pair;

import java.util.List;

/**
 * Created by sergey on 21.05.15.
 */
public class Node {
    private Pair<String, List<String>> code;

    private String valueString;
    private int valueInt;
    private boolean valueBool;

    public Node(Pair<String, List<String>> code) {
        this.code = code;
    }

    public void setResult(String expr) {
        if(code.getKey().equals("string")){
            valueString = expr;
        }
        if(code.getKey().equals("int")){
            valueInt = Integer.parseInt(expr);
        }
        if(code.getKey().equals("bool")) {
            //valueBool = Integer.parseInt(expr);
        }
    }

}
