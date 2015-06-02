import javafx.util.Pair;
import java.util.*;

/**
 * Created by sergey on 12.05.15.
 */
public class FunctionStorage {

    String returnType;
    int shift;

    //args <name, type>
    Map<String, FunctionArg> args;
    List<Pair<String, FunctionArg>> argsLst;

    public FunctionStorage(){
        argsLst = new ArrayList<>();
        args = new HashMap<>();
        shift = 4;
    }

    public void addArgument(String argName, String argType, int argSize){
        if (containsArg(argName)) {
            throw new IllegalArgumentException("Redefinition of argument " + argName);
        }
        String adress = "[ebp + " + (shift += argSize) + "]";
        FunctionArg so = new FunctionArg(argType, argSize, adress);
        args.put(argName, so);
        argsLst.add(new Pair<>(argName, so));
    }

    public boolean containsArg(String argName) {
        return args.containsKey(argName);
    }

    public String getArgType(String argName) throws Exception {
        if (args.containsKey(argName)) {
            return args.get(argName).getArgType();
        }
        throw new Exception("No such argument in function");
    }

    public String getArgType(int i) {
        return argsLst.get(i).getValue().getArgType();
    }

    public int getArgListSize() {
        return argsLst.size();
    }

    public String getArgAdress(int i) {
        return argsLst.get(i).getValue().getArgAdress();
    }

    public String getArgAdress(String argName) throws Exception {
        if (args.containsKey(argName)) {
            return args.get(argName).getArgAdress();
        }
        throw new Exception("No such argument in function");
    }

    public void setReturnType(String type){
        this.returnType = type;
    }

    public String getReturnType () {
        return returnType;
    }

}
