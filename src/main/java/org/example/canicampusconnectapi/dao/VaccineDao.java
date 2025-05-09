package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.healthRecord.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface VaccineDao extends JpaRepository<Vaccine, Integer> {
    Optional<Vaccine> findByName(String name);
    List<Vaccine> findByRenewDelayLessThan(short date);
    List<Vaccine> findByRenewDelayGreaterThan(short date);
}