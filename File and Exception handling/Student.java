class Student {
    private int id, classId, marks, age;
    private String name, gender;
    private String result;

    public Student(int id, String name, int classId, int marks, String gender, int age) throws InvalidAgeException, InvalidMarksException {
        if(age < 20) throw new InvalidAgeException("Age must be >=20");
        if(marks < 0 || marks > 100) throw new InvalidMarksException("Marks must be between 0 and 100");
        this.id = id; this.name = name; this.classId = classId;
        this.marks = marks; this.gender = gender; this.age = age;
        this.result = marks < 50 ? "Fail" : "Pass";
    }

    // Getters & Setters
    public int getId() { return id; }
    public int getClassId() { return classId; }
    public int getMarks() { return marks; }
    public int getAge() { return age; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public String getResult() { return result; }

    // Custom Exceptions
    public static class InvalidAgeException extends Exception {
        public InvalidAgeException(String msg){ super(msg);}
    }
    public static class InvalidMarksException extends Exception {
        public InvalidMarksException(String msg){ super(msg);}
    }
}
