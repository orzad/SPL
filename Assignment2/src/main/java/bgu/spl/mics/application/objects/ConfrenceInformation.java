package bgu.spl.mics.application.objects;

/**
 * Passive object representing information on a conference.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class ConfrenceInformation {

    private String name;
    private int date;
    private int fdate;

    public ConfrenceInformation(String name, int date){
        this.name = name;
        this.date = date;
        this.fdate = 0;
    }

    public void setFdate(int fdate) {
        this.fdate = fdate;
    }

    public int getFdate() {
        return fdate;
    }



    public String getName(){return name;}
    public int getDate(){return date;}
}
