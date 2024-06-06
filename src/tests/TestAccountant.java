package tests;

import consumer.Consumer;
import databases.Accountant;
import distributors.Distributor;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAccountant {
    // Adding a new contract successfully associates a consumer with a distributor
    @Test
    public void test_add_new_contract_success() {
        Accountant accountant = new Accountant();
        Consumer consumer = new Consumer(1, 1000, 200);
        Distributor distributor = new Distributor(1, 12, 5000, 1000, 100, null);

        accountant.addNewContract(consumer, distributor);

        assertEquals(distributor, accountant.searchDistributor(consumer));
    }

    // Searching for a distributor returns the correct distributor for a given consumer
    @Test
    public void test_search_distributor_success() {
        Accountant accountant = new Accountant();
        Consumer consumer = new Consumer(1, 1000, 200);
        Distributor distributor = new Distributor(1, 12, 5000, 1000, 100, null);

        accountant.addNewContract(consumer, distributor);

        assertEquals(distributor, accountant.searchDistributor(consumer));
    }

    // Adding a bankrupt consumer correctly updates the list of bankrupt consumers
    @Test
    public void test_add_bankrupt_consumer() {
        Accountant accountant = new Accountant();
        Consumer consumer = new Consumer(1, 1000, 200);

        accountant.addBankrupt(consumer);

        assertTrue(accountant.getBankruptConsumers().contains(consumer));
    }

    // Adding a bankrupt distributor correctly updates the list of bankrupt distributors
    @Test
    public void test_add_bankrupt_distributor() {
        Accountant accountant = new Accountant();
        Distributor distributor = new Distributor(1, 12, 5000, 1000, 100, null);

        accountant.addBankrupt(distributor);

        assertTrue(accountant.getBankruptDistributors().contains(distributor));
    }

    // Adding a new contract with a consumer or distributor that is null
    @Test
    public void test_add_new_contract_with_null() {
        Accountant accountant = new Accountant();
        Consumer consumer = new Consumer(1, 1000, 200);

        accountant.addNewContract(consumer, null);

        assertNull(accountant.searchDistributor(consumer));
    }

    // Searching for a distributor with a consumer that has no contract
    @Test
    public void test_search_distributor_no_contract() {
        Accountant accountant = new Accountant();
        Consumer consumer = new Consumer(1, 1000, 200);

        assertNull(accountant.searchDistributor(consumer));
    }

    // Retrieving a consumer for a distributor that has no associated consumer
    @Test
    public void test_get_consumer_no_association() {
        Accountant accountant = new Accountant();
        Distributor distributor = new Distributor(1, 12, 5000, 1000, 100, null);

        assertNull(accountant.getConsumer(distributor));
    }
}
