package optimize;

import java.util.List;

/**
 * Created by sergey on 01.06.15.
 */
public class Additional {
    private int tmpVarCounter;
    private int labelVarCounter;
    private List<String> dataSection;

    public Additional(int tmpVarCounter, int labelVarCounter, List<String> dataSection){
        this.tmpVarCounter = tmpVarCounter;
        this.labelVarCounter = labelVarCounter;
        this.dataSection = dataSection;
    }

    public void setDataSection(List<String> dataSection) {
        this.dataSection = dataSection;
    }

    public void setLabelVarCounter(int labelVarCounter) {
        this.labelVarCounter = labelVarCounter;
    }

    public void setTmpVarCounter(int tmpVarCounter) {
        this.tmpVarCounter = tmpVarCounter;
    }

    public int getTmpVarCounter() {
        return tmpVarCounter;
    }

    public int getLabelVarCounter() {
        return labelVarCounter;
    }

    public List<String> getDataSection() {
        return dataSection;
    }
}
