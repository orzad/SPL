package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.*;
import com.google.gson.*;
import bgu.spl.mics.Input;
import bgu.spl.mics.application.services.CPUService;
import bgu.spl.mics.application.services.ConferenceService;
import bgu.spl.mics.application.services.StudentService;
import bgu.spl.mics.application.services.GPUService;
import bgu.spl.mics.application.services.TimeService;
import java.io.*;
import java.util.Vector;


/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {

    public static void main(String[] args) {

        Vector<Thread> Threads = new Vector<>();
        Vector<Student> students= new Vector<>();
        Vector<ConferenceService> conferenceServices=new Vector<>();
        Vector<GPU> gpus = new Vector<>();
        Vector<CPU> cpus = new Vector<>();

        ModelDeserializer modelDeserializer = new ModelDeserializer();
        Gson gson = new GsonBuilder().registerTypeAdapter(Model.class , modelDeserializer).create();
        Reader reader = null;
        try {
            reader = new FileReader(args[0]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        Input input = gson.fromJson(reader, Input.class);

        for (int i=0; i<input.getGPUS().length;i++){
            GPU gpu = new GPU(input.getGPUS()[i]);
            gpus.add(gpu);
            GPUService service = new GPUService("gpu"+i,gpu);
            Thread thread = new Thread(service);
            Threads.add(thread);
            thread.start();
        }

        for (int i=0; i<input.getCPUS().length;i++){
            CPU cpu = new CPU(input.getCPUS()[i]);
            cpus.add(cpu);
            CPUService service = new CPUService("cpu"+i,cpu);
            Thread thread = new Thread(service);
            Threads.add(thread);
            thread.start();
        }

        for(int i = 0; i< input.getConferences().length; i++){
            for (int j = 0; j< input.getConferences().length && j!=i; j++){
                if(input.getConferences()[j].getDate()<input.getConferences()[i].getDate() &&
                        input.getConferences()[j].getDate()>input.getConferences()[i].getFdate()) {
                    input.getConferences()[i].setFdate(input.getConferences()[j].getDate());
                }
            }
        }

        for (int i=0; i<input.getConferences().length;i++){
            ConferenceService service = new ConferenceService(input.getConferences()[i].getName(),input.getConferences()[i].getDate(), input.getConferences()[i].getFdate());
            conferenceServices.add(service);
            Thread thread = new Thread(service);
            Threads.add(thread);
            thread.start();
        }

        TimeService Service = new TimeService(input.getDuration(),input.getTickTime());
        Thread timeThread = new Thread(Service);
        Threads.add(timeThread);
        timeThread.start();

        for (int i=0; i<input.getStudents().length;i++){
            students.add(new Student(input.getStudents()[i].getName(), input.getStudents()[i].getDepartment()
                    ,input.getStudents()[i].getStatus()
                    ,input.getStudents()[i].getModels()));
            for (int j=0; j<students.get(i).getModels().size();j++){
                students.get(i).getModels().get(0).setStudent(students.get(i));

            }
            StudentService service4 = new StudentService("student"+i,students.get(i));
            Thread thread4 = new Thread(service4);
            students.get(i).setService(service4);
            Threads.add(thread4);
            thread4.start();
        }

        for (Thread t : Threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Statistics statistics1 = new Statistics(cpus,gpus, students, conferenceServices);

        Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
        try {
            Writer writer = new FileWriter(args[1]);
            String s=gson1.toJson(statistics1);
            gson1.toJson(statistics1,writer);
            writer.flush();
            writer.close();
            System.out.println(s);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
