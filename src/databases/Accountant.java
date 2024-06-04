package databases;

import consumer.Consumer;
import distributors.Distributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Keep track of the taxpayers who have gone bankrupt and the distributors of each consumer
 */
public final class Accountant {
    private final Map<Consumer, Distributor> agenda = new HashMap<>();
    private final List<Consumer> bankruptConsumers = new ArrayList<>();
    private final List<Distributor> bankruptDistributors = new ArrayList<>();

    public Accountant() {
    }

    public List<Consumer> getBankruptConsumers() {
        return bankruptConsumers;
    }

    public List<Distributor> getBankruptDistributors() {
        return bankruptDistributors;
    }

    /**
     * @param consumer    The consumer who signed a new contract
     * @param distributor The distributor with which he signed the contract
     */
    public void addNewContract(final Consumer consumer, final Distributor distributor) {
        agenda.put(consumer, distributor);
    }

    /**
     * @param consumer A consumer from the agenda
     * @return The distributor to whom the given consumer has a contract
     */
    public Distributor searchDistributor(final Consumer consumer) {
        return agenda.get(consumer);
    }

    /**
     * @param consumer A taxpayer which has gone bankrupt
     */
    public void addBankrupt(final Consumer consumer) {
        bankruptConsumers.add(consumer);
    }

    /**
     * @param distributor A taxpayer which has gone bankrupt
     */
    public void addBankrupt(final Distributor distributor) {
        bankruptDistributors.add(distributor);
    }

    /**
     * @param distributor The distributor for whom I want to find the contracted consumer
     * @return The related consumer
     */
    public Consumer getConsumer(final Distributor distributor) {
        for (Map.Entry<Consumer, Distributor> entry : agenda.entrySet()) {
            // if give value is equal to value from entry
            // print the corresponding key
            if (entry.getValue() == distributor) {
                return entry.getKey();
            }
        }
        return null;
    }
}
