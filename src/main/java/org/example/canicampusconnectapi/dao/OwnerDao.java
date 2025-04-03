package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.Course;
import org.example.canicampusconnectapi.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OwnerDao extends JpaRepository<Owner, Long> {
}