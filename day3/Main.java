import java.util.*;

public class Main {
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Map<Integer, Class> classes = new HashMap<>();
        Map<Integer, Student> students = new HashMap<>();
        Map<Integer, List<Student>> studentsByClass = new HashMap<>();
        Map<Integer, Address> addresses = new HashMap<>();

        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Add Classroom");
            System.out.println("2. Add Student + Address");
            System.out.println("3. Show All Students");
            System.out.println("4. Delete Student");
            System.out.println("5. Read Students Paginated");
            System.out.println("6. Exit");

            int choice = readInt("Choose option: ");
            switch (choice) {
                case 1 -> addClass(classes);
                case 2 -> addStudent(classes, students, studentsByClass, addresses);
                case 3 -> showAll(classes, studentsByClass, addresses);
                case 4 -> deleteStudent(students, studentsByClass, addresses, classes);
                case 5 -> readStudentsPaginatedMenu(students, addresses);
                case 6 -> exit = true;
                default -> System.out.println("Invalid option.");
            }
        }
        System.out.println("Exiting...");
    }

    private static void addClass(Map<Integer, Class> classes) {
        int cid = readInt("Class ID: ");
        String name = readNonEmpty("Class Name: ");
        classes.put(cid, new Class(cid, name));
        System.out.println("Class added.");
    }

    private static void addStudent(Map<Integer, Class> classes,
                                   Map<Integer, Student> students,
                                   Map<Integer, List<Student>> studentsByClass,
                                   Map<Integer, Address> addresses) {
        int sid = readInt("Student ID: ");
        String name = readNonEmpty("Student Name: ");
        int classId = readInt("Class ID: ");
        int marks = readInt("Marks: ");
        String gender = readNonEmpty("Gender (M/F): ");
        int age = readInt("Age (<20): ");
        int addrId = readInt("Address ID: ");
        String city = readNonEmpty("City: ");
        String pincode = readNonEmpty("Pincode: ");

        Student s = new Student(sid, name, classId, marks, gender, age);
        Address a = new Address(addrId, pincode, city, sid);
        StudentService.addStudent(students, studentsByClass, addresses, s, a);
        System.out.println("Student added.");
    }

    private static void showAll(Map<Integer, Class> classes,
                                Map<Integer, List<Student>> studentsByClass,
                                Map<Integer, Address> addresses) {
        classes.values().forEach(c -> {
            System.out.println(c);
            List<Student> list = studentsByClass.get(c.getId());
            if (list != null) list.forEach(s -> System.out.println("  " + s + " " + addresses.get(s.getId())));
        });
    }

    private static void deleteStudent(Map<Integer, Student> students,
                                      Map<Integer, List<Student>> studentsByClass,
                                      Map<Integer, Address> addresses,
                                      Map<Integer, Class> classes) {
        int sid = readInt("Student ID to delete: ");
        StudentService.deleteStudent(students, studentsByClass, addresses, classes, sid);
        System.out.println("Deleted student and cleaned references.");
    }

    private static void readStudentsPaginatedMenu(Map<Integer, Student> students,
                                                  Map<Integer, Address> addresses) {
        String gender = readOptional("Gender (M/F or empty): ");
        String ageStr = readOptional("Age (<enter any): ");
        String classStr = readOptional("Class ID (<enter any): ");
        String city = readOptional("City (<enter any): ");
        String pincode = readOptional("Pincode (<enter any): ");
        String orderBy = readOptional("Sort by (name/marks): ");
        int start = readInt("Start record: ");
        int end = readInt("End record: ");

        Integer age = ageStr.isEmpty() ? null : Integer.parseInt(ageStr);
        Integer classId = classStr.isEmpty() ? null : Integer.parseInt(classStr);

        List<Student> page = StudentService.readStudentsPaginated(students.values(), addresses,
                gender, age, classId, city, pincode, orderBy, start, end);

        page.forEach(s -> System.out.println(s + " " + addresses.get(s.getId())));
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(sc.nextLine().trim());
    }

    private static String readNonEmpty(String prompt) {
        String s;
        do {
            System.out.print(prompt);
            s = sc.nextLine().trim();
        } while (s.isEmpty());
        return s;
    }

    private static String readOptional(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }
}
