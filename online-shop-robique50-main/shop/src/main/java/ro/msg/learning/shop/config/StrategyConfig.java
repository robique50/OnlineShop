package ro.msg.learning.shop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.strategy.LocationStrategy;
import ro.msg.learning.shop.strategy.MostAbundantStrategy;
import ro.msg.learning.shop.strategy.SingleLocationStrategy;
import ro.msg.learning.shop.strategy.StrategyType;

@RequiredArgsConstructor
@Configuration
public class StrategyConfig {
    @Value("${shop.strategy}")
    private StrategyType strategyType;

    private final SingleLocationStrategy singleLocationStrategy;

    private final MostAbundantStrategy mostAbundantStrategy;

    @Bean
    public LocationStrategy locationStrategy() {
        return switch (strategyType) {
            case SINGLE_LOCATION -> singleLocationStrategy;
            case MOST_ABUNDANT -> mostAbundantStrategy;
        };
    }
}