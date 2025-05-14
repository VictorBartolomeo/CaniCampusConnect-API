package org.example.canicampusconnectapi.dto; // ou un package approprié pour les DTOs

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.healthRecord.VeterinaryVisit;
import org.example.canicampusconnectapi.model.healthRecord.DogWeight; // Supposant que vous avez une entité DogWeight

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DogDashboardDTO {
    private Dog dogInfo; // Ou des champs spécifiques de Dog si vous ne voulez pas tout envoyer
    private List<VeterinaryVisit> recentVeterinaryVisits;
    private List<DogWeight> recentWeights;
    // Ajoutez d'autres listes ou objets pour d'autres informations agrégées
    // Par exemple: private List<UpcomingAppointmentDTO> upcomingAppointments;
}
