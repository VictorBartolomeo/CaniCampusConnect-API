package org.example.canicampusconnectapi.model.enumeration;

import com.fasterxml.jackson.annotation.JsonView;
import org.example.canicampusconnectapi.view.coach.CoachViewRegistrations;
import org.example.canicampusconnectapi.view.owner.OwnerViewCourse;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;

/**
 * Represents the status of a registration for a course.
 * The workflow is as follows:
 * 1. All new registrations start with PENDING status
 * 2. A club owner or coach must validate the registration to change it to CONFIRMED
 * 3. A registration can be CANCELLED at any time by the owner, coach, or club owner
 */
@JsonView({OwnerViewDog.class, OwnerViewCourse.class, CoachViewRegistrations.class})
public enum RegistrationStatus {
    CONFIRMED,
    PENDING,
    CANCELLED,
    REFUSED
}
