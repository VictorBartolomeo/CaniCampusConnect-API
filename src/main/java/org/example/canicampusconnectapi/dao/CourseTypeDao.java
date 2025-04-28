package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.courseRelated.AgeRange;
import org.example.canicampusconnectapi.model.courseRelated.CourseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseTypeDao extends JpaRepository<CourseType, Long> {
    Optional<CourseType> findByName(String name);
    List<CourseType> findByAgeRange(AgeRange ageRange);
}
