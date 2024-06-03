package com.example.springboottemplate.repository;

import com.example.springboottemplate.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void givenPreInsertedDataInDatabase_whenGetAllEmployees_ThenReturn20Records() {
        // Given
        // We already have 20 records in the database that were inserted with Flyway migration

        // When
        List<Employee> employeeList = employeeRepository.findAll();

        // Then
        assertEquals(20, employeeList.size());
    }

    @Test
    void givenPreInsertedDataInDatabase_whenGetEmployeeById_ThenReturnEmployee() {
        // Given
        // We already have 20 records in the database that were inserted with Flyway migration

        // When
        Employee employee = employeeRepository.findById(1).get();

        // Then
        assertEquals("Blayne", employee.getFirstName());
    }

    @Test
    void givenEmployeeObject_whenSaveEmployee_ThenReturnSavedEmployee() {
        // Given
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setAge(30);
        employee.setDesignation("Software Engineer");
        employee.setPhoneNumber("1234567890");
        employee.setJoinedOn(LocalDate.now());
        employee.setAddress("123, Baker Street, London");
        employee.setDateOfBirth(LocalDate.of(1990, 1, 1));
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());

        // When
        Employee savedEmployee = employeeRepository.save(employee);

        // Then
        assertEquals("John", savedEmployee.getFirstName());
    }

    @Test
    void givenEmployeeObject_whenUpdateEmployee_ThenReturnUpdatedEmployee() {
        // Given
        Employee employee = employeeRepository.findById(1).get();
        employee.setFirstName("Blayne1");

        // When
        Employee updatedEmployee = employeeRepository.save(employee);

        // Then
        assertEquals("Blayne1", updatedEmployee.getFirstName());
    }

    @Test
    void givenEmployeeId_whenDeleteEmployee_ThenReturnVoid() {
        // Given
        // We already have 20 records in the database that were inserted with Flyway migration

        // When
        employeeRepository.deleteById(1);

        // Then
        assertEquals(19, employeeRepository.findAll().size());
    }
}