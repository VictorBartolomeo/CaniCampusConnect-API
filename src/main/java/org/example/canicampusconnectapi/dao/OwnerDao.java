package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OwnerDao extends JpaRepository<Owner, Long> {

    Optional<Owner> findByEmail(String email);

}