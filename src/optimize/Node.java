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

    Pair<int[], Pair<String, List<String>>> generateCode(int varCounter, int helpCounter, int constCounter, List<String> constants);
}
