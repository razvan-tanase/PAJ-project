package tests;

import consumer.Consumer;
import databases.Accountant;
import distributors.Contracts;
import distributors.Distributor;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestDistributor {

    // Distributor updates contract price based on current infrastructure and production costs
    @Test
    public void test_update_contract_price() {
        Distributor distributor = new Distributor(1, 12, 1000, 500, 300, null);
        distributor.updateContract();
        assertEquals(500, distributor.getContractPrice());
    }

    // Distributor correctly adds new contracts to the contract list
    @Test
    public void test_add_new_contract() {
        Distributor distributor = new Distributor(1, 12, 1000, 500, 300, null);
        Contracts contract = new Contracts(1, 500, 12);
        distributor.addContract(contract);
        assertTrue(distributor.getContracts().contains(contract));
    }

    // Distributor accurately calculates monthly expenses including infrastructure and production costs
    @Test
    public void test_calculate_monthly_expenses() {
        Distributor distributor = new Distributor(1, 12, 1000, 500, 300, null);
        distributor.addContract(new Contracts(1, 500, 12));
        long monthlyExpenses = distributor.getMonthlyExpenses();
        assertEquals(500, monthlyExpenses);
    }

    // Distributor correctly updates budget after receiving payments from consumers
    @Test
    public void test_update_budget_after_payment() {
        Distributor distributor = new Distributor(1, 12, 1000, 500, 300, null);
        distributor.getRate(500);
        assertEquals(1500, distributor.getBudget());
    }

    // Distributor handles a scenario where the budget becomes negative and declares bankruptcy
    @Test
    public void test_handle_negative_budget_and_bankruptcy() {
        Distributor distributor = new Distributor(1, 12, -1000, 500, 300, null);
        distributor.calculateNewBudget();
        assertTrue(distributor.isBankrupt());
    }

    // Distributor updates contract price when there are no current contracts
    @Test
    public void test_update_contract_price_no_contracts() {
        Distributor distributor = new Distributor(1, 12, 1000, 500, 300, null);
        distributor.updateContract();
        assertEquals(500 + Math.round(Math.floor(0.2 * distributor.getProductionCost())), distributor.getContractPrice());
    }

    // Distributor calculates monthly expenses when there are no contracts
    @Test
    public void test_calculate_monthly_expenses_no_contracts() {
        Distributor distributor = new Distributor(1, 12, 1000, 500, 300, null);
        long monthlyExpenses = distributor.getMonthlyExpenses();
        assertEquals(500 + Math.round(Math.floor(0.2 * distributor.getProductionCost())), monthlyExpenses);
    }

    // Distributor processes consumer visits when consumer budget is not enough
    @Test
    public void test_consumer_visit_insufficient_budget() {
        Consumer consumer = new Consumer(1, 1000, 200);
        Accountant accountant = new Accountant();
        Distributor distributor = new Distributor(1, 12, 1000, 500, 300, null);
        consumer.setMonthlyExpenses(1500); // Set higher than budget
        consumer.payRate(distributor, accountant);
        assertTrue(consumer.getBlackList().isPotentialBankrupt());
    }

    // Distributor updates budget after receiving contract payments
    @Test
    public void test_update_budget_after_payments() {
        Distributor distributor = new Distributor(1, 12, 1000, 500, 300, null);
        distributor.getRate(500);
        assertEquals(1500, distributor.getBudget());
    }

    // Distributor correctly processes when budget becomes negative and declares bankruptcy
    @Test
    public void test_declare_bankruptcy_when_budget_negative() {
        Distributor distributor = new Distributor(1, 12, -1000, 500, 300, null);
        distributor.calculateNewBudget();
        assertTrue(distributor.isBankrupt());
    }

    // Distributor handles a scenario where no consumers have contracts
    @Test
    public void test_no_consumers_have_contracts() {
        Distributor distributor = new Distributor(1, 12, 1000, 500, 300, null);
        assertTrue(distributor.getContracts().isEmpty());
    }

    // Distributor handles scenario where infrastructure cost changes drastically
    @Test
    public void test_infrastructure_cost_changes_drastically() {
        Distributor distributor = new Distributor(1, 12, 1000, 5000, 300, null);
        distributor.setInfrastructureCost(10000);
        assertEquals(10000, distributor.getInfrastructureCost());
    }

    // Distributor correctly identifies and removes finished contracts
    @Test
    public void test_remove_finished_contracts() {
        Distributor distributor = new Distributor(1, 12, 1000, 500, 300, null);
        Contracts contract = new Contracts(1, 500, 0);
        distributor.addContract(contract);
        distributor.removeFinishedContracts();
        assertFalse(distributor.getContracts().contains(contract));
    }
}