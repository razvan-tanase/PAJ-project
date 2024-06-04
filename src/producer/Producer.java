package producer;

import distributors.Subscriber;
import entities.EnergyType;

import java.util.ArrayList;
import java.util.List;

/**
 * The entity that supplies energy to distributors
 */
public final class Producer extends Provider {
    /**
     * Producer id
     */
    private final int id;
    /**
     * The type of energy it produces
     */
    private EnergyType energyType;
    /**
     * Whether it is renewable or not
     */
    private boolean isRenewable;
    /**
     * The maximum number of distributors it supports
     */
    private long maxDistributors;
    /**
     * The price of energy
     */
    private float priceKW;
    /**
     * The energy it can provide to each distributor
     */
    private long energyPerDistributor;
    /**
     * Monthly statistics
     */
    private List<MonthlyStats> monthlyStats;

    public Producer(final int id, final EnergyType energyType, final long maxDistributors,
                    final float priceKW, final long energyPerDistributor) {
        super();
        this.id = id;
        this.energyType = energyType;
        this.isRenewable = energyType.isRenewable();
        this.maxDistributors = maxDistributors;
        this.priceKW = priceKW;
        this.energyPerDistributor = energyPerDistributor;
        this.monthlyStats = new ArrayList<>();
    }

    public Producer(final int id, final long energyPerDistributor) {
        this.id = id;
        this.energyPerDistributor = energyPerDistributor;
    }

    public int getId() {
        return id;
    }

    public EnergyType getEnergyType() {
        return energyType;
    }

    public long getMaxDistributors() {
        return maxDistributors;
    }

    public float getPriceKW() {
        return priceKW;
    }

    public long getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public List<MonthlyStats> getMonthlyStats() {
        return monthlyStats;
    }

    public boolean getIsRenewable() {
        return isRenewable;
    }

    public void setEnergyPerDistributor(final long energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }

    /**
     * @return True if distributors can still subscribe
     */
    public boolean checkAvailability() {
        return maxDistributors > getSubscribers().size();
    }

    /**
     * Retrieve from the list of subscribers the ID of all distributors to whom it supplies energy
     * in order to be able to update monthly statistics
     *
     * @return List of IDs of distributors to whom it provides energy in the current month
     */
    public List<Integer> getSubscribersID() {
        List<Integer> IDs = new ArrayList<>();
        for (Subscriber subscriber : getSubscribers()) {
            IDs.add(subscriber.getId());
        }
        IDs.sort(Integer::compareTo);
        return IDs;
    }

    /**
     * Update statistics for the current month
     *
     * @param currentMonth The round in which the simulation is
     */
    public void updateMonthlyStats(final int currentMonth) {
        monthlyStats.add(new MonthlyStats(currentMonth, getSubscribersID()));
    }
}
