package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.AgeRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgeRangeDao extends JpaRepository<AgeRange, Long> {
    Optional<AgeRange> findByMinAgeAndMaxAge(int minAge, int maxAge);
}