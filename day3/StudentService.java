
import java.util.*;
import java.util.stream.Collectors;

class StudentService {

    // Add student
    public static void addStudent(Map<Integer, Student> students,
                                  Map<Integer, List<Student>> studentsByClass,
                                  Map<Integer, Address> addresses,
                                  Student s, Address a) {
        students.put(s.getId(), s);
        addresses.put(a.getStudentId(), a);
        studentsByClass.computeIfAbsent(s.getClassId(), k -> new ArrayList<>()).add(s);
    }

    // Delete student and cleanup
    public static void deleteStudent(Map<Integer, Student> students,
                                     Map<Integer, List<Student>> studentsByClass,
                                     Map<Integer, Address> addresses,
                                     Map<Integer, Classroom> classes,
                                     int studentId) {
        Student s = students.get(studentId);
        if (s == null) return;

        students.remove(studentId);
        addresses.remove(studentId);

        List<Student> list = studentsByClass.get(s.getClassId());
        if (list != null) list.remove(s);

        if (list == null || list.isEmpty()) {
            studentsByClass.remove(s.getClassId());
            classes.remove(s.getClassId());
        }
    }

    // Filter + pagination + sort
    public static List<Student> readStudentsPaginated(Collection<Student> allStudents,
                                                      Map<Integer, Address> addresses,
                                                      String gender,
                                                      Integer age,
                                                      Integer classId,
                                                      String city,
                                                      String pincode,
                                                      String orderBy,
                                                      int startIndex,
                                                      int endIndex) {
        List<Student> filtered = allStudents.stream().filter(s -> {
            Address a = addresses.get(s.getId());
            if (gender != null && !gender.isEmpty() && !s.getGender().equalsIgnoreCase(gender)) return false;
            if (age != null && s.getAge() != age) return false;
            if (classId != null && s.getClassId() != classId) return false;
            if (a != null) {
                if (city != null && !city.isEmpty() && !a.getCity().equalsIgnoreCase(city)) return false;
                if (pincode != null && !pincode.isEmpty() && !a.getPincode().equalsIgnoreCase(pincode)) return false;
            }
            return true;
        }).collect(Collectors.toList());

        if ("name".equalsIgnoreCase(orderBy)) {
            filtered.sort(Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER));
        } else if ("marks".equalsIgnoreCase(orderBy)) {
            filtered.sort(Comparator.comparingInt(Student::getMarks).reversed());
        }

        int from = Math.max(0, startIndex - 1);
        int to = Math.min(filtered.size(), endIndex);
        if (from >= filtered.size()) return Collections.emptyList();

        return filtered.subList(from, to);
    }

    // Update ranks based on marks
    public static void updateRanks(List<Student> students) {
        students.sort(Comparator.comparingInt(Student::getMarks).reversed());
        int rank = 1;
        for (Student s : students) {
            s.setRank(rank++);
        }
    }

    // Get passed students
    public static List<Student> getPassedStudents(Collection<Student> students) {
        return students.stream().filter(s -> s.getMarks() >= 50).collect(Collectors.toList());
    }

    // Get failed students
    public static List<Student> getFailedStudents(Collection<Student> students) {
        return students.stream().filter(s -> s.getMarks() < 50).collect(Collectors.toList());
    }
}

