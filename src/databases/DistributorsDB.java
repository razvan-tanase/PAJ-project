package databases;

import consumer.Consumer;
import distributors.Distributor;
import producer.Producer;

import java.util.Comparator;
import java.util.List;

/**
 * A database of all distributors which are not bankrupt
 */
public final class DistributorsDB {
    /**
     * List with all distributors
     */
    private final List<Distributor> distributors;

    public DistributorsDB(final List<Distributor> distributors) {
        this.distributors = distributors;
    }

    public List<Distributor> getDistributors() {
        return distributors;
    }

    /**
     * The distributor is searched in the database by id and its costs will be updated
     *
     * @param distributor Distributor with changed costs
     */
    public void updateDistributor(final Distributor distributor) {
        distributors.stream()
                .filter(d -> d.getId() == distributor.getId())
                .forEach(d -> d.setInfrastructureCost(distributor.getInfrastructureCost()));
    }

    /**
     * Every distributor who has been notified of a change or who has not yet chosen a manufacturer
     * will now apply their supplier search strategy.
     *
     * @param producers List of all manufacturers that will be selected to purchase energy
     */
    public void searchProducers(final List<Producer> producers) {
        distributors.stream()
                .sorted(Comparator.comparing(Distributor::getId))
                .filter(d -> d.getProductionCost() == 0)
                .forEach(d -> d.executeStrategy(producers));
    }

    /**
     * @return The distributor with the cheapest contract
     */
    public Distributor getCheapestContract() {
        return distributors.stream()
                .sorted(Comparator.comparing(Distributor::getId))
                .min(Comparator.comparing(Distributor::getContractPrice))
                .orElse(null);
    }

    /**
     * Pay the taxes for all distributors
     */
    public void payAllRates() {
        distributors.forEach(Distributor::calculateNewBudget);
    }

    /**
     * This will be done at the start of every round;
     * The new contracts are calculated according to how many consumers each distributor has
     */
    public void updateContractsPrice() {
        distributors.forEach(Distributor::updateContract);
    }

    /**
     * Each distributor will eliminate the concluded contracts
     */
    public void removeContracts() {
        distributors.forEach(Distributor::removeFinishedContracts);
    }

    /**
     * Each distributor will be checked for bankruptcy, and those in question will have
     * their contracts deleted, and the consumers with whom they concluded will be notified
     * that they must find a new distributor for the next round.
     * Manufacturers who supply it with energy will also be notified to remove it from the
     * list of subscribers
     * <p>
     * Then, it will be added in a list with all bankrupts and removed from the current one
     *
     * @param accountant Keeps track of bankrupt distributors
     */
    public void removeBankrupts(final Accountant accountant) {
        List<Distributor> bankruptDistributors = distributors.stream()
                .filter(Distributor::isBankrupt)
                .toList();

        for (Distributor distributor : bankruptDistributors) {
            Consumer consumer = accountant.getConsumer(distributor);
            if (consumer != null) {
                consumer.setMonthlyExpenses(0);
            }
            accountant.addBankrupt(distributor);
            distributor.getContracts().clear();
            distributor.getContractedProducers().forEach(producer -> producer.unsubscribe(distributor));
        }

        distributors.removeAll(bankruptDistributors);
    }

    /**
     * This method is used at the end of the rounds to merge the list of distributors
     *
     * @param accountant Keeps track of bankrupt distributors
     */
    public void mergeLists(final Accountant accountant) {
        distributors.addAll(accountant.getBankruptDistributors());
        distributors.sort(Comparator.comparing(Distributor::getId));
    }
}
