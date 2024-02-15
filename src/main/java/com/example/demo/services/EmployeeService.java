package com.example.demo.services;

import com.example.demo.model.Employee;
import com.example.demo.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public List<Employee> findEmployeeBeginningWithChar(char c) {
        List<Employee> employeeList = employeeRepository.findAll();
        List<Employee> returnList = new ArrayList<>();
        for (Employee emp: employeeList) {
            char empChar = emp.getFirst_name().charAt(0);
            empChar = Character.toLowerCase(empChar);
            if (empChar == Character.toLowerCase(c)) {
                returnList.add(emp);
            }
        }
        return returnList;
    }
}
