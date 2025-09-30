class Student {
    private int id;
    private String name;
    private int classId;
    private int marks;
    private String gender;
    private int age;

    private String status; // Pass or Fail
    private int rank;

    public Student(int id, String name, int classId, int marks, String gender, int age) {
        this.id = id;
        this.name = name;
        this.classId = classId;
        this.marks = marks;
        this.gender = gender;
        this.age = age;
        this.status = (marks < 50) ? "Fail" : "Pass";
        this.rank = 0;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getClassId() { return classId; }
    public int getMarks() { return marks; }
    public String getGender() { return gender; }
    public int getAge() { return age; }
    public String getStatus() { return status; }
    public int getRank() { return rank; }

    // Setters
    public void setRank(int rank) { this.rank = rank; }
    public void updateStatus() { this.status = (marks < 50) ? "Fail" : "Pass"; }

    @Override
    public String toString() {
        return "Student[id=" + id + ", name=" + name + ", classId=" + classId +
                ", marks=" + marks + ", gender=" + gender + ", age=" + age +
                ", status=" + status + ", rank=" + rank + "]";
    }
}
