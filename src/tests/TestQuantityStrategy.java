package tests;

import org.junit.Test;
import producer.Producer;
import strategies.QuantityStrategy;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestQuantityStrategy {

    // correctly sorts producers by energy per distributor in descending order
    @Test
    public void test_sorts_producers_by_energy_per_distributor_descending() {
        Producer producer1 = new Producer(1, 100);
        Producer producer2 = new Producer(2, 200);
        Producer producer3 = new Producer(3, 150);
        List<Producer> producers = Arrays.asList(producer1, producer2, producer3);
        QuantityStrategy strategy = new QuantityStrategy();
        List<Producer> sortedProducers = strategy.execute(producers);
        assertEquals(200, sortedProducers.get(0).getEnergyPerDistributor());
        assertEquals(150, sortedProducers.get(1).getEnergyPerDistributor());
        assertEquals(100, sortedProducers.get(2).getEnergyPerDistributor());
    }

    // handles producers with zero energy per distributor
    @Test
    public void test_handles_producers_with_zero_energy_per_distributor() {
        Producer producer1 = new Producer(1, 0);
        Producer producer2 = new Producer(2, 200);
        Producer producer3 = new Producer(3, 150);
        List<Producer> producers = Arrays.asList(producer1, producer2, producer3);
        QuantityStrategy strategy = new QuantityStrategy();
        List<Producer> sortedProducers = strategy.execute(producers);
        assertEquals(200, sortedProducers.get(0).getEnergyPerDistributor());
        assertEquals(150, sortedProducers.get(1).getEnergyPerDistributor());
        assertEquals(0, sortedProducers.get(2).getEnergyPerDistributor());
    }

    // handles producers with negative energy per distributor
    @Test
    public void test_handles_producers_with_negative_energy_per_distributor() {
        Producer producer1 = new Producer(1, -50);
        Producer producer2 = new Producer(2, 200);
        Producer producer3 = new Producer(3, 150);
        List<Producer> producers = Arrays.asList(producer1, producer2, producer3);
        QuantityStrategy strategy = new QuantityStrategy();
        List<Producer> sortedProducers = strategy.execute(producers);
        assertEquals(200, sortedProducers.get(0).getEnergyPerDistributor());
        assertEquals(150, sortedProducers.get(1).getEnergyPerDistributor());
        assertEquals(-50, sortedProducers.get(2).getEnergyPerDistributor());
    }
}