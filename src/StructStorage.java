import javafx.util.Pair;

import java.util.*;
/**
 * Created by sergey on 12.05.15.
 */
public class StructStorage {
    String structType;

    int structSize = 0;
    //<fieldName, fieldType>
    Map<String, String> fields;
    List<Pair<String, String>> fieldsLst;

    public StructStorage(){
        fields = new HashMap<>();
        fieldsLst = new ArrayList<>();
    }

    public String getStructType(){
        return structType;
    }

    public boolean containsArg(String fieldName) {
        return fields.containsKey(fieldName);
    }

    public void addField(String fieldName, String fieldType, int size){
        if (containsArg(fieldName)) {
            throw new IllegalArgumentException("Redefinition of field " + fieldName);
        }
        fields.put(fieldName, fieldType);
        fieldsLst.add(new Pair<>(fieldName, fieldType));
        structSize += size;
    }

    public String getFieldType(String fieldName) throws Exception {
        if (containsArg(fieldName)) {
            return fields.get(fieldName);
        }
        throw new Exception("No such argument in function");
    }

    public String getFieldType(int i){
        return fieldsLst.get(i).getValue();
    }

    public String getFieldName(int i){
        return fieldsLst.get(i).getKey();
    }

    public int getStructSize(){
        return structSize;
    }

}
