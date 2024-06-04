package strategies;

import producer.Producer;

import java.util.List;

/**
 * This interface is used to implement strategy design pattern
 */
public interface MarketStrategy {
    /**
     * @param producers The list of manufacturers from which the distributor will choose
     *                  its new energy supplier
     * @return List sorted according to the distributor's interest of all producers
     * that can be selected for energy supply
     */
    List<Producer> execute(List<Producer> producers);

    /**
     * @return A string with the name of the strategy used to display it in the output files
     */
    String strategyToString();
}
