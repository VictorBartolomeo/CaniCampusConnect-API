package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.CourseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CourseTypeDao extends JpaRepository<CourseType, Long> {

}
