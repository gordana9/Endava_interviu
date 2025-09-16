package com.example.carins.service;

import com.example.carins.model.Car;
import com.example.carins.model.Claim;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.ClaimRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.ClaimDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final InsurancePolicyRepository policyRepository;
    private final ClaimRepository claimRepository;

    public CarService(CarRepository carRepository, InsurancePolicyRepository policyRepository, ClaimRepository claimRepository) {
        this.carRepository = carRepository;
        this.policyRepository = policyRepository;
        this.claimRepository = claimRepository;
    }

    public List<Car> listCars() {
        return carRepository.findAll();
    }
//check if the string a valid date
    public boolean isDataFormatValid(String input) {  //implementation of data format validation
        if (input == null) {
            return false;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate.parse(input, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    public Car findCarById(Long carId) {
        return carRepository.findById(carId).orElse(null);
    }
    //dto->Claim, save in database
    public Claim saveClaim(ClaimDto newClaim){
        Car car = carRepository.findById(newClaim.carId()).orElseThrow(() ->new NoSuchElementException("Car not found with id " + newClaim.carId()));
        Claim newEntity = toEntity(newClaim, car);
        return claimRepository.save(newEntity);
    }
//
    public List<Claim> getClaimsForCar(Long carId) {
        return claimRepository.findByCar_Id(carId);
    }

    public boolean isInsuranceValid(Long carId, LocalDate date) {
        if (carId == null || date == null) return false;
        // TODO: optionally throw NotFound if car does not exist

        return policyRepository.existsActiveOnDate(carId, date);
    }
    //maping a claim entity to a ClaimDto
    public ClaimDto toDto(Claim claim) {
        if (claim == null) {
            return null;
        }
        return new ClaimDto(
                claim.getId(),
                claim.getCar() != null ? claim.getCar().getId() : null,
                claim.getClaimDate(),
                claim.getDescription(),
                claim.getAmount()
        );
    }
//maping a ClaimDto to a claim entity
    public Claim toEntity(ClaimDto dto, Car car) {
        Claim claim = new Claim();
        claim.setCar(car); // car loaded from DB via CarRepository
        claim.setClaimDate(dto.claim_date());
        claim.setDescription(dto.description());
        claim.setAmount(dto.amount());
        return claim;
    }
}
