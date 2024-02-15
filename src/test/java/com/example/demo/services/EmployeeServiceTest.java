package com.example.demo.services;

import com.example.demo.model.Employee;
import com.example.demo.repositories.EmployeeRepository;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EmployeeServiceTest {

    @InjectMocks
    EmployeeService employeeService;

    @Mock
    EmployeeRepository employeeRepository;

    @Test
    public void testFindEmployeeBeginningWithChar_whenFound() {
        List<Employee> testList = new ArrayList<>();
        testList.add(createEmployee());
        Mockito.when(employeeRepository.findAll()).thenReturn(testList);

        List<Employee> returned = employeeService.findEmployeeBeginningWithChar('L');

        assertNotNull(returned);
        assertEquals(11, returned.get(0).getEmployee_id());
    }

    @Test
    public void testFindEmployeeBeginningWithChar_whenNotFound() {
        Mockito.when(employeeRepository.findAll()).thenReturn(new ArrayList<>());

        List<Employee> returned = employeeService.findEmployeeBeginningWithChar('L');

        assertEquals(0, returned.size());
    }

    private Employee createEmployee() {
        Employee emp = new Employee();
        emp.setEmployee_id(11);
        emp.setFirst_name("Leo");
        emp.setLast_name("Tolstoy");
        emp.setTitle("Mr");
        emp.setAccess("Level 2");
        return emp;
    }
}
