package tests;

import distributors.Distributor;
import entities.EnergyType;
import org.junit.Test;
import producer.Producer;
import strategies.GreenStrategy;

import static org.junit.Assert.*;

public class TestProducer {

    // Creating a Producer with valid parameters initializes all fields correctly
    @Test
    public void test_create_producer_with_valid_parameters() {
        Producer producer = new Producer(1, EnergyType.SOLAR, 10, 0.5f, 100);
        assertEquals(1, producer.getId());
        assertEquals(EnergyType.SOLAR, producer.getEnergyType());
        assertEquals(10, producer.getMaxDistributors());
        assertEquals(0.5f, producer.getPriceKW(), 0.01);
        assertEquals(100, producer.getEnergyPerDistributor());
        assertTrue(producer.getIsRenewable());
    }

    // getEnergyType returns the correct energy type
    @Test
    public void test_get_energy_type() {
        Producer producer = new Producer(1, EnergyType.WIND, 10, 0.5f, 100);
        assertEquals(EnergyType.WIND, producer.getEnergyType());
    }

    // getMaxDistributors returns the correct maximum number of distributors
    @Test
    public void test_get_max_distributors() {
        Producer producer = new Producer(1, EnergyType.SOLAR, 10, 0.5f, 100);
        assertEquals(10, producer.getMaxDistributors());
    }

    // getPriceKW returns the correct price per kilowatt
    @Test
    public void test_get_price_kw() {
        Producer producer = new Producer(1, EnergyType.SOLAR, 10, 0.5f, 100);
        assertEquals(0.5f, producer.getPriceKW(), 0.01);
    }

    // getEnergyPerDistributor returns the correct energy per distributor
    @Test
    public void test_get_energy_per_distributor() {
        Producer producer = new Producer(1, EnergyType.SOLAR, 10, 0.5f, 100);
        assertEquals(100, producer.getEnergyPerDistributor());
    }

    // getIsRenewable returns the correct renewable status
    @Test
    public void test_get_is_renewable() {
        Producer producer = new Producer(1, EnergyType.SOLAR, 10, 0.5f, 100);
        assertTrue(producer.getIsRenewable());
    }

    // Creating a Producer with zero maxDistributors and checking availability returns false
    @Test
    public void test_zero_max_distributors_check_availability() {
        Producer producer = new Producer(1, EnergyType.SOLAR, 0, 0.5f, 100);
        assertFalse(producer.checkAvailability());
    }

    // Creating a Producer with zero energyPerDistributor and verifying the energy per distributor
    @Test
    public void test_zero_energy_per_distributor_initialization() {
        Producer producer = new Producer(1, EnergyType.SOLAR, 10, 0.5f, 0);
        assertEquals(0, producer.getEnergyPerDistributor());
    }

    // Updating a Producer's energyPerDistributor to zero and verifying the update
    @Test
    public void test_update_energy_per_distributor_to_zero() {
        Producer producer = new Producer(1, EnergyType.SOLAR, 10, 0.5f, 100);
        producer.setEnergyPerDistributor(0);
        assertEquals(0, producer.getEnergyPerDistributor());
    }

    // Subscribing and unsubscribing distributors and checking the subscriber list
    @Test
    public void test_subscribe_and_unsubscribe_distributors() {
        Producer producer = new Producer(1, EnergyType.SOLAR, 10, 0.5f, 100);
        Distributor distributor = new Distributor(1, 1000, new GreenStrategy());
        producer.subscribe(distributor);
        assertEquals(1, producer.getSubscribers().size());
        producer.unsubscribe(distributor);
        assertEquals(0, producer.getSubscribers().size());
    }

    // Updating monthly stats when there are no subscribers
    @Test
    public void test_update_monthly_stats_no_subscribers() {
        Producer producer = new Producer(1, EnergyType.SOLAR, 10, 0.5f, 100);
        producer.updateMonthlyStats(1);
        assertEquals(1, producer.getMonthlyStats().size());
        assertTrue(producer.getMonthlyStats().getFirst().getDistributorsIds().isEmpty());
    }

    // checkAvailability returns true when the number of subscribers is less than maxDistributors
    @Test
    public void test_check_availability_with_less_subscribers_than_max_distributors() {
        Producer producer = new Producer(1, EnergyType.SOLAR, 10, 0.5f, 100);
        Distributor distributor = new Distributor(1, 1000, new GreenStrategy());
        producer.subscribe(distributor);
        assertTrue(producer.checkAvailability());
    }
}
