package org.example.canicampusconnectapi.model.enumeration;

/**
 * Represents the status of a registration for a course.
 * The workflow is as follows:
 * 1. All new registrations start with PENDING status
 * 2. A club owner or coach must validate the registration to change it to CONFIRMED
 * 3. A registration can be CANCELLED at any time by the owner, coach, or club owner
 */
public enum RegistrationStatus {
    /**
     * The registration has been validated by a club owner or coach
     */
    CONFIRMED,

    /**
     * The registration is waiting for validation by a club owner or coach
     */
    PENDING,

    /**
     * The registration has been cancelled
     */
    CANCELLED,

    /**
     * The registration is refused
     */
    REFUSED
}
