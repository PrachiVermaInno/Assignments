import java.util.*;

// Employee class
class Employee implements Comparable<Employee> {
    private int id;
    private String name;
    private String dept;
    private double salary;

    // Constructor
    public Employee(int id, String name, String dept, double salary) {
        this.id = id;
        this.name = name;
        this.dept = dept;
        this.salary = salary;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDept() { return dept; }
    public double getSalary() { return salary; }

    // Comparable (Natural Ordering: dept -> name -> salary)
    @Override
    public int compareTo(Employee other) {
        // 1) Compare dept
        int deptCmp = this.dept.compareTo(other.dept);
        if (deptCmp != 0) return deptCmp;

        // 2) If dept same, compare name
        int nameCmp = this.name.compareTo(other.name);
        if (nameCmp != 0) return nameCmp;

        // 3) If name same, compare salary
        return Double.compare(this.salary, other.salary);
    }

    @Override
    public String toString() {
        return String.format("Employee{id=%d, name='%s', dept='%s', salary=%.2f}",
                id, name, dept, salary);
    }
}

// Comparator for Salary (Descending)
class SalaryDescComparator implements Comparator<Employee> {
    @Override
    public int compare(Employee e1, Employee e2) {
        return Double.compare(e2.getSalary(), e1.getSalary()); // descending
    }
}

public class EmployeeSortExample {
    public static void main(String[] args) {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "Alice", "HR", 50000));
        employees.add(new Employee(2, "Bob", "IT", 70000));
        employees.add(new Employee(3, "Charlie", "IT", 60000));
        employees.add(new Employee(4, "David", "HR", 55000));
        employees.add(new Employee(5, "Eve", "Finance", 75000));

        System.out.println("Original List:");
        for (Employee e : employees) {
            System.out.println(e);
        }

        // 1) Sort by dept -> name -> salary (Comparable)
        Collections.sort(employees);
        System.out.println("\nSorted by Dept -> Name -> Salary (Comparable):");
        Iterator<Employee> it1 = employees.iterator();
        while (it1.hasNext()) {
            System.out.println(it1.next());
        }

        // 2) Sort by salary descending (Comparator)
        Collections.sort(employees, new SalaryDescComparator());
        System.out.println("\nSorted by Salary (Descending, Comparator):");
        Iterator<Employee> it2 = employees.iterator();
        while (it2.hasNext()) {
            System.out.println(it2.next());
        }
    }
}
