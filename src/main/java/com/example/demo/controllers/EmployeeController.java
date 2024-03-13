package com.example.demo.controllers;

import com.example.demo.model.Employee;
import com.example.demo.model.Office;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.OfficeRepository;
import com.example.demo.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin("*")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<?> list() {
        List<String> returnedList = employeeRepository.findAllAccess();
        if (returnedList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No employees found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(returnedList);
    }

    @GetMapping
    @RequestMapping("/get/{id}")
    public ResponseEntity<?> get(@PathVariable int id) {
        try {
            Employee returned = employeeRepository.findById(id).orElseThrow();
            return ResponseEntity.status(HttpStatus.OK).body(returned);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No employees found");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody @Valid final Employee emp) {
        Employee savedEmployee = employeeRepository.save(emp);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        if (!employeeRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee with ID " + id + " not found");
        }

        employeeRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Employee with ID " + id + " deleted successfully");
    }

    @RequestMapping(value = "/starts/{c}")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> startsWith(@PathVariable char c) {
        List<Employee> returnedList = employeeService.findEmployeeBeginningWithChar(c);
        if (returnedList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No employees found");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(returnedList);
        }
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody @Valid Employee emp) {
        try {
            Employee existingEmp = employeeRepository.findById(id).orElseThrow();
            BeanUtils.copyProperties(emp, existingEmp, "employee_id");
            Employee newEmp = employeeRepository.save(existingEmp);
            return ResponseEntity.status(HttpStatus.OK).body(newEmp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No employees found");
        }
    }
}
