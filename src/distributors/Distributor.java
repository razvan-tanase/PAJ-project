package distributors;

import consumer.Consumer;
import producer.Producer;
import strategies.MarketStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * The entity that concludes contracts as an energy distributor
 */
public final class Distributor implements Subscriber {
    /**
     * Distributor id
     */
    private final int id;
    /**
     * Length of the contract
     */
    private int contractLength;
    /**
     * Distributor budget
     */
    private long budget;
    /**
     * Cost of the infrastructure
     */
    private long infrastructureCost;
    /**
     * Required monthly energy
     */
    private long energyNeededKW;
    /**
     * Producer choice strategy
     */
    private MarketStrategy producerStrategy;
    /**
     * Cost of the production
     */
    private long productionCost;
    /**
     * Contract price
     */
    private long contractPrice;
    /**
     * The list of contracts concluded with consumers
     */
    private List<Contracts> contracts;
    /**
     * The financial situation of a distributor
     */
    private boolean bankrupt;
    /**
     * List of manufacturers that supply current energy
     */
    private List<Producer> contractedProducers;

    public Distributor(final int id, final int contractLength, final long budget,
                       final long infrastructureCost, final long energyNeededKW,
                       final MarketStrategy producerStrategy) {
        this.id = id;
        this.contractLength = contractLength;
        this.budget = budget;
        this.infrastructureCost = infrastructureCost;
        this.productionCost = 0;
        this.energyNeededKW = energyNeededKW;
        this.producerStrategy = producerStrategy;
        this.bankrupt = false;
        this.contracts = new ArrayList<>();
        this.contractedProducers = new ArrayList<>();
    }

    public Distributor(final int id, final long infrastructureCost) {
        this.id = id;
        this.infrastructureCost = infrastructureCost;
    }

    public int getId() {
        return id;
    }

    public int getContractLength() {
        return contractLength;
    }

    public long getBudget() {
        return budget;
    }

    public long getInfrastructureCost() {
        return infrastructureCost;
    }

    public long getEnergyNeededKW() {
        return energyNeededKW;
    }

    public MarketStrategy getProducerStrategy() {
        return producerStrategy;
    }

    public long getProductionCost() {
        return productionCost;
    }

    public long getContractPrice() {
        return contractPrice;
    }

    public List<Contracts> getContracts() {
        return contracts;
    }

    public void setInfrastructureCost(final long infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }

    public boolean isBankrupt() {
        return bankrupt;
    }

    public List<Producer> getContractedProducers() {
        return contractedProducers;
    }

    /**
     * The distributor will set his production cost to 0 to signal that he must reapply
     * his strategy to find producers.
     */
    @Override
    public void update() {
        productionCost = 0;
    }

    /**
     * The manufacturers that have the greatest interest for the current distributor will
     * be checked if they have reached the maximum of distributors.
     * If they are valid, the distributor will subscribe to them and calculate the cost of
     * energy they charge.
     *
     * @param producers List sorted according to the distributor's interest of all producers
     *                  that can be selected for energy supply
     * @return Production cost
     */
    public long calculateProductionCost(final List<Producer> producers) {
        long cost = 0;
        long energyFound = 0;
        for (int i = 0; energyFound < energyNeededKW; i++) {
            Producer producer = producers.get(i);
            if (producer.checkAvailability()) {
                producer.subscribe(this);
                contractedProducers.add(producer);
                energyFound += producer.getEnergyPerDistributor();
                cost += producer.getEnergyPerDistributor() * producer.getPriceKW();
            }
        }

        return Math.round(Math.floor((float) cost / 10));
    }

    /**
     * Before executing the strategy of finding new energy suppliers, the distributor will
     * unsubscribe from the old producers because he does not know if they will remain the
     * same after the new search;
     * Finally, the production cost for the current month is calculated
     *
     * @param producers The list of manufacturers from which the distributor will choose
     *                  its new energy supplier
     */
    public void executeStrategy(final List<Producer> producers) {
        for (Producer p : contractedProducers) {
            p.unsubscribe(this);
        }
        contractedProducers.clear();
        productionCost = calculateProductionCost(producerStrategy.execute(producers));
    }

    /**
     * @return Monthly expenses of the distributor
     */
    public long getMonthlyExpenses() {
        return infrastructureCost + productionCost * contracts.size();
    }

    /**
     * @param contractValue The money that the distributor receives from the consumer
     */
    public void getRate(final long contractValue) {
        budget += contractValue;
    }

    /**
     * It pays the monthly fees and if he has no money, he is declared bankrupt
     */
    public void calculateNewBudget() {
        budget -= getMonthlyExpenses();
        if (budget < 0) {
            bankrupt = true;
        }
    }

    /**
     * @param contract The contract with the consumer that will be added to the list
     */
    public void addContract(final Contracts contract) {
        this.contracts.add(contract);
    }

    /**
     * @param visitor The consumer who visits him to sign a potential new contract
     * @return True if the consumer has signed a new contract, false otherwise
     */

    public boolean accept(final Consumer visitor) {
        return visitor.visit(this);
    }

    /**
     * The price of the contracts will change at the beginning of each round depending on the
     * number of consumers with whom he has a contract or the change in costs of infrastructure
     */
    public void updateContract() {
        long profit = Math.round(Math.floor(0.2 * productionCost));
        if (contracts.size() != 0) {
            contractPrice = Math.round(Math.floor((float) infrastructureCost / contracts.size())
                    + productionCost + profit);
        } else {
            contractPrice = infrastructureCost + productionCost + profit;
        }
    }

    /**
     * The consumer is searched in the list of contracts by id and it is checked if the remaining
     * payment months have reached 0
     *
     * @param consumerID The id after which a consumer will be searched in the list of contracts
     * @return True if the consumer no longer has to pay for that contract
     */
    public boolean searchContract(final int consumerID) {
        for (Contracts contract : this.contracts) {
            if (contract.getConsumerId() == consumerID) {
                return contract.endOfContract();
            }
        }
        return false;
    }

    /**
     * @param consumerID The id after which a consumer will be searched in the list of contracts
     */
    public void removeBankruptContract(final int consumerID) {
        contracts.removeIf(contract -> contract.getConsumerId() == consumerID);
    }

    /**
     * If the consumer no longer has to pay for that contract, remove the contract
     */
    public void removeFinishedContracts() {
        contracts.removeIf(contract -> contract.getRemainedContractMonths() == 0);
    }
}
