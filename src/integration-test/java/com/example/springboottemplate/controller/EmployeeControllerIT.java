package com.example.springboottemplate.controller;

import com.example.springboottemplate.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1);
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
    }

    @Test
    public void testGetAllEmployees() {
        // Test the GET request
        String getUrl = "/employee/v1/";
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        HttpEntity requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<List<Employee>> response = testRestTemplate.exchange(getUrl,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<Employee>>() {
                });
        List<Employee> employees = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(20, employees.size());
    }

    @Test
    public void testGetEmployeeById() {
        // Test the GET request
        String getUrl = "/employee/v1/1";
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        HttpEntity requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Employee> response = testRestTemplate.exchange(getUrl,
                HttpMethod.GET,
                requestEntity,
                Employee.class);
        Employee employee = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, employee.getId());
    }

    @Test
    public void testSaveEmployee() {
        // Test the POST request
        String postUrl = "/employee/v1/";
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("Content-Type", "application/json");

        HttpEntity<Employee> requestEntity = new HttpEntity<>(employee, headers);
        ResponseEntity<Employee> response = testRestTemplate.exchange(postUrl,
                HttpMethod.POST,
                requestEntity,
                Employee.class);
        Employee responseEmployee = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John", responseEmployee.getFirstName());
        assertEquals(30, responseEmployee.getAge());
    }

    @Test
    public void testUpdateEmployee() {
        // Test the PUT request
        String putUrl = "/employee/v1/";
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("Content-Type", "application/json");

        HttpEntity<Employee> requestEntity = new HttpEntity<>(employee, headers);
        ResponseEntity<Employee> response = testRestTemplate.exchange(putUrl,
                HttpMethod.PUT,
                requestEntity,
                Employee.class);
        Employee responseEmployee = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John", responseEmployee.getFirstName());
        assertEquals(30, responseEmployee.getAge());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void testDeleteEmployeeById() {
        // Test the DELETE request
        String deleteUrl = "/employee/v1/1";
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        HttpEntity requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(deleteUrl,
                HttpMethod.DELETE,
                requestEntity,
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted employee successfully", response.getBody());
    }

}