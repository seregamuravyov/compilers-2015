import javafx.util.Pair;

import java.util.*;

/**
 * Created by sergey on 07.05.15.
 */
public class VariableStorage {
    public Stack<Map<String, Pair<String, String>>> storage;
    String functionName;
    int shift = 0;

    public VariableStorage(){
        storage = new Stack<>();
        storage.push(new HashMap<String, Pair<String, String>>());
        shift = 0;
        functionName = "global scope";
    }

    public void addGlobalVariable(String name, String type, int size) throws Exception {
        if (!containsVariable(name))
            storage.get(0).put(name, new Pair<>(type, "[" + name + "]"));
        else
            throw new Exception("Redefenition of variable " + name);
    }

    public void addLocalVariable(String name, String type, int size) throws Exception {
        if (!containsVariable(name)) {
            String adress = "[ebp - " + (shift += size) + "]";
            storage.peek().put(name, new Pair<>(type, adress));
        } else {
            throw new Exception("Redefenition of variable " + name);
        }
    }

    public void addFuncArgument(String name, String type, String address) throws Exception {
        if (!containsVariable(name)) {
            storage.peek().put(name, new Pair<>(type, address));
        } else {
            throw new Exception("Redefenition of variable " + name);
        }
    }

    public void enterBlock(String funcName){
        shift = 0;
        storage.push(new HashMap<String, Pair<String, String>>());
        this.functionName = funcName;
    }

    public void exitBlock(){
        storage.pop();
    }

    public boolean containsVariable(String name) {
        for (Map<String, Pair<String, String>> i : storage) {
            if (i.containsKey(name))
                return true;
        }
        return false;
    }

    public String getVariableType(String name) throws Exception {
        for (Map<String, Pair<String, String>> i : storage) {
            if (i.containsKey(name))
                return i.get(name).getKey();
        }
        throw new Exception("No variable with name" +  name + "found in current scope");
    }

    public String getVariableAddress(String name) throws Exception {
        for (Map<String, Pair<String, String>> i : storage) {
            if (i.containsKey(name))
                return i.get(name).getValue();
        }
        throw new Exception("No variable with name" + name + "found in current scope");
    }

    public int getLocalVarStackSize(){
        return shift;
    }
}
