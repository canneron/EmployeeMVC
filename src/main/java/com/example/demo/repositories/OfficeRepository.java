package com.example.demo.repositories;

import com.example.demo.model.Office;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface OfficeRepository extends MongoRepository<Office, Integer> {

    Office findByOfficeLocation(String location);

    @Query(value = "{}", fields = "{ 'date' : 1, '_id': 0}")
    List<Office> getAllOfficeLocation();
}
