package org.example.canicampusconnectapi.factory;

import org.example.canicampusconnectapi.service.rgpd.strategy.AnonymizationStrategy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;

/**
 * Factory pour obtenir la stratégie d'anonymisation appropriée
 */
@Component
public class AnonymizationStrategyFactory {

    private final List<AnonymizationStrategy> strategies;

    public AnonymizationStrategyFactory(List<AnonymizationStrategy> strategies) {
        this.strategies = strategies.stream()
                .sorted(Comparator.comparingInt(AnonymizationStrategy::getPriority))
                .toList();
    }

    /**
     * Retourne la première stratégie capable de gérer le champ
     */
    public AnonymizationStrategy getStrategy(Field field) {
        return strategies.stream()
                .filter(strategy -> strategy.canHandle(field))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Aucune stratégie d'anonymisation trouvée pour le champ: " + field.getName()
                ));
    }

    /**
     * Retourne toutes les stratégies disponibles
     */
    public List<AnonymizationStrategy> getAllStrategies() {
        return List.copyOf(strategies);
    }
}