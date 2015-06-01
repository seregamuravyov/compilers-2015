package optimize;

import java.util.List;

/**
 * Created by sergey on 01.06.15.
 */
public class CodeNode {
    private String type;
    private int labelCounter;
    private List<String> code;

    public CodeNode(String type, List<String> code, int labelCounter){
        this.type = type;
        this.code = code;
        this.labelCounter = labelCounter;
    }

    public void setCode(List<String> code) {
        this.code = code;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLabelCounter(int labelCounter) {
        this.labelCounter = labelCounter;
    }

    public String getType() {
        return type;
    }

    public int getLabelCounter() {
        return labelCounter;
    }

    public List<String> getCode() {
        return code;
    }
}
