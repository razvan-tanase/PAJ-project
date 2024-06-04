package output;

import java.util.List;

/**
 * This class contains lists of entities whose details will appear in the output files
 */
public final class OutputLoader {
    private final List<Consumers> consumers;

    private final List<Distributors> distributors;

    private final List<EnergyProducers> energyProducers;

    public OutputLoader(final List<Consumers> consumers, final List<Distributors> distributors,
                        final List<EnergyProducers> producers) {
        this.consumers = consumers;
        this.distributors = distributors;
        this.energyProducers = producers;
    }

    public List<Consumers> getConsumers() {
        return consumers;
    }

    public List<Distributors> getDistributors() {
        return distributors;
    }

    public List<EnergyProducers> getEnergyProducers() {
        return energyProducers;
    }
}
