import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EmployeeSerialization {

    public static class Employee implements Serializable {
        private static final long serialVersionUID = 1L;

        private int id;
        private String name;
        private double salary;

        public Employee(int id, String name, double salary) {
            this.id = id;
            this.name = name;
            this.salary = salary;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public double getSalary() { return salary; }

        @Override
        public String toString() {
            return String.format("Employee [ID=%d, Name=%s, Salary=%.2f]", id, name, salary);
        }
    }

    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee(101, "Alice", 55000),
            new Employee(102, "Bob", 60000),
            new Employee(103, "Charlie", 75000),
            new Employee(104, "Diana", 80000)
        );

        System.out.println("--- Original Employees ---");
        for (Employee e : employees) {
            System.out.println(e);
        }

        byte[] serializedData = serializeEmployees(employees);
        List<Employee> deserialized = deserializeEmployees(serializedData);

        if (deserialized == null || deserialized.isEmpty()) {
            System.out.println("No employees deserialized or data empty.");
        } else {
            System.out.println("\n--- Deserialized Employees ---");
            for (Employee e : deserialized) {
                System.out.println(e);
            }
        }
    }

    private static byte[] serializeEmployees(List<Employee> employees) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {

            oos.writeObject(employees);
            oos.flush();
            return bos.toByteArray();

        } catch (IOException ioe) {
            System.err.println("Error during serialization: " + ioe.getMessage());
            return null;
        }
    }

    private static List<Employee> deserializeEmployees(byte[] data) {
        if (data == null) return Collections.emptyList();

        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {

            Object obj = ois.readObject();
            if (obj instanceof List) {
                List<?> raw = (List<?>) obj;
                List<Employee> employees = new ArrayList<>();
                for (Object item : raw) {
                    if (item instanceof Employee) {
                        employees.add((Employee) item);
                    }
                }
                return employees;
            }

        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Error during deserialization: " + ex.getMessage());
        }

        return Collections.emptyList();
    }
}
