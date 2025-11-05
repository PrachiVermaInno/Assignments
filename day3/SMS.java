import java.util.*;

public class SMS {

    // === Entity Classes ===
    static class Student {
        int id;
        String name;
        int classId;
        int marks;
        String gender;
        int age;
        String result;
        int rank;

        Student(int id, String name, int classId, int marks, String gender, int age) {
            this.id = id;
            this.name = name;
            this.classId = classId;
            this.marks = marks;
            this.gender = gender;
            this.age = age;
        }
    }

    static class Address {
        int id;
        String pincode;
        String city;
        int studentId;

        Address(int id, String pincode, String city, int studentId) {
            this.id = id;
            this.pincode = pincode;
            this.city = city;
            this.studentId = studentId;
        }
    }

    static class ClassRoom {
        int id;
        String name;

        ClassRoom(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    // === Data Collections ===
    List<Student> students = new ArrayList<>();
    List<Address> addresses = new ArrayList<>();
    List<ClassRoom> classes = new ArrayList<>();

    Scanner sc = new Scanner(System.in);

    // === Core Methods ===
    void addClassRoom() {
        System.out.print("Enter Class ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Class Name: ");
        String name = sc.nextLine();
        classes.add(new ClassRoom(id, name));
        System.out.println(" Class Added Successfully!");
    }

    void addStudent() {
        System.out.print("Enter Student ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Student Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Class ID: ");
        int classId = sc.nextInt();
        System.out.print("Enter Marks: ");
        int marks = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Gender (M/F): ");
        String gender = sc.nextLine();
        System.out.print("Enter Age: ");
        int age = sc.nextInt();

        if (age > 20) {
            System.out.println(" Cannot add student. Age > 20.");
            return;
        }

        Student s = new Student(id, name, classId, marks, gender, age);
        if (marks < 50) {
            s.result = "Fail";
        } else {
            s.result = "Pass";
        }
        students.add(s);
        System.out.println("Student Added Successfully!");
        rankStudents();
    }

    void addAddress() {
        System.out.print("Enter Address ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Pincode: ");
        String pincode = sc.nextLine();
        System.out.print("Enter City: ");
        String city = sc.nextLine();
        System.out.print("Enter Student ID: ");
        int sid = sc.nextInt();
        addresses.add(new Address(id, pincode, city, sid));
        System.out.println(" Address Added Successfully!");
    }

    // === Ranking ===
    void rankStudents() {
        for (int i = 0; i < students.size() - 1; i++) {
            for (int j = i + 1; j < students.size(); j++) {
                if (students.get(i).marks < students.get(j).marks) {
                    Student temp = students.get(i);
                    students.set(i, students.get(j));
                    students.set(j, temp);
                }
            }
        }
        int rank = 1;
        for (Student s : students) {
            s.rank = rank++;
        }
    }

    // === Search Functions ===
    List<Student> findByPincode(String pincode) {
        List<Student> result = new ArrayList<>();
        for (Address a : addresses) {
            if (a.pincode.equals(pincode)) {
                for (Student s : students) {
                    if (s.id == a.studentId) {
                        result.add(s);
                    }
                }
            }
        }
        return result;
    }

    List<Student> findByCity(String city) {
        List<Student> result = new ArrayList<>();
        for (Address a : addresses) {
            if (a.city.equalsIgnoreCase(city)) {
                for (Student s : students) {
                    if (s.id == a.studentId) {
                        result.add(s);
                    }
                }
            }
        }
        return result;
    }

    List<Student> findByClass(String className) {
        List<Student> result = new ArrayList<>();
        int classId = -1;
        for (ClassRoom c : classes) {
            if (c.name.equalsIgnoreCase(className)) {
                classId = c.id;
                break;
            }
        }
        if (classId != -1) {
            for (Student s : students) {
                if (s.classId == classId) {
                    result.add(s);
                }
            }
        }
        return result;
    }

    List<Student> getPassedStudents() {
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.result.equalsIgnoreCase("Pass")) {
                result.add(s);
            }
        }
        return result;
    }

    List<Student> getFailedStudents() {
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.result.equalsIgnoreCase("Fail")) {
                result.add(s);
            }
        }
        return result;
    }

    // === Delete Function ===
    void deleteStudent() {
        System.out.print("Enter Student ID to delete: ");
        int sid = sc.nextInt();
        boolean found = false;

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).id == sid) {
                students.remove(i);
                found = true;
                System.out.println("Student deleted successfully.");
                break;
            }
        }

        for (int i = 0; i < addresses.size(); i++) {
            if (addresses.get(i).studentId == sid) {
                addresses.remove(i);
                i--;
            }
        }

        // Delete empty class
        Set<Integer> classIds = new HashSet<>();
        for (Student s : students) {
            classIds.add(s.classId);
        }
        for (int i = 0; i < classes.size(); i++) {
            if (!classIds.contains(classes.get(i).id)) {
                System.out.println("🗑 Class " + classes.get(i).name + " deleted (no students left).");
                classes.remove(i);
                i--;
            }
        }

        if (!found) {
            System.out.println(" Student not found.");
        }
    }

    // === Pagination ===
    List<Student> getPaginated(int start, int end, String orderBy) {
        rankStudents();
        if (orderBy.equalsIgnoreCase("name")) {
            for (int i = 0; i < students.size() - 1; i++) {
                for (int j = i + 1; j < students.size(); j++) {
                    if (students.get(i).name.compareToIgnoreCase(students.get(j).name) > 0) {
                        Student temp = students.get(i);
                        students.set(i, students.get(j));
                        students.set(j, temp);
                    }
                }
            }
        }

        List<Student> result = new ArrayList<>();
        for (int i = start - 1; i < end && i < students.size(); i++) {
            result.add(students.get(i));
        }
        return result;
    }

    // === Display Function ===
    void displayStudents(List<Student> list) {
        if (list.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        for (Student s : list) {
            System.out.println("ID: " + s.id + ", Name: " + s.name + ", ClassId: " + s.classId +
                    ", Marks: " + s.marks + ", Gender: " + s.gender + ", Age: " + s.age +
                    ", Result: " + s.result + ", Rank: " + s.rank);
        }
    }

    // === Menu ===
    void menu() {
        while (true) {
            System.out.println("\n==== STUDENT MANAGEMENT MENU ====");
            System.out.println("1. Add Class");
            System.out.println("2. Add Student");
            System.out.println("3. Add Address");
            System.out.println("4. Find Students by Pincode");
            System.out.println("5. Find Students by City");
            System.out.println("6. Find Students by Class");
            System.out.println("7. Get Passed Students");
            System.out.println("8. Get Failed Students");
            System.out.println("9. Delete Student");
            System.out.println("10. Paginate Students");
            System.out.println("11. View All Students");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addClassRoom();
                    break;
                case 2:
                    addStudent();
                    break;
                case 3:
                    addAddress();
                    break;
                case 4:
                    System.out.print("Enter Pincode: ");
                    String pin = sc.nextLine();
                    displayStudents(findByPincode(pin));
                    break;
                case 5:
                    System.out.print("Enter City: ");
                    String city = sc.nextLine();
                    displayStudents(findByCity(city));
                    break;
                case 6:
                    System.out.print("Enter Class Name: ");
                    String cname = sc.nextLine();
                    displayStudents(findByClass(cname));
                    break;
                case 7:
                    displayStudents(getPassedStudents());
                    break;
                case 8:
                    displayStudents(getFailedStudents());
                    break;
                case 9:
                    deleteStudent();
                    break;
                case 10:
                    System.out.print("Enter start index: ");
                    int s = sc.nextInt();
                    System.out.print("Enter end index: ");
                    int e = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Order by (name/marks): ");
                    String order = sc.nextLine();
                    displayStudents(getPaginated(s, e, order));
                    break;
                case 11:
                    displayStudents(students);
                    break;
                case 0:
                    System.out.println("Exiting... Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }

    // === Main ===
    public static void main(String[] args) {
        SMS sms = new SMS();
        sms.menu();
    }
}
