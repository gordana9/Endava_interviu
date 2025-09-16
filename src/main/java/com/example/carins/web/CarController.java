package com.example.carins.web;

import com.example.carins.model.Car;
import com.example.carins.model.Claim;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.ClaimRepository;
import com.example.carins.service.CarService;
import com.example.carins.web.dto.CarDto;
import com.example.carins.web.dto.ClaimDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CarController {

    private final CarService service;

    public CarController(CarService service, ClaimRepository claimRepository, CarRepository carRepo) {
        this.service = service;
    }

    @GetMapping("/cars")
    public List<CarDto> getCars() {
        return service.listCars().stream().map(this::toDto).toList();
    }

    @GetMapping("/cars/{carId}/insurance-valid")
    public ResponseEntity<?> isInsuranceValid(@PathVariable Long carId, @RequestParam String date) {
        // TODO: validate date format and handle errors consistently
        //verifying if data format is valid & carId exists
        if(service.findCarById(carId) == null) {
            return ResponseEntity.badRequest().body("Car not found"); //
        }
        if (!service.isDataFormatValid(date)) {
            return ResponseEntity.badRequest().body("invalid data format"); //
        }
        LocalDate d = LocalDate.parse(date);
        boolean valid = service.isInsuranceValid(carId, d);
        return ResponseEntity.ok(new InsuranceValidityResponse(carId, d.toString(), valid));

    }
    //B)1.endpoint for registering a nex claim for a car
    //for first, I check the input, then create a ClaimDto
    @PostMapping("/cars/{carId}/claims")
    public ResponseEntity<?> registerClaim(@PathVariable Long carId,
                                  @RequestBody LocalDate claimDate,
                                  @RequestBody String description,
                                  @RequestBody float amount
        ) {
        if (service.isDataFormatValid(claimDate.toString())) {
            return ResponseEntity.badRequest().body("claimDate is required");
        }
        if (description == null || description.isBlank()) {
            return ResponseEntity.badRequest().body("description must not be null or empty");
        }
        if (amount <= 0) {
            return ResponseEntity.badRequest().body("amount must be greater than 0");
        }
        ClaimDto newClaim = new ClaimDto(Math.abs(UUID.randomUUID().getMostSignificantBits()), carId, claimDate,  description, BigDecimal.valueOf(amount));
        var saved = service.saveClaim(newClaim);
        java.net.URI location = java.net.URI.create("/api/cars/%d/claims/%d".formatted(carId, saved.getId()));
        return ResponseEntity.created(location).body(newClaim);
    }
//B)2. endpoint that returns the claim history for a given carId
    @GetMapping("/cars/{carId}/history")
    public List<ClaimDto> getHistory(@PathVariable Long carId) {
        List<Claim> claims = service.getClaimsForCar(carId);
        List<ClaimDto> claimDtos = new ArrayList<>();
        for (Claim claim : claims) {
            claimDtos.add(service.toDto(claim));
        }
        return claimDtos;
    }

    private CarDto toDto(Car c) {
        var o = c.getOwner();
        return new CarDto(c.getId(), c.getVin(), c.getMake(), c.getModel(), c.getYearOfManufacture(),
                o != null ? o.getId() : null,
                o != null ? o.getName() : null,
                o != null ? o.getEmail() : null);
    }

    public record InsuranceValidityResponse(Long carId, String date, boolean valid) {

    }
}
