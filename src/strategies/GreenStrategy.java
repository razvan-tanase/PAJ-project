package strategies;

import producer.Producer;

import java.util.Comparator;
import java.util.List;

public final class GreenStrategy implements MarketStrategy {
    @Override
    public List<Producer> execute(final List<Producer> producers) {
        return producers.stream()
                .sorted(Comparator.comparing(Producer::getId))
                .sorted(Comparator.comparing(Producer::getEnergyPerDistributor).reversed())
                .sorted(Comparator.comparing(Producer::getPriceKW))
                .sorted(Comparator.comparing(Producer::getIsRenewable).reversed())
                .toList();
    }

    @Override
    public String strategyToString() {
        return EnergyChoiceStrategyType.GREEN.getLabel();
    }
}
