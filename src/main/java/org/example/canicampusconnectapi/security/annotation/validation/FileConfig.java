package org.example.canicampusconnectapi.security.annotation.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileConfig {

    @Value("${file.default.accepted.types}")
    private String[] defaultAcceptedTypes;

    public String[] getDefaultAcceptedTypes() {
        return defaultAcceptedTypes;
    }
}