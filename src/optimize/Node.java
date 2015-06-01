package optimize;

import javafx.util.Pair;
import java.util.List;

/**
 * Created by sergey on 31.05.15.
 */
public interface Node {
    Object getValue();

    boolean isValue();

    Node simplify();

    CodeNode generateCode(int labelCounter);
}
