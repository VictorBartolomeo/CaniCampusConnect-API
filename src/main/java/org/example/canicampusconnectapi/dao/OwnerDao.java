package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.users.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface OwnerDao extends JpaRepository<Owner, Long> {

    Optional<Owner> findByEmail(String email);

    @Query("SELECT o FROM Owner o WHERE o.isAnonymized = false")
    List<Owner> findAllNotAnonymized();

    @Query("SELECT COUNT(o) FROM Owner o WHERE o.isAnonymized = false")
    long countNotAnonymized();


}