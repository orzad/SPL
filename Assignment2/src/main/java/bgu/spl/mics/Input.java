package bgu.spl.mics;

//import com.sun.org.apache.xpath.internal.operations.String;
import bgu.spl.mics.application.objects.ConfrenceInformation;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Student;

public class Input {
    private Student[] Students;
    private String[] GPUS;
    private int [] CPUS;
    private ConfrenceInformation[] Conferences;
    private long TickTime;
    private Long Duration;

    public String[] getGPUS() {
        return GPUS;
    }

    public int[] getCPUS() {
        return CPUS;
    }

    public ConfrenceInformation[] getConferences() {
        return Conferences;
    }

    public long getTickTime() {
        return TickTime;
    }

    public Long getDuration() {
        return Duration;
    }

    public Student [] getStudents(){
        return Students;
    }

}
