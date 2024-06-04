package input;

import consumer.Consumer;
import distributors.Distributor;
import producer.Producer;

import java.util.List;

/**
 * The class contains information about input
 */
public final class Input {
    /**
     * How many rounds will be simulated in a game
     */
    private final int numberOfTurns;
    /**
     * List of consumers
     */
    private final List<Consumer> consumers;
    /**
     * List of distributors
     */
    private final List<Distributor> distributors;
    /**
     * List of producers
     */
    private final List<Producer> producers;
    /**
     * A list of consumers that will be added during the game
     */
    private final List<List<Consumer>> newConsumers;
    /**
     * A list of distributors that will be added during the game
     */
    private final List<List<Distributor>> newDistributors;
    /**
     * A list of producers that will be added during the game
     */
    private final List<List<Producer>> newProducers;

    public Input(final List<Consumer> consumers, final List<Distributor> distributors,
                 final List<Producer> producers, final List<List<Consumer>> newConsumers,
                 final List<List<Distributor>> newDistributors, final int numberOfTurns,
                 final List<List<Producer>> newProducers) {
        this.consumers = consumers;
        this.distributors = distributors;
        this.producers = producers;
        this.newConsumers = newConsumers;
        this.newDistributors = newDistributors;
        this.numberOfTurns = numberOfTurns;
        this.newProducers = newProducers;
    }

    public List<Consumer> getConsumers() {
        return consumers;
    }

    public List<Distributor> getDistributors() {
        return distributors;
    }

    public List<Producer> getProducers() {
        return producers;
    }

    public List<List<Consumer>> getNewConsumers() {
        return newConsumers;
    }

    public List<List<Distributor>> getNewDistributors() {
        return newDistributors;
    }

    public List<List<Producer>> getNewProducers() {
        return newProducers;
    }

    public int getNumberOfTurns() {
        return numberOfTurns;
    }
}
