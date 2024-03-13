package com.example.demo.services;

import com.example.demo.exception.EmployeeException;
import com.example.demo.model.Office;
import com.example.demo.repositories.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfficeService {

    @Autowired
    OfficeRepository officeRepository;

    public String returnOfficeManager(String location) {

        Office loc = officeRepository.findByOfficeLocation(location);
        loc.officeId();
        int cap = loc.capacity();
        return switch (cap) {
            case  100 -> """
                    {
                        "employee_id" : 100,
                        "first_name" : "Paul",
                        "last_name" : "Banks",
                        "title" : "Mr",
                        "access" : "Manager"
                    }
                    """;
            case 8 -> """
                    {
                        "employee_id" : 101,
                        "first_name" : "Ian",
                        "last_name" : "Curtis",
                        "title" : "Mr",
                        "access" : "Manager"
                    }
                    """;
            default -> throw new EmployeeException("EMP101", "No Employee Found");
        };
    }

    //public List<Office> get
}
