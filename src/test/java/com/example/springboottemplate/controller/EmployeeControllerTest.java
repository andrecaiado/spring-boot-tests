package com.example.springboottemplate.controller;

import com.example.springboottemplate.entity.Employee;
import com.example.springboottemplate.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private ObjectMapper objMapper = new ObjectMapper();

    private Employee employee1;

    private Employee employee2;

    private List<Employee> employeeList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        objMapper.findAndRegisterModules();
        objMapper.registerModule(new JavaTimeModule());
        objMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        employee1 = new Employee();
        employee1.setId(1);
        employee1.setFirstName("John");
        employee1.setLastName("Doe");
        employee1.setAge(30);
        employee1.setDesignation("Software Engineer");
        employee1.setPhoneNumber("1234567890");
        employee1.setJoinedOn(LocalDate.of(2023, 6, 5));
        employee1.setAddress("123, Baker Street, London");
        employee1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        employee1.setCreatedAt(LocalDateTime.of(2024, 6, 5, 12, 0, 0));
        employee1.setUpdatedAt(LocalDateTime.of(2024, 6, 5, 12, 0, 0));

        employee2 = new Employee();
        employee2.setId(2);
        employee2.setFirstName("Jane");
        employee2.setLastName("Doe");
        employee2.setAge(30);
        employee2.setDesignation("Software Engineer");
        employee2.setPhoneNumber("1234567890");
        employee2.setJoinedOn(LocalDate.of(2023, 6, 5));
        employee2.setAddress("123, Baker Street, London");
        employee2.setDateOfBirth(LocalDate.of(1990, 1, 1));
        employee2.setCreatedAt(LocalDateTime.of(2024, 6, 5, 12, 0, 0));
        employee2.setUpdatedAt(LocalDateTime.of(2024, 6, 5, 12, 0, 0));

        employeeList.addAll(List.of(employee1, employee2));
    }

    @Test
    void whenGetAllEmployees_shouldReturn200() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(employeeList);
        mockMvc.perform(get("/employee/v1/")
                        .contentType("application/json"))
                .andExpect(content().json(objMapper.writeValueAsString(employeeList)))
                .andExpect(status().isOk());
    }

    @Test
    void givenValidId_whenGetEmployeeById_shouldReturnEmployee() throws Exception {
        when(employeeService.getEmployeeById(1)).thenReturn(employee1);
        mockMvc.perform(get("/employee/v1/1")
                        .contentType("application/json"))
                .andExpect(content().json(objMapper.writeValueAsString(employee1)))
                .andExpect(status().isOk());
    }

    @Test
    void givenValidPayload_whenSaveEmployee_thenReturnsEmployee() throws Exception {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("John");
        newEmployee.setLastName("Doe");
        newEmployee.setAge(30);
        newEmployee.setDesignation("Software Engineer");
        newEmployee.setPhoneNumber("1234567890");
        newEmployee.setJoinedOn(LocalDate.of(2023, 6, 5));
        newEmployee.setAddress("123, Baker Street, London");
        newEmployee.setDateOfBirth(LocalDate.of(1990, 1, 1));
        newEmployee.setCreatedAt(LocalDateTime.of(2024, 6, 5, 12, 0, 0));
        newEmployee.setUpdatedAt(LocalDateTime.of(2024, 6, 5, 12, 0, 0));

        when(employeeService.saveEmployee(newEmployee)).thenReturn(newEmployee);

        mockMvc.perform(post("/employee/v1/")
                        .contentType("application/json")
                        .content(objMapper.writeValueAsString(newEmployee))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objMapper.writeValueAsString(newEmployee)));
    }

    @Test
    void givenInvalidPayload_whenSaveEmployee_thenReturnsBadRequest() throws Exception {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("");
        newEmployee.setLastName("Doe");
        newEmployee.setAge(30);
        newEmployee.setDesignation("Software Engineer");
        newEmployee.setPhoneNumber("1234567890");
        newEmployee.setJoinedOn(LocalDate.of(2023, 6, 5));
        newEmployee.setAddress("123, Baker Street, London");
        newEmployee.setDateOfBirth(LocalDate.of(1990, 1, 1));
        newEmployee.setCreatedAt(LocalDateTime.of(2024, 6, 5, 12, 0, 0));
        newEmployee.setUpdatedAt(LocalDateTime.of(2024, 6, 5, 12, 0, 0));

        mockMvc.perform(post("/employee/v1/")
                        .contentType("application/json")
                        .content(objMapper.writeValueAsString(newEmployee))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenValidPayload_whenUpdateEmployee_shouldReturnEmployee() throws Exception {
        when(employeeService.updateEmployee(employee1)).thenReturn(employee1);
        mockMvc.perform(put("/employee/v1/")
                        .contentType("application/json")
                        .content(objMapper.writeValueAsString(employee1))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objMapper.writeValueAsString(employee1)));
    }

    @Test
    void givenValidId_whenDeleteEmployeeById_shouldReturnCorrectMessage() throws Exception {
        doNothing().when(employeeService).deleteEmployeeById(1);
        mockMvc.perform(delete("/employee/v1/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted employee successfully"));
    }
}