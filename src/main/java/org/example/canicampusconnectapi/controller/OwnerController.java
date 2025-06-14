package org.example.canicampusconnectapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.example.canicampusconnectapi.dao.OwnerDao;
import org.example.canicampusconnectapi.dao.UserDao;
import org.example.canicampusconnectapi.model.users.Owner;
import org.example.canicampusconnectapi.security.AppUserDetails;
import org.example.canicampusconnectapi.security.annotation.role.IsClubOwner;
import org.example.canicampusconnectapi.security.annotation.role.IsOwner;
import org.example.canicampusconnectapi.service.rgpd.RgpdService;
import org.example.canicampusconnectapi.service.security.OwnerSecurityService;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
public class OwnerController {

    protected UserDao userDao;
    protected OwnerDao ownerDao;
    protected OwnerSecurityService ownerSecurityService;
    protected RgpdService rgpdService;

    @Autowired
    public OwnerController(OwnerDao ownerDao, UserDao userDao, OwnerSecurityService ownerSecurityService, RgpdService rgpdService) {
        this.ownerDao = ownerDao;
        this.userDao = userDao;
        this.ownerSecurityService = ownerSecurityService;
        this.rgpdService = rgpdService; // ✅ Injection
    }


    @GetMapping("/owner/{id}")
    @JsonView(OwnerViewDog.class)
    public ResponseEntity<Owner> getOwner(@PathVariable Long id, @AuthenticationPrincipal AppUserDetails userDetails) {

        // Vérification de sécurité
        if (!ownerSecurityService.isOwnerSelfOrAdmin(id, userDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<Owner> optionalOwner = ownerDao.findById(id);
        if (optionalOwner.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalOwner.get(), HttpStatus.OK);
    }

    // GET owners - Accessible uniquement aux admins
    @IsClubOwner
    @GetMapping("/owners")
    public List<Owner> getAll() {
        return ownerDao.findAll();
    }

    // DELETE owner/{id} - Accessible uniquement aux admins
    @IsClubOwner
    @DeleteMapping("/owner/{id}")
    public ResponseEntity<Map<String, String>> deleteOwner(
            @PathVariable Long id,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        try {
            // Vérifier si l'owner existe
            Optional<Owner> optionalOwner = ownerDao.findById(id);
            if (optionalOwner.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "message", "Owner non trouvé",
                        "action", "error"
                ));
            }

            // Vérifier si déjà anonymisé
            if (rgpdService.isAnonymized(Owner.class, id)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "message", "Cet owner est déjà anonymisé",
                        "action", "already_anonymized"
                ));
            }

            rgpdService.anonymizeEntity(Owner.class, id);

            return ResponseEntity.ok(Map.of(
                    "message", "Données personnelles de l'owner anonymisées avec succès (conformité RGPD)",
                    "action", "gdpr_anonymized",
                    "ownerId", id.toString(),
                    "anonymizedBy", userDetails.getUsername()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", "Erreur lors de l'anonymisation RGPD",
                    "action", "error",
                    "details", e.getMessage()
            ));
        }
    }


    @PutMapping("/owner/{id}")
    public ResponseEntity<Owner> updateOwner(@PathVariable Long id,
                                             @RequestBody @Validated(Owner.OnUpdateFromOwner.class) Owner owner,
                                             @AuthenticationPrincipal AppUserDetails userDetails) {

        // Vérification de sécurité
        if (!ownerSecurityService.isOwnerSelfOrAdmin(id, userDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<Owner> ownerOptional = ownerDao.findById(id);
        if (ownerOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Owner existingOwner = ownerOptional.get();

        owner.setId(id);
        owner.setPassword(existingOwner.getPassword());
        owner.setEmailValidated(existingOwner.isEmailValidated());
        owner.setEmailValidatedAt(existingOwner.getEmailValidatedAt());

        ownerDao.save(owner);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Route pour qu'un owner accède à ses propres données (plus lisible)
    @IsOwner
    @GetMapping("/owner/me")
    @JsonView(OwnerViewDog.class)
    public ResponseEntity<Owner> getMyProfile(@AuthenticationPrincipal AppUserDetails userDetails) {
        Optional<Owner> optionalOwner = ownerDao.findById(userDetails.getUserId());
        if (optionalOwner.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalOwner.get(), HttpStatus.OK);
    }
}