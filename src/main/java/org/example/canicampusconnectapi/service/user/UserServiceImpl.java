package org.example.canicampusconnectapi.service.user;

import org.example.canicampusconnectapi.model.users.User;
import org.example.canicampusconnectapi.service.rgpd.RgpdService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final RgpdService rgpdService;

    public UserServiceImpl(RgpdService rgpdService) {
        this.rgpdService = rgpdService;
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        rgpdService.anonymizeEntity(User.class, id);
    }

    @Override
    public boolean isUserAnonymized(Long id) {
        return rgpdService.isAnonymized(User.class, id);
    }
}