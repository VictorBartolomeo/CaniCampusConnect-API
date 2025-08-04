
package org.example.canicampusconnectapi.dogAccess;

import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.users.Owner;
import org.example.canicampusconnectapi.model.users.User;
import org.example.canicampusconnectapi.service.dog.DogService;
import org.example.canicampusconnectapi.common.exception.ResourceNotFoundException;
import org.example.canicampusconnectapi.common.exception.UnauthorizedAccessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class DogTest {

    @Mock
    private DogService dogService;
    private Owner owner1;
    private Owner owner2;
    private Dog dog1;
    private Dog dog2;

    @BeforeEach
    void setUp() {
        owner1 = new Owner();
        owner1.setId(1L);owner1.setEmail("john@example.com");owner1.setFirstname("John");owner1.setLastname("Doe");

        owner2 = new Owner();
        owner2.setId(2L);owner2.setEmail("marie@example.com");owner2.setFirstname("Marie");owner2.setLastname("Martin");

        dog1 = new Dog();
        dog1.setId(1L);dog1.setName("Rex");dog1.setOwner(owner1);

        dog2 = new Dog();
        dog2.setId(2L);dog2.setName("Luna");dog2.setOwner(owner1);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void proprietaireAvecChiens_retourneListeChiens() {
        List<Dog> chiensAttendus = Arrays.asList(dog1, dog2);
        when(dogService.getDogsByOwner(1L)).thenReturn(chiensAttendus);

        List<Dog> result = dogService.getDogsByOwner(1L);

        assertEquals(2, result.size());
        assertEquals("Rex", result.get(0).getName());
        assertEquals("Luna", result.get(1).getName());
    }

    @Test
    void chienNAppartientPasAProprietaire_leveUnauthorizedAccessException() {
        when(dogService.getDogByIdAndOwnerId(1L, 2L))
                .thenThrow(new UnauthorizedAccessException("Vous n'avez pas l'autorisation d'accéder à ce chien"));

        assertThrows(UnauthorizedAccessException.class, () -> {
            dogService.getDogByIdAndOwnerId(1L, 2L);
        });
    }

    @Test
    void proprietaireInexistant_leveResourceNotFoundException() {
        when(dogService.getDogsByOwner(999L))
                .thenThrow(new ResourceNotFoundException("Owner not found with id: 999"));

        assertThrows(ResourceNotFoundException.class, () -> {
            dogService.getDogsByOwner(999L);
        });
    }

    @Test
    void proprietaireSansChiens_retourneListeVide() {
        when(dogService.getDogsByOwner(2L)).thenReturn(Collections.emptyList());

        List<Dog> result = dogService.getDogsByOwner(2L);

        assertTrue(result.isEmpty());
    }


    @Test
    void chienAppartientAProprietaire_retourneChien() {
        // Arrange
        when(dogService.getDogByIdAndOwnerId(1L, 1L)).thenReturn(Optional.of(dog1));

        // Act
        Optional<Dog> result = dogService.getDogByIdAndOwnerId(1L, 1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Rex", result.get().getName());
        assertEquals(1L, result.get().getOwner().getId());
    }



    @Test
    void chienInexistant_leveResourceNotFoundException() {
        // Arrange
        when(dogService.getDogById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Dog> result = dogService.getDogById(999L);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void creerChienAvecProprietaire_retourneChienCree() {
        // Arrange
        Dog nouveauChien = new Dog();
        nouveauChien.setName("Buddy");
        nouveauChien.setOwner(owner1);

        Dog chienCree = new Dog();
        chienCree.setId(3L);
        chienCree.setName("Buddy");
        chienCree.setOwner(owner1);

        when(dogService.createDog(nouveauChien)).thenReturn(chienCree);

        // Act
        Dog result = dogService.createDog(nouveauChien);

        // Assert
        assertNotNull(result.getId());
        assertEquals("Buddy", result.getName());
        assertEquals(owner1, result.getOwner());
    }

    @Test
    void supprimerChien_retourneSucces() {
        // Arrange
        doNothing().when(dogService).deleteDog(1L);

        // Act & Assert
        assertDoesNotThrow(() -> dogService.deleteDog(1L));
        verify(dogService, times(1)).deleteDog(1L);
    }

    @Test
    void chienAnonyme_retourneTrue() {
        // Arrange
        when(dogService.isDogAnonymized(1L)).thenReturn(true);

        // Act
        boolean result = dogService.isDogAnonymized(1L);

        // Assert
        assertTrue(result);
    }

    @Test
    void calculerAgeChien_retourneAgeEnMois() {
        // Arrange
        LocalDate dateNaissance = LocalDate.now().minusMonths(18);
        when(dogService.calculateAgeInMonths(dateNaissance)).thenReturn(18L);

        // Act
        long ageEnMois = dogService.calculateAgeInMonths(dateNaissance);

        // Assert
        assertEquals(18L, ageEnMois);
    }
}