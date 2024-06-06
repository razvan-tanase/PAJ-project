package strategies;

import producer.Producer;

import java.util.Comparator;
import java.util.List;

public final class QuantityStrategy implements MarketStrategy {
    @Override
    public List<Producer> execute(final List<Producer> producers) {
        return producers.stream()
                .sorted(Comparator.comparing(Producer::getEnergyPerDistributor).reversed())
                .toList();
    }

    @Override
    public String strategyToString() {
        return EnergyChoiceStrategyType.QUANTITY.getLabel();
    }
}
