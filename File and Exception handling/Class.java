class Class {
    private int id;
    private String name;

    public Class(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // Custom Exception inside ClassEntity (if needed)
    public static class ClassNotFoundException extends Exception {
        public ClassNotFoundException(String msg) { super(msg); }
    }
}
