package tests;

import consumer.Consumer;
import databases.Accountant;
import databases.DistributorsDB;
import distributors.Contracts;
import distributors.Distributor;
import entities.EnergyType;
import org.junit.Test;
import producer.Producer;
import strategies.GreenStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class TestDistributorsDB {

    // updateDistributor correctly updates the infrastructure cost of the specified distributor
    @Test
    public void test_update_distributor_infrastructure_cost() {
        Distributor distributor1 = new Distributor(1, 1000);
        Distributor distributor2 = new Distributor(2, 2000);
        List<Distributor> distributorList = Arrays.asList(distributor1, distributor2);
        DistributorsDB db = new DistributorsDB(distributorList);

        Distributor updatedDistributor = new Distributor(1, 1500);
        db.updateDistributor(updatedDistributor);

        assertEquals(1500, distributor1.getInfrastructureCost());
    }

    // getCheapestContract returns the distributor with the lowest contract price
    @Test
    public void test_get_cheapest_contract() {
        Distributor distributor1 = new Distributor(1, 1000);
        distributor1.updateContract();
        Distributor distributor2 = new Distributor(2, 2000);
        distributor2.updateContract();
        List<Distributor> distributorList = Arrays.asList(distributor1, distributor2);
        DistributorsDB db = new DistributorsDB(distributorList);

        Distributor cheapestDistributor = db.getCheapestContract();

        assertEquals(distributor1.getId(), cheapestDistributor.getId());
    }

    // payAllRates correctly calculates the new budget for all distributors
    @Test
    public void test_pay_all_rates_calculates_new_budget() {
        Distributor distributor1 = new Distributor(1, 1000);
        distributor1.addContract(new Contracts(1, 500, 12));
        Distributor distributor2 = new Distributor(2, 2000);
        distributor2.addContract(new Contracts(2, 700, 12));
        List<Distributor> distributorList = Arrays.asList(distributor1, distributor2);
        DistributorsDB db = new DistributorsDB(distributorList);

        db.payAllRates();

        assertTrue(distributor1.getBudget() < 0);
        assertTrue(distributor2.getBudget() < 0);
    }

    // updateDistributor with a non-existent distributor ID does not alter any distributor
    @Test
    public void test_update_non_existent_distributor() {
        Distributor distributor1 = new Distributor(1, 1000);
        List<Distributor> distributorList = List.of(distributor1);
        DistributorsDB db = new DistributorsDB(distributorList);

        Distributor nonExistentDistributor = new Distributor(99, 1500);
        db.updateDistributor(nonExistentDistributor);

        assertEquals(1000, distributor1.getInfrastructureCost());
    }

    // searchProducers when all distributors already have a production cost set does not alter any distributor
    @Test
    public void test_search_producers_no_alteration_when_production_cost_set() {
        Distributor distributor1 = new Distributor(1, 1000, new GreenStrategy());
        Producer producer = new Producer(1, EnergyType.SOLAR, 10, 5.0f, 100);
        List<Producer> producerList = List.of(producer);

        distributor1.executeStrategy(producerList);
        List<Distributor> distributorList = List.of(distributor1);
        DistributorsDB db = new DistributorsDB(distributorList);

        db.searchProducers(producerList);

        assertEquals(1000, distributor1.getInfrastructureCost());
    }

    // getCheapestContract when there are no distributor returns null
    @Test
    public void test_get_cheapest_contract_no_distributors() {
        List<Distributor> distributorList = new ArrayList<>();
        DistributorsDB db = new DistributorsDB(distributorList);

        Distributor cheapestDistributor = db.getCheapestContract();

        assertNull(cheapestDistributor);
    }

    // payAllRates when all distributors are bankrupt does not alter any distributor's budget
    @Test
    public void test_pay_all_rates_all_bankrupt() {
        Distributor distributor1 = new Distributor(1, 1000);
        distributor1.calculateNewBudget();
        Distributor distributor2 = new Distributor(2, 2000);
        distributor2.calculateNewBudget();
        List<Distributor> distributorList = Arrays.asList(distributor1, distributor2);
        DistributorsDB db = new DistributorsDB(distributorList);

        db.payAllRates();

        assertTrue(distributor1.isBankrupt());
        assertTrue(distributor2.isBankrupt());
    }

    // payAllRates correctly calculates and updates the budget for all distributors
    @Test
    public void test_pay_all_rates_updates_budget() {
        Distributor distributor = new Distributor(1, 1000);
        distributor.setBudget(5000);
        DistributorsDB db = new DistributorsDB(Collections.singletonList(distributor));

        db.payAllRates();

        assertTrue(distributor.getBudget() < 5000);
    }

    // updateContractsPrice correctly updates the contract prices based on the number of consumers
    @Test
    public void test_update_contracts_price() {
        Distributor distributor = new Distributor(1, 1000);
        Consumer consumer = new Consumer(1, 1000, 100);
        distributor.addContract(new Contracts(consumer.getConsumerID(), 100, 12));
        DistributorsDB db = new DistributorsDB(Collections.singletonList(distributor));

        db.updateContractsPrice();

        assertTrue(distributor.getContractPrice() > 0);
    }

    // removeContracts successfully removes finished contracts from all distributors
    @Test
    public void test_remove_contracts() {
        Distributor distributor = new Distributor(1, 1000);
        Consumer consumer = new Consumer(1, 1000, 100);
        Contracts contract = new Contracts(consumer.getConsumerID(), 100, 0);
        distributor.addContract(contract);
        DistributorsDB db = new DistributorsDB(Collections.singletonList(distributor));

        db.removeContracts();

        assertTrue(distributor.getContracts().isEmpty());
    }

    // updateDistributor handles the case where the distributor is not found in the list
    @Test
    public void test_update_distributor_not_found() {
        Distributor distributor1 = new Distributor(1, 1000);
        Distributor distributor2 = new Distributor(2, 2000);
        DistributorsDB db = new DistributorsDB(Collections.singletonList(distributor1));

        db.updateDistributor(distributor2);

        assertEquals(1000, db.getDistributors().get(0).getInfrastructureCost());
    }

    // getCheapestContract handles the case where all distributors have the same contract price
    @Test
    public void test_get_cheapest_contract_same_price() {
        Distributor distributor1 = new Distributor(1, 1000);
        distributor1.setContractPrice(100);
        Distributor distributor2 = new Distributor(2, 2000);
        distributor2.setContractPrice(100);
        DistributorsDB db = new DistributorsDB(Arrays.asList(distributor1, distributor2));

        Distributor cheapest = db.getCheapestContract();

        assertNotNull(cheapest);
    }

    // payAllRates handles the case where a distributor has no contracts
    @Test
    public void test_pay_all_rates_no_contracts() {
        Distributor distributor = new Distributor(1, 1000);
        distributor.setBudget(5000);
        DistributorsDB db = new DistributorsDB(Collections.singletonList(distributor));

        db.payAllRates();

        assertEquals(5000 - distributor.getInfrastructureCost(), distributor.getBudget());
    }

    // removeBankrupts handles the case where no distributors are bankrupt
    @Test
    public void test_remove_bankrupts_no_bankrupts() {
        Distributor distributor = new Distributor(1, 1000);
        Accountant accountant = new Accountant();
        DistributorsDB db = new DistributorsDB(Collections.singletonList(distributor));

        db.removeBankrupts(accountant);

        assertEquals(1, db.getDistributors().size());
    }
}
