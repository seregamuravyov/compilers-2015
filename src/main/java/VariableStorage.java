import javafx.util.Pair;

import java.util.*;

/**
 * Created by sergey on 07.05.15.
 */
public class VariableStorage {
    public Stack<Map<String, VariableDescription>> storage;
    private Map<String, StructStorage> strStorage;
    String functionName;
    int shift = 0;
    int blockCount = 0;

    public void setStructStorage(Map<String, StructStorage> strStorage){
        this.strStorage = strStorage;
    }

    public VariableStorage(){
        storage = new Stack<>();
        storage.push(new HashMap<String, VariableDescription>());
        shift = 0;

        functionName = "global_scope";
    }

    public void addGlobalVariable(String name, String type, int size) throws Exception {
        if (!containsVariable(name)){
            storage.get(0).put(name, new VariableDescription(type, "[" + name + "]", false));
        }
        else
            throw new Exception("Redefenition of variable " + name);
    }

    public void addLocalVariable(String name, String type, int size) throws Exception {
        if (!containsVariable(name)) {
            String adress = "[ebp - " + (shift += size) + "]";
            storage.peek().put(name, new VariableDescription(type, adress, false));
        } else {
            throw new Exception("Redefenition of variable " + name);
        }
    }

    public void addFuncArgument(String name, String type, String address) throws Exception {
        if (!containsVariable(name)) {
            storage.peek().put(name, new VariableDescription(type, address, false));
        } else {
            throw new Exception("Redefenition of variable " + name);
        }
    }

    public void enterBlock(String funcName){
        shift = 0;
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
        return false;
    }

    public String getVariableType(String name) throws Exception {
        for (Map<String, VariableDescription> i : storage) {
            if (i.containsKey(name))
                return i.get(name).getType();
        }
        throw new Exception("No variable with name: " +  name + " found in current scope");
    }

    public String getVariableAddress(String name) throws Exception {
        for (Map<String, VariableDescription> i : storage) {
            if (i.containsKey(name))
                return i.get(name).getAdress();
        }
        throw new Exception("No variable with name " + name + "found in current scope");
    }

    public int getLocalVarStackSize(){
        return shift;
    }

    private List<String> cleanStructuresByFields(String type, String sourse, String sourseAdress, List<String> code, Boolean assignFlag){
        StructStorage structStr = strStorage.get(type);
        if (assignFlag) {
            for (Pair<String, String> j : structStr.getFieldsLst()) {
                if (j.getValue().equals("string")){
                    code.add("mov ecx, " + sourseAdress);
                    code.add(sourse + " + " + type  + "." + j.getKey() + "]");
                    code.add("pusha");
                    code.add("push edx");
                    code.add("call free");
                    code.add("add esp, 4");
                    code.add("popa");

                } else {
                    if (!(j.getValue().equals("int") || j.getValue().equals("bool"))) {
                        List<String> assignCode = new ArrayList<>();
                        code.addAll(cleanStructuresByFields(j.getValue(), sourse + " + " + type + "." + j.getKey(),
                                sourseAdress, assignCode, assignFlag));
                    }
                }
            }
            return code;
        } else {
            throw new UnsupportedOperationException("Can't clear uninitialized struct!");
        }
    }

    public List<String> garbageCollector(){
        List<String> code = new ArrayList<>();
        for (VariableDescription i : storage.peek().values()) {
            if (!(i.getType().equals("bool")) && !(i.getType().equals("int"))
                    && i.getAdress().startsWith("[ebp -") && i.getIsAllocated()) {
                if (i.getType().equals("string")) {
                    code.add("pusha");
                    code.add("push dword " + i.getAdress());
                    code.add("call free");
                    code.add("add esp, 4");
                    code.add("popa");
                } else {

                    List<String> local = new ArrayList<>();
                    code.addAll(cleanStructuresByFields(i.getType(), "mov edx, [ecx ", i.getAdress(), local, i.getIsAllocated()));

                    code.add("pusha");
                    code.add("push dword " + i.getAdress());
                    code.add("call free");
                    code.add("add esp, 4");
                    code.add("popa");
                }
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
