import java.io.*;
import java.util.*;
class FileUtil {

    public static void writeClasses(List<Class> classes, String filename) {
        try(PrintWriter pw = new PrintWriter(new FileWriter(filename))){
            for(Class c : classes) {
                pw.println(c.getId() + "," + c.getName());
            }
        } catch(IOException e){ System.out.println("Error writing classes: " + e.getMessage()); }
    }

    public static void writeStudents(List<Student> students, String filename) {
        try(PrintWriter pw = new PrintWriter(new FileWriter(filename))){
            for(Student s : students) {
                pw.println(s.getId()+","+s.getName()+","+s.getClassId()+","+s.getMarks()+","+s.getGender()+","+s.getAge()+","+s.getResult());
            }
        } catch(IOException e){ System.out.println("Error writing students: " + e.getMessage()); }
    }

    public static void writeAddresses(List<Address> addresses, String filename){
        try(PrintWriter pw = new PrintWriter(new FileWriter(filename))){
            for(Address a : addresses) {
                pw.println(a.getId()+","+a.getStudentId()+","+a.getPincode()+","+a.getCity());
            }
        } catch(IOException e){ System.out.println("Error writing addresses: " + e.getMessage()); }
    }

    public static void writeRanking(List<Student> students, String filename){
        try(PrintWriter pw = new PrintWriter(new FileWriter(filename))){
            int rank=1;
            for(Student s: students){
                pw.println(rank + "," + s.getId() + "," + s.getName() + "," + s.getMarks());
                rank++;
                if(rank>5) break;
            }
        } catch(IOException e){ System.out.println("Error writing ranking: " + e.getMessage()); }
    }
}
