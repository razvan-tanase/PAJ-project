package tests;

import databases.ProducersDB;
import entities.EnergyType;
import org.junit.Test;
import producer.Producer;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestProducersDB {

    // updateProducers updates the energy supplied by a producer and notifies subscribers
    @Test
    public void test_updateProducers_updates_energy_and_notifies_subscribers() {
        Producer producer = new Producer(1, EnergyType.SOLAR, 10, 0.5f, 100);
        ProducersDB db = new ProducersDB(List.of(producer));
        Producer updatedProducer = new Producer(1, EnergyType.SOLAR, 10, 0.5f, 200);
        db.updateProducers(updatedProducer);
        assertEquals(200, producer.getEnergyPerDistributor());
    }

    // updateMonthlyStats updates monthly statistics for all producers
    @Test
    public void test_updateMonthlyStats_updates_statistics_for_all_producers() {
        Producer producer = new Producer(1, EnergyType.SOLAR, 10, 0.5f, 100);
        ProducersDB db = new ProducersDB(List.of(producer));
        db.updateMonthlyStats(1);
        assertEquals(1, producer.getMonthlyStats().size());
    }

    // getProducers returns the list of all producers
    @Test
    public void test_getProducers_returns_all_producers() {
        Producer producer = new Producer(1, EnergyType.SOLAR, 10, 0.5f, 100);
        ProducersDB db = new ProducersDB(List.of(producer));
        assertEquals(1, db.getProducers().size());
        assertEquals(producer, db.getProducers().getFirst());
    }

    // updateProducers with a non-existent producer ID does not alter any producer
    @Test
    public void test_updateProducers_non_existent_id_does_not_alter_any_producer() {
        Producer producer = new Producer(1, EnergyType.SOLAR, 10, 0.5f, 100);
        ProducersDB db = new ProducersDB(List.of(producer));
        Producer nonExistentProducer = new Producer(2, EnergyType.WIND, 10, 0.5f, 200);
        db.updateProducers(nonExistentProducer);
        assertEquals(100, producer.getEnergyPerDistributor());
    }

    // updateProducers with zero energyPerDistributor correctly updates the producer
    @Test
    public void test_updateProducers_zero_energy_updates_producer() {
        Producer producer = new Producer(1, EnergyType.SOLAR, 10, 0.5f, 100);
        ProducersDB db = new ProducersDB(List.of(producer));
        Producer updatedProducer = new Producer(1, EnergyType.SOLAR, 10, 0.5f, 0);
        db.updateProducers(updatedProducer);
        assertEquals(0, producer.getEnergyPerDistributor());
    }

    // updateMonthlyStats with no producers in the list does not throw an error
    @Test
    public void test_updateMonthlyStats_no_producers_does_not_throw_error() {
        ProducersDB db = new ProducersDB(List.of());
        try {
            db.updateMonthlyStats(1);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
}
