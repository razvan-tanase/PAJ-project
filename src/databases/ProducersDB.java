package databases;

import producer.Producer;

import java.util.Comparator;
import java.util.List;


/**
 * A database of all producers
 */
public final class ProducersDB {
    /**
     *
     */
    private final List<Producer> producers;

    public ProducersDB(final List<Producer> producers) {
        this.producers = producers;
    }

    public List<Producer> getProducers() {
        return producers;
    }

    /**
     * The producer is searched in the database by id and its energy supplied will be updated
     *
     * @param producer Producer with changed energy
     */
    public void updateProducers(final Producer producer) {
        for (Producer p : producers) {
            if (p.getId() == producer.getId()) {
                p.setEnergyPerDistributor(producer.getEnergyPerDistributor());
                p.notifySubscribers();
            }
        }
    }

    /**
     * Each manufacturer will update its monthly statistics at the end of this month
     *
     * @param currentMonth Round that is simulated at the current time
     */
    public void updateMonthlyStats(final int currentMonth) {
        for (Producer p : producers) {
            p.updateMonthlyStats(currentMonth);
        }
    }

    /**
     * Sorts the producers by ID so that they can be passed in the correct order in
     * the output files
     */
    public void sortProducers() {
        Comparator<Producer> comparator = Comparator.comparing(Producer::getId);
        producers.sort(comparator);
    }
}
