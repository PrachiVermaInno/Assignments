import java.util.*;
import java.util.stream.Collectors;

public class StudentService {
    private List<Class> classes = new ArrayList<>();
    private List<Student> students = new ArrayList<>();
    private List<Address> addresses = new ArrayList<>();

    // Add Class
    public void addClass(Class c){
        classes.add(c);
        FileUtil.writeClasses(classes,"class.csv"); }

    // Add Student
    public void addStudent(Student s){
        students.add(s);
        FileUtil.writeStudents(students,"student.csv");
        updateRanking();
    }

    // Add Address
    public void addAddress(Address a){
        addresses.add(a);
        FileUtil.writeAddresses(addresses,"address.csv");
    }

    // Delete Student (cascade)
    public void deleteStudent(int id){
        Student s = students.stream().filter(st->st.getId()==id).findFirst().orElse(null);
        if(s==null){ System.out.println("Student not found!"); return; }
        students.remove(s);
        addresses.removeIf(a->a.getStudentId()==id);
        // Delete class if empty
        int classId = s.getClassId();
        if(students.stream().noneMatch(st->st.getClassId()==classId)){
            classes.removeIf(c->c.getId()==classId);
        }
        // Save files
        FileUtil.writeStudents(students,"student.csv");
        FileUtil.writeAddresses(addresses,"address.csv");
        FileUtil.writeClasses(classes,"class.csv");
        updateRanking();
    }

    public Address getAddressByStudentId(int id){
        return addresses.stream().filter(a->a.getStudentId()==id).findFirst().orElse(null);
    }

    // Ranking Top 5
    public void updateRanking(){
        List<Student> top = students.stream().sorted((s1,s2)->s2.getMarks()-s1.getMarks()).limit(5).collect(Collectors.toList());
        FileUtil.writeRanking(top,"ranking.csv");
    }

    // Filters with Pagination + Sorting
    public List<Student> filterStudents(Optional<String> gender, Optional<Integer> age, Optional<Integer> classId,
                                        Optional<String> city, Optional<String> pincode,
                                        Optional<String> sortBy, int start, int end){
        List<Student> filtered = students.stream()
                .filter(s -> gender.map(g -> s.getGender().equalsIgnoreCase(g)).orElse(true))
                .filter(s -> age.map(a -> s.getAge()==a).orElse(true))
                .filter(s -> classId.map(cid -> s.getClassId()==cid).orElse(true))
                .filter(s -> city.map(c -> {
                    Address addr = getAddressByStudentId(s.getId());
                    return addr!=null && addr.getCity().equalsIgnoreCase(c);
                }).orElse(true))
                .filter(s -> pincode.map(p -> {
                    Address addr = getAddressByStudentId(s.getId());
                    return addr!=null && addr.getPincode().equalsIgnoreCase(p);
                }).orElse(true))
                .collect(Collectors.toList());

        // Sorting
        if(sortBy.isPresent()){
            if(sortBy.get().equalsIgnoreCase("name")) filtered.sort(Comparator.comparing(Student::getName));
            else if(sortBy.get().equalsIgnoreCase("marks")) filtered.sort((s1,s2)->s2.getMarks()-s1.getMarks());
        }

        // Pagination (1-based index)
        int from = Math.max(0, start-1);
        int to = Math.min(filtered.size(), end);
        if(from>=to) return new ArrayList<>();
        return filtered.subList(from,to);
    }

    // Get passed or failed students with filters
    public List<Student> getPassedStudents(Optional<String> gender, Optional<Integer> age, Optional<Integer> classId,
                                           Optional<String> city, Optional<String> pincode){
        return filterStudents(gender, age, classId, city, pincode, Optional.empty(), 1, students.size())
                .stream().filter(s->s.getResult().equals("Pass")).toList();
    }

    public List<Student> getFailedStudents(Optional<String> gender, Optional<Integer> age, Optional<Integer> classId,
                                           Optional<String> city, Optional<String> pincode){
        return filterStudents(gender, age, classId, city, pincode, Optional.empty(), 1, students.size())
                .stream().filter(s->s.getResult().equals("Fail")).toList();
    }
}
