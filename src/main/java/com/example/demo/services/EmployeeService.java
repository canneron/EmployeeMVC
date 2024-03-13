package com.example.demo.services;

import com.example.demo.model.Employee;
import com.example.demo.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public List<Employee> findEmployeeBeginningWithChar(char c) {
        List<Employee> employeeList = employeeRepository.findAll();

        return employeeList.stream().filter(emp->
                Character.toLowerCase(emp.getFirstName().charAt(0)) == Character.toLowerCase(c))
                .collect(Collectors.toList());
    }

    public List<String> findAllAccess() {
        return employeeRepository.findAllAccess();
    }
    public List<String> multipleEmployeeID(int num) {
        List<Employee> employeeList = employeeRepository.findAll();
        return employeeList.stream().map(Employee::getAccess).collect(Collectors.toList());
    }
}
