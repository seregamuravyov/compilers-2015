import optimize.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergey on 31.05.15.
 */
public class VisitTreeNode {
    private Node node;
    private String type;
    private List<String> code;

    public VisitTreeNode(String type, List<String> code, Node node) {
        this.type = type;

        this.code = new ArrayList<>();
        if (code != null)
            this.code.addAll(code);
        else
            this.code = null;

        this.node = node;
    }


    public void setCode(List<String> code) {
        this.code = code;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Node getNode() {
        return node;
    }

    public List<String> getCode() {
        return code;
    }
}
