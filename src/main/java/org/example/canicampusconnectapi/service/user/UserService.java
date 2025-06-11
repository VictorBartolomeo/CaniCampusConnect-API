package org.example.canicampusconnectapi.service.user;

public interface UserService {
    void deleteUser(Long id);
    boolean isUserAnonymized(Long id);
}