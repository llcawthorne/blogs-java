package io.github.llcawthorne.junit5.helpers;

public class EmployeeDao {

    public Employee save(long id) {
        return new Employee(id);
    }

    public Employee save(long id, String firstName) {
        return new Employee(id, firstName);
    }

    public Employee update(Employee employee) {
        return employee;
    }
}
