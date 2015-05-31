package optimize;

import javafx.util.Pair;

import java.util.List;

/**
 * Created by sergey on 31.05.15.
 */
public class IdNode implements Node  {
    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public boolean isValue() {
        return false;
    }

    @Override
    public Node simplify() {
        return null;
    }

    @Override
    public Pair<int[], Pair<String, List<String>>> generateCode(int varCounter, int helpCounter, int constCounter, List<String> constants) {
        return null;
    }
}
