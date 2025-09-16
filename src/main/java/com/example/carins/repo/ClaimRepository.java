package com.example.carins.repo;

import com.example.carins.model.Claim;
import com.example.carins.model.InsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
//B)2.
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByCar_Id(Long carId);
}
