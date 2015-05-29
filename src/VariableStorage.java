import javafx.util.Pair;

import java.util.*;

/**
 * Created by sergey on 07.05.15.
 */
public class VariableStorage {
    //public Stack<Map<String, Pair<String, String>>> storage;
    public Stack<Map<String, VariableDescription>> storage;
    String functionName;
    int shift = 0;
    int blockCount = 0;

    public VariableStorage(){
        storage = new Stack<>();
        //storage.push(new HashMap<String, Pair<String, String>>());
        storage.push(new HashMap<String, VariableDescription>());
        shift = 0;

        functionName = "global_scope";
    }

    public void addGlobalVariable(String name, String type, int size) throws Exception {
        if (!containsVariable(name)){
//            if (!(type.equals("int") || type.equals("string") || type.equals("bool")))
//                storage.get(0).put((name + "_" + functionName + "_" + getBlockCount()),
//                        new VariableDescription(type, "[" + name + "_" + functionName + "_" + getBlockCount() + "]", true));
//            else
//                storage.get(0).put(name, new VariableDescription(type, "[" + name + "]", true));

            //заменить на
            storage.get(0).put(name, new VariableDescription(type, "[" + name + "]", true));
        }
        else
            throw new Exception("Redefenition of variable " + name);
    }

    public void addLocalVariable(String name, String type, int size) throws Exception {
        if (!containsVariable(name)) {
            String adress = "[ebp - " + (shift += size) + "]";
            //storage.peek().put(name, new Pair<>(type, adress));
            storage.peek().put(name, new VariableDescription(type, adress, false));
        } else {
            throw new Exception("Redefenition of variable " + name);
        }
    }

    public void addFuncArgument(String name, String type, String address) throws Exception {
        if (!containsVariable(name)) {
            ///
//            if (!(type.equals("int") || type.equals("bool") || type.equals("string"))){
//                storage.get(0).put(getStructName(name), new VariableDescription(type, address, false));
//            } else {
//                storage.peek().put(name, new VariableDescription(type, address, false));
//            }
            ///заменить на
            storage.peek().put(name, new VariableDescription(type, address, false));
        } else {
            throw new Exception("Redefenition of variable " + name);
        }
    }

    public void enterBlock(String funcName){
        shift = 0;
        //storage.push(new HashMap<String, Pair<String, String>>());
        storage.push(new HashMap<String, VariableDescription>());
        this.functionName = funcName;
        blockCount++;
    }

    public void exitBlock(){
        storage.pop();
    }

    public boolean containsVariable(String name) {
        for (Map<String, VariableDescription> i : storage) {
            if (i.containsKey(name))
                return true;
        }
        ///убрать
//        if (storage.get(0).containsKey(name + "_" + functionName + "_" + blockCount))
//            return true;
        /////
        return false;
    }

    public String getVariableType(String name) throws Exception {
        for (Map<String, VariableDescription> i : storage) {
            if (i.containsKey(name))
                return i.get(name).getType();
        }

        ///убрать
//        name = name + "_" + functionName + "_" + blockCount;
//        if (storage.get(0).containsKey(name))
//            return storage.get(0).get(name).getType();
        ///

        throw new Exception("No variable with name: " +  name + " found in current scope");
    }

    public String getVariableAddress(String name) throws Exception {
        for (Map<String, VariableDescription> i : storage) {
            if (i.containsKey(name))
                return i.get(name).getAdress();
        }

        //убрать
//        name = name + "_" + functionName + "_" + blockCount;
//        if (storage.get(0).containsKey(name))
//            return storage.get(0).get(name).getAdress();
        /////

        throw new Exception("No variable with name " + name + "found in current scope");
    }

    public int getLocalVarStackSize(){
        return shift;
    }

    public List<String> garbageCollector(){
        List<String> code = new ArrayList<>();
        for (VariableDescription i : storage.peek().values()) {
            if (containsVariable(i.getType()) && (!(i.getType().equals("bool") || i.getType().equals("int")))
                    && i.getType().startsWith("[ebp -")) {
                code.add("pusha");
                code.add("push dword " + i.getAdress());
                code.add("call free");
                code.add("add esp, 4");
                code.add("popa");
            }
        }
        return code;
    }

    public void setAllocated(String name){
        for (Map<String, VariableDescription> i : storage) {
            if (i.containsKey(name))
                i.get(name).setIsAllocated(true);
        }
    }

    public boolean isAllocated(String name) throws Exception {
        for (Map<String, VariableDescription> i : storage) {
            if (i.containsKey(name))
                return i.get(name).getIsAllocated();
        }
        throw new Exception("No variable with name: " +  name + " found in current scope");
    }

    public boolean isGlobal(String name) {
        return storage.get(0).containsKey(name);
    }
}
