package strategies;

import producer.Producer;

import java.util.Comparator;
import java.util.List;

public final class GreenStrategy implements MarketStrategy {
    @Override
    public List<Producer> execute(final List<Producer> producers) {
        Comparator<Producer> comparator = Comparator.comparing(Producer::getId);
        producers.sort(comparator);
        Comparator<Producer> comparator1 = Comparator.comparing(Producer::getEnergyPerDistributor);
        producers.sort(comparator1.reversed());
        Comparator<Producer> comparator2 = Comparator.comparing(Producer::getPriceKW);
        producers.sort(comparator2);
        Comparator<Producer> comparator3 = Comparator.comparing(Producer::getIsRenewable);
        producers.sort(comparator3.reversed());
        return producers;
    }

    @Override
    public String strategyToString() {
        return EnergyChoiceStrategyType.GREEN.getLabel();
    }


}
