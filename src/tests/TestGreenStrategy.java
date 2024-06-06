package tests;

import entities.EnergyType;
import org.junit.Test;
import producer.Producer;
import strategies.GreenStrategy;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestGreenStrategy {

    // correctly sorts producers by renewable status first
    @Test
    public void test_sorts_by_renewable_status_first() {
        Producer producer1 = new Producer(1, EnergyType.SOLAR, 10, 0.5f, 100);
        Producer producer2 = new Producer(2, EnergyType.COAL, 10, 0.3f, 100);
        List<Producer> producers = List.of(producer1, producer2);
        GreenStrategy strategy = new GreenStrategy();
        List<Producer> sortedProducers = strategy.execute(producers);
        assertEquals(producer1, sortedProducers.get(0));
        assertEquals(producer2, sortedProducers.get(1));
    }

    // handles an empty list of producers without errors
    @Test
    public void test_handles_empty_list() {
        List<Producer> producers = List.of();
        GreenStrategy strategy = new GreenStrategy();
        List<Producer> sortedProducers = strategy.execute(producers);
        assertTrue(sortedProducers.isEmpty());
    }

    // handles a list with a single producer correctly
    @Test
    public void test_handles_single_producer() {
        Producer producer = new Producer(1, EnergyType.SOLAR, 10, 0.5f, 100);
        List<Producer> producers = List.of(producer);
        GreenStrategy strategy = new GreenStrategy();
        List<Producer> sortedProducers = strategy.execute(producers);
        assertEquals(1, sortedProducers.size());
        assertEquals(producer, sortedProducers.getFirst());
    }
}
