package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.enumeration.RegistrationStatus;
import org.example.canicampusconnectapi.model.users.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DogDao extends JpaRepository<Dog, Long> {
    List<Dog> findByName(String name);

    Optional<Dog> findByChipNumber(String chipNumber);

    List<Dog> findByOwner(Owner owner);

    Dog findUniqueByOwner(Dog dog);

    List<Dog> findByOwnerId(Long ownerId);

    @Query("SELECT d FROM Dog d WHERE d.id = :id AND d.isAnonymized = false")
    Optional<Dog> findByIdAndNotAnonymized(@Param("id") Long id);

    @Query("SELECT d FROM Dog d WHERE d.isAnonymized = false")
    List<Dog> findAllNotAnonymized();

    @Query("SELECT d FROM Dog d WHERE d.id = :dogId AND d.owner.id = :ownerId AND d.isAnonymized = false")
    Optional<Dog> findByIdAndOwnerId(@Param("dogId") Long dogId, @Param("ownerId") Long ownerId);

}
