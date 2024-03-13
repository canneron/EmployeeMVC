package com.example.demo.controllers;

import com.example.demo.model.Employee;
import com.example.demo.model.Office;
import com.example.demo.repositories.OfficeRepository;
import com.example.demo.services.OfficeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.lang.System.*;

@RestController
@RequestMapping("/api/office")
@CrossOrigin("*")
public class OfficeController {

    @Autowired
    OfficeRepository officeRepository;

    @Autowired
    OfficeService officeService;

    @GetMapping
    public ResponseEntity<?> list() {
        List<Office> returnedList = officeRepository.findAll();
        if (returnedList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No employees found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(returnedList);
    }

    @GetMapping
    @RequestMapping("/get/{location}")
    public ResponseEntity<?> get(@PathVariable String location) {
        try {
            var returned = officeRepository.findByOfficeLocation(location);
            return ResponseEntity.status(HttpStatus.OK).body(returned);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No employees found");
        }
    }

    @GetMapping
    @RequestMapping("/get/manager/{location}")
    public ResponseEntity<?> getSize(@PathVariable String location) {
        try {
            String returned = officeService.returnOfficeManager(location);
            returned.lines().forEach(out::println);
            ObjectMapper o = new ObjectMapper();
            Employee manager = o.readValue(returned, Employee.class);
            return ResponseEntity.status(HttpStatus.OK).body(manager);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No employees found");
        }
    }
}

