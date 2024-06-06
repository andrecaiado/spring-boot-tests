package com.example.springboottemplate.controller;

import com.example.springboottemplate.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

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
        List<Employee> users = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(20, users.size());
    }

}