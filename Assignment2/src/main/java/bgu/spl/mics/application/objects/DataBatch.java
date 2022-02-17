package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class DataBatch {
    Data data;
    int startIndex;
    String modelName;

    public DataBatch(Data data, int startIndex, String modelName){
        this.data = data;
        this.startIndex = startIndex;
        this.modelName = modelName;
    }

    public Data getData() {
        return data;
    }

    public String getModelName() {
        return modelName;
    }
}
