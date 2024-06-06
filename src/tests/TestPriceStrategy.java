package tests;

import entities.EnergyType;
import org.junit.Test;
import producer.Producer;
import strategies.PriceStrategy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestPriceStrategy {

    // Producers are sorted by price in ascending order
    @Test
    public void test_producers_sorted_by_price_ascending() {
        Producer producer1 = new Producer(1, EnergyType.SOLAR, 10, 5.0f, 100);
        Producer producer2 = new Producer(2, EnergyType.WIND, 10, 3.0f, 100);
        Producer producer3 = new Producer(3, EnergyType.HYDRO, 10, 4.0f, 100);
        List<Producer> producers = List.of(producer1, producer2, producer3);
        PriceStrategy priceStrategy = new PriceStrategy();
        List<Producer> sortedProducers = priceStrategy.execute(producers);
        assertEquals(2, sortedProducers.get(0).getId());
        assertEquals(3, sortedProducers.get(1).getId());
        assertEquals(1, sortedProducers.get(2).getId());
    }

    // The List of producers is empty
    @Test
    public void test_empty_producer_list() {
        List<Producer> producers = new ArrayList<>();
        PriceStrategy priceStrategy = new PriceStrategy();
        List<Producer> sortedProducers = priceStrategy.execute(producers);
        assertTrue(sortedProducers.isEmpty());
    }

    // All producers have the same price and energy per distributor
    @Test
    public void test_same_price_and_energy_per_distributor() {
        Producer producer1 = new Producer(1, EnergyType.SOLAR, 10, 5.0f, 100);
        Producer producer2 = new Producer(2, EnergyType.WIND, 10, 5.0f, 100);
        Producer producer3 = new Producer(3, EnergyType.HYDRO, 10, 5.0f, 100);
        List<Producer> producers = List.of(producer1, producer2, producer3);
        PriceStrategy priceStrategy = new PriceStrategy();
        List<Producer> sortedProducers = priceStrategy.execute(producers);
        assertEquals(1, sortedProducers.get(0).getId());
        assertEquals(2, sortedProducers.get(1).getId());
        assertEquals(3, sortedProducers.get(2).getId());
    }
}
