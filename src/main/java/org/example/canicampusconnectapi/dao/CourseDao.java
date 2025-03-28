package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CourseDao extends JpaRepository<Course, Long> {
}