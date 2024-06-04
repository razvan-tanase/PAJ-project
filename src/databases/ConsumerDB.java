package databases;

import consumer.Consumer;
import distributors.Distributor;

import java.util.Comparator;
import java.util.List;

/**
 * A database of all consumers which are not broke
 */
public final class ConsumerDB {
    /**
     * List with all consumers
     */
    private final List<Consumer> consumers;

    public ConsumerDB(final List<Consumer> consumers) {
        this.consumers = consumers;
    }

    public List<Consumer> getConsumers() {
        return consumers;
    }

    /**
     * @param consumer A consumer which will be added to the list
     */
    public void addConsumer(final Consumer consumer) {
        consumers.add(consumer);
    }

    /**
     * All consumers go visit the cheapest distributor to sign a contract if they dont have
     * one already and the accountant notes at which one he went to
     *
     * @param distributor The cheapest distributor for whom the consumer can signs a new contract
     * @param accountant  Notes the distributor with whom a consumer has signed
     */
    public void signNewContracts(final Distributor distributor, final Accountant accountant) {
        for (Consumer consumer : consumers) {
            if (distributor.accept(consumer)) {
                accountant.addNewContract(consumer, distributor);
            }
        }
    }

    /**
     * @param accountant Indicates to the consumer whose distributor must pay
     */
    public void payAllRates(final Accountant accountant) {
        for (Consumer consumer : consumers) {
            consumer.payRate(accountant.searchDistributor(consumer), accountant);
        }
    }

    /**
     * Each consumer is checked for bankruptcy, and distributors will remove the
     * contracts of those concerned;
     * Then, all bankrupts will be removed from the list
     *
     * @param accountant Indicates to the consumer whose distributor must pay
     */
    public void removeBankrupts(final Accountant accountant) {
        for (Consumer consumer : consumers) {
            if (consumer.getBlackList().isBankrupt()) {
                Distributor currentDist = accountant.searchDistributor(consumer);
                currentDist.removeBankruptContract(consumer.getConsumerID());
            }
        }
        consumers.removeIf(consumer -> consumer.getBlackList().isBankrupt());
    }

    /**
     * This method is used at the end of the rounds to merge the list of consumers
     *
     * @param accountant Keeps track of bankrupt consumers
     */
    public void mergeLists(final Accountant accountant) {
        consumers.addAll(accountant.getBankruptConsumers());
        Comparator<Consumer> comparator = Comparator.comparing(Consumer::getConsumerID);
        consumers.sort(comparator);
    }
}
