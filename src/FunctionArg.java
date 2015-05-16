/**
 * Created by sergey on 12.05.15.
 */
public class FunctionArg {
//    private String argName;
    private String argType;
    private int argSize;
    private String argAdress;

    public FunctionArg(String argType, int argSize, String argAdress){
        //this.argName = argName;
        this.argType = argType;
        this.argSize = argSize;
        this.argAdress = argAdress;
    }

    //public String getArgName() { return argName; }

    public String getArgType() {
        return argType;
    }

    public int getArgSize() {
        return argSize;
    }

    public String getArgAdress() {
        return argAdress;
    }
}
