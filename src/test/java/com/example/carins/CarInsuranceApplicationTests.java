package com.example.carins;

import com.example.carins.model.Claim;
import com.example.carins.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CarInsuranceApplicationTests {

    @Autowired
    CarService service;

    @Test
    void insuranceValidityBasic() {
        assertTrue(service.isInsuranceValid(1L, LocalDate.parse("2024-06-01")));
        assertTrue(service.isInsuranceValid(1L, LocalDate.parse("2025-06-01")));
        assertFalse(service.isInsuranceValid(2L, LocalDate.parse("2025-02-01")));
    }
    @Test
    void insuranceFormatValidationTest(){
        assertFalse(service.isDataFormatValid("2025-23-55")); //testarea unui format invalid
        assertTrue(service.isDataFormatValid("2025-09-12")); //testarea unui format valid
    }

    @Test
    void carExistsTest(){
        assertTrue(service.findCarById(1L) != null);
        assertFalse(service.findCarById(5L) != null);
    }
}

