package com.example.demo.controllers;

import com.example.demo.model.Employee;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.services.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SpringBootTest
public class EmployeeControllerTest {

    @InjectMocks
    EmployeeController employeeController;
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    EmployeeService employeeService;
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    public void testResponse_whenInvalidURL() throws Exception {
        mockMvc.perform(get("/api/employee/fake/url"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGet_whenValidID() throws Exception {
        Employee employee = createEmployee();

        // Mock repository behavior
        when(employeeRepository.findById(11)).thenReturn(Optional.of(employee));

        // Perform GET request and verify response
        mockMvc.perform(get("/api/employee/get/11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee_id").value(11))
                .andExpect(jsonPath("$.first_name").value("Leo"))
                .andExpect(jsonPath("$.last_name").value("Tolstoy"))
                .andExpect(jsonPath("$.title").value("Mr"))
                .andExpect(jsonPath("$.access").value("Level 2"));
    }

    @Test
    public void testGet_whenInvalidID() throws Exception {
        Employee employee = createEmployee();

        when(employeeRepository.findById(3)).thenReturn(null);

        // Perform GET request and verify response
        mockMvc.perform(get("/api/employee/get/11"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No employees found"));

    }

    @Test
    public void testList_whenEmployeesExist() throws Exception {
        Employee employee = createEmployee();
        Employee employee2 = createEmployee2();

        // Mock repository behavior
        when(employeeRepository.findAll()).thenReturn(List.of(employee, employee2));

        // Perform GET request and verify response
        mockMvc.perform(get("/api/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employee_id").value(11))
                .andExpect(jsonPath("$[0].first_name").value("Leo"))
                .andExpect(jsonPath("$[0].last_name").value("Tolstoy"))
                .andExpect(jsonPath("$[0].title").value("Mr"))
                .andExpect(jsonPath("$[0].access").value("Level 2"))
                .andExpect(jsonPath("$[1].employee_id").value(1916))
                .andExpect(jsonPath("$[1].first_name").value("Michael"))
                .andExpect(jsonPath("$[1].last_name").value("Collins"))
                .andExpect(jsonPath("$[1].title").value("Mr"))
                .andExpect(jsonPath("$[1].access").value("Level 5"));
    }

    @Test
    public void testList_whenNoEmployees() throws Exception {
        // Mock repository behavior
        when(employeeRepository.findAll()).thenReturn(List.of());

        // Perform GET request and verify response
        mockMvc.perform(get("/api/employee"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No employees found"));
    }

    @Test
    public void testCreate_whenValidBody() throws Exception {
        Employee employee = createEmployee();

        when(employeeRepository.saveAndFlush(Mockito.any())).thenReturn(employee);

        // Perform GET request and verify response
        mockMvc.perform(MockMvcRequestBuilders.post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employee_id").value(11))
                .andExpect(jsonPath("$.first_name").value("Leo"))
                .andExpect(jsonPath("$.last_name").value("Tolstoy"))
                .andExpect(jsonPath("$.title").value("Mr"))
                .andExpect(jsonPath("$.access").value("Level 2"));
    }

    @Test
    public void testCreate_whenNullBody() throws Exception {
        // Perform GET request and verify response
        mockMvc.perform(MockMvcRequestBuilders.post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteEmployee_whenValid() throws Exception {
        int employeeId = 1;

        Mockito.when(employeeRepository.existsById(Mockito.anyInt())).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/employee/delete/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(employeeRepository).deleteById(employeeId);
    }

    @Test
    public void testDeleteEmployee_whenNoEmployeeExists() throws Exception {
        int employeeId = 1;

        Mockito.when(employeeRepository.existsById(Mockito.anyInt())).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/employee/delete/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Employee with ID 1 not found"));
    }

    @Test
    public void testStartsWith_whenEmployeesFound() throws Exception {
        Employee employee = createEmployee();

        Mockito.when(employeeService.findEmployeeBeginningWithChar(Mockito.anyChar())).thenReturn(List.of(employee));

        mockMvc.perform(get("/api/employee/starts/L"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employee_id").value(11))
                .andExpect(jsonPath("$[0].first_name").value("Leo"))
                .andExpect(jsonPath("$[0].last_name").value("Tolstoy"))
                .andExpect(jsonPath("$[0].title").value("Mr"))
                .andExpect(jsonPath("$[0].access").value("Level 2"));
    }

    @Test
    public void testStartsWith_whenNoEmployeesFound() throws Exception {
        Mockito.when(employeeService.findEmployeeBeginningWithChar(Mockito.anyChar())).thenReturn(List.of());

        mockMvc.perform(get("/api/employee/starts/L"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No employees found"));
    }

    @Test
    public void testUpdate_whenValidUpdateBody() throws Exception {
        Employee existingEmployee = createEmployee();
        Employee updatedEmployee = createEmployee2();

        when(employeeRepository.findById(11)).thenReturn(java.util.Optional.of(existingEmployee));
        when(employeeRepository.saveAndFlush(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/employee/update/11")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.employee_id").value(11))
                .andExpect(jsonPath("$.first_name").value("Michael"))
                .andExpect(jsonPath("$.last_name").value("Collins"))
                .andExpect(jsonPath("$.title").value("Mr"))
                .andExpect(jsonPath("$.access").value("Level 5"));
    }

    @Test
    public void testUpdate_whenInvalidUpdateBody() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/employee/update/11")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdate_whenExistingIDNotFound() throws Exception {
        Employee existingEmployee = createEmployee();

        when(employeeRepository.findById(11)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/employee/update/11")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingEmployee)))
                .andExpect(status().isNotFound());
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ///                                 Helper Functions                                ///
    ///////////////////////////////////////////////////////////////////////////////////////

    private Employee createEmployee() {
        Employee emp = new Employee();
        emp.setEmployee_id(11);
        emp.setFirst_name("Leo");
        emp.setLast_name("Tolstoy");
        emp.setTitle("Mr");
        emp.setAccess("Level 2");
        return emp;
    }

    private Employee createEmployee2() {
        Employee emp = new Employee();
        emp.setEmployee_id(1916);
        emp.setFirst_name("Michael");
        emp.setLast_name("Collins");
        emp.setTitle("Mr");
        emp.setAccess("Level 5");
        return emp;
    }
}
