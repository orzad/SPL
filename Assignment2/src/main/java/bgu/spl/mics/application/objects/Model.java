package bgu.spl.mics.application.objects;


/**
 * Passive object representing a Deep Learning model.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Model {

    public enum Status {
        PreTrained, Training, Trained, Tested
    }

     public enum Result {
        None, Good, Bad
    }

    private String name;
    private Data data;
    private Student student;
    private Status status;
    private Result result;
    private Data.Type type;
    private int size;
    private boolean isPublished;

    public Model(String name, String type, int size){
        this.name = name;
        this.isPublished = false;
        if(type == "Images"){
            this.type = Data.Type.Images;
        }
        else if(type == "Text"){
            this.type = Data.Type.Text;
        }
        else{
            this.type = Data.Type.Tabular;
        }
        this.size=size;
        this.data = new Data(this.type, size);
        this.student = null;
        this.status = Status.PreTrained;
        this.result = Result.None;
    }

    public Model(){

    }

    public boolean isPublished(){
        return isPublished;
    }

    public void published (){
        isPublished=true;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getResult() {
        return result.toString();
    }

    public Data getData() {
        return data;
    }

    public String getStatus() {
        return status.toString();
    }

    public String getName() {
        return name;
    }

    public Student getStudent() {
        return student;
    }

}
