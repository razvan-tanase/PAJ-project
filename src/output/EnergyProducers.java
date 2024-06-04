package output;

import entities.EnergyType;
import producer.MonthlyStats;

import java.util.List;

/**
 * Class used exclusively to retain the details of a producer required to be displayed at output
 */
public final class EnergyProducers {

    private final int id;

    private final long maxDistributors;

    private final float priceKW;

    private final EnergyType energyType;

    private final long energyPerDistributor;

    private final List<MonthlyStats> monthlyStats;

    public EnergyProducers(final int id, final EnergyType energyType, final long maxDistributors,
                           final float priceKW, final long energyPerDistributor,
                           final List<MonthlyStats> monthlyStats) {
        this.id = id;
        this.energyType = energyType;
        this.maxDistributors = maxDistributors;
        this.priceKW = priceKW;
        this.energyPerDistributor = energyPerDistributor;
        this.monthlyStats = monthlyStats;
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
}
