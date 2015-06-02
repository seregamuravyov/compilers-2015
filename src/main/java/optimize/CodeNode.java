package optimize;

import java.util.List;

/**
 * Created by sergey on 01.06.15.
 */
public class CodeNode {
    private String type;
    private Additional add;
    private List<String> code;

    public CodeNode(String type, List<String> code, Additional add){
        this.type = type;
        this.code = code;
        this.add = add;
    }

    //setters

    public void setCode(List<String> code) {
        this.code = code;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAdd(Additional add) {
        this.add = add;
    }

    //getters

    public String getType() {
        return type;
    }

    public List<String> getCode() {
        return code;
    }

    public Additional getAdd() {
        return add;
    }
}
