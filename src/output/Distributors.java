package output;

import distributors.Contracts;

import java.util.List;

/**
 * Class used exclusively to retain the details of a distributor required to be displayed at output
 */
public final class Distributors {

    private final int id;

    private final long energyNeededKW;

    private final long contractCost;

    private final long budget;

    private final String producerStrategy;

    private final boolean isBankrupt;

    private final List<Contracts> contracts;

    public Distributors(final int id, final long energyNeededKW, final long contractCost,
                        final long budget, final String producerStrategy, final boolean isBankrupt,
                        final List<Contracts> contracts) {
        this.id = id;
        this.energyNeededKW = energyNeededKW;
        this.contractCost = contractCost;
        this.budget = budget;
        this.producerStrategy = producerStrategy;
        this.isBankrupt = isBankrupt;
        this.contracts = contracts;
    }

    public int getId() {
        return id;
    }

    public long getBudget() {
        return budget;
    }

    public boolean getIsBankrupt() {
        return isBankrupt;
    }

    public List<Contracts> getContracts() {
        return contracts;
    }

    public long getEnergyNeededKW() {
        return energyNeededKW;
    }

    public long getContractCost() {
        return contractCost;
    }

    public String getProducerStrategy() {
        return producerStrategy;
    }
}
