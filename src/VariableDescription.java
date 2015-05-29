/**
 * Created by sergey on 26.05.15.
 */
public class VariableDescription {
    private String type;
    private String adress;
    private Boolean isAllocated;

    public VariableDescription(String type, String adress, Boolean isInitialized){
        this.type = type;
        this.adress = adress;
        this.isAllocated = isInitialized;
    }

    public String getType() {
        return type;
    }

    public String getAdress() {
        return adress;
    }

    public Boolean getIsAllocated() {
        return isAllocated;
    }

    public void setIsAllocated(Boolean isAllocated) {
        this.isAllocated = isAllocated;
    }
}
