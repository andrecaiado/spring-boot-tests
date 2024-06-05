package com.example.springboottemplate.service;

import com.example.springboottemplate.entity.Employee;
import com.example.springboottemplate.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee1;

    private Employee employee2;

    private List<Employee> employeeList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        employee1 = new Employee();
        employee1.setId(1);
        employee1.setFirstName("John");
        employee1.setLastName("Doe");
        employee1.setAge(30);
        employee1.setDesignation("Software Engineer");
        employee1.setPhoneNumber("1234567890");
        employee1.setJoinedOn(LocalDate.now());
        employee1.setAddress("123, Baker Street, London");
        employee1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        employee1.setCreatedAt(LocalDateTime.now());
        employee1.setUpdatedAt(LocalDateTime.now());

        employee2 = new Employee();
        employee2.setId(2);
        employee2.setFirstName("Jane");
        employee2.setLastName("Doe");
        employee2.setAge(30);
        employee2.setDesignation("Software Engineer");
        employee2.setPhoneNumber("1234567890");
        employee2.setJoinedOn(LocalDate.now());
        employee2.setAddress("123, Baker Street, London");
        employee2.setDateOfBirth(LocalDate.of(1990, 1, 1));
        employee2.setCreatedAt(LocalDateTime.now());
        employee2.setUpdatedAt(LocalDateTime.now());

        employeeList.addAll(List.of(employee1, employee2));
    }

    @Test
    void getAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(employeeList);
        List<Employee> employeeList = employeeService.getAllEmployees();
        assertEquals(2, employeeList.size());
    }

    @Test
    void getEmployeeById() {
        when(employeeRepository.findById(1)).thenReturn(java.util.Optional.of(employee1));
        Employee employee = employeeService.getEmployeeById(1);
        assertEquals("John", employee.getFirstName());
    }

    @Test
    void saveEmployee() {
        when(employeeRepository.save(employee1)).thenReturn(employee1);
        Employee savedEmployee = employeeService.saveEmployee(employee1);
        assertEquals("John", savedEmployee.getFirstName());
    }

    @Test
    void updateEmployee() {
        when(employeeRepository.findById(1)).thenReturn(java.util.Optional.of(employee1));
        when(employeeRepository.save(employee1)).thenReturn(employee1);
        Employee updatedEmployee = employeeService.updateEmployee(employee1);
        assertEquals("John", updatedEmployee.getFirstName());
    }

    @Test
    void deleteEmployeeById() {
        when(employeeRepository.findById(1)).thenReturn(java.util.Optional.of(employee1));
        employeeService.deleteEmployeeById(1);
        Mockito.verify(employeeRepository, Mockito.times(1)).deleteById(1);
    }
}