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
        Comparator<Producer> comparator1 = Comparator.comparing(Producer::getEnergyPerDistributor);
        producers.sort(comparator1.reversed());
        Comparator<Producer> comparator2 = Comparator.comparing(Producer::getPriceKW);
        producers.sort(comparator2);
        return producers;
    }

    @Override
    public String strategyToString() {
        return EnergyChoiceStrategyType.PRICE.getLabel();
    }
}
