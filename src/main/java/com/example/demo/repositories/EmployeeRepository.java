package com.example.demo.repositories;

import com.example.demo.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface EmployeeRepository extends MongoRepository<Employee, Integer> {
    @Query(value = "{}", fields = "{ 'access' : 1, '_id': 0}")
    List<String> findAllAccess();
}
