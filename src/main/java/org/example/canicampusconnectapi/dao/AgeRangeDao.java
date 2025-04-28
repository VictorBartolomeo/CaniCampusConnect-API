package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.AgeRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgeRangeDao extends JpaRepository<AgeRange, Long> {
    Optional<AgeRange> findByMinAgeAndMaxAge(int minAge, int maxAge);

    // Find age ranges that include a specific age
    List<AgeRange> findByMinAgeLessThanEqualAndMaxAgeGreaterThanEqual(int age, int sameAge);

    // Find age ranges for older dogs
    List<AgeRange> findByMinAgeGreaterThanEqual(int minAge);

    // Find age ranges for younger dogs
    List<AgeRange> findByMaxAgeLessThanEqual(int maxAge);
}
