class Address {
    private int id, studentId;
    private String pincode, city;

    public Address(int id, int studentId, String pincode, String city){
        this.id = id; this.studentId = studentId; this.pincode = pincode; this.city = city;
    }

    // Getters & Setters
    public int getId() { return id; }
    public int getStudentId() { return studentId; }
    public String getPincode() { return pincode; }
    public String getCity() { return city; }
}
