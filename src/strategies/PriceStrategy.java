package strategies;

import producer.Producer;

import java.util.Comparator;
import java.util.List;

/**
 *
 */
public final class PriceStrategy implements MarketStrategy {
    @Override
    public List<Producer> execute(final List<Producer> producers) {
        return producers.stream()
                .sorted(Comparator.comparing(Producer::getEnergyPerDistributor).reversed())
                .sorted(Comparator.comparing(Producer::getPriceKW))
                .toList();
    }

    @Override
    public String strategyToString() {
        return EnergyChoiceStrategyType.PRICE.getLabel();
    }
}
