package com.example.springboottemplate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class EmployeeControllerMockMvcIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllEmployees() throws Exception {
        mockMvc.perform(get("/employee/v1/"))
                .andExpect(status().isOk());
    }

}
