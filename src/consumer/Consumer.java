package consumer;

import databases.Accountant;
import distributors.Contracts;
import distributors.Distributor;

/**
 * The entity which pays for the consumed energy from one distributor
 */
public final class Consumer {
    /**
     * Consumer id
     */
    private final int consumerID;
    /**
     * Consumer budget
     */
    private long budget;
    /**
     * Consumer salary
     */
    private final long monthlyIncome;
    /**
     * The value of the current contract he pays
     */
    private long monthlyExpenses;
    /**
     * Information about the financial situation of a consumer
     */
    private final BlackList blackList;

    public Consumer(final int consumerID, final long budget, final long monthlyIncome) {
        this.consumerID = consumerID;
        this.budget = budget;
        this.monthlyIncome = monthlyIncome;
        this.monthlyExpenses = 0;
        this.blackList = new BlackList(false, 0, false);

    }

    public int getConsumerID() {
        return consumerID;
    }

    public long getBudget() {
        return budget;
    }

    public long getMonthlyIncome() {
        return monthlyIncome;
    }

    public BlackList getBlackList() {
        return blackList;
    }

    public void setMonthlyExpenses(final int monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }

    /**
     * The salary he receives in each round and which will be added to the consumer's
     * budget before making any payment
     */
    public void getSalary() {
        budget += monthlyIncome;
    }

    /**
     * If the consumer has no undergoing contract, he will sign one with the cheapest distributor
     *
     * @param distributor The distributor for whom the consumer can signs a new contract
     * @return True if the consumer signs a new contract or false if it already has a contract
     * undergoing
     */
    public boolean visit(final Distributor distributor) {
        if (monthlyExpenses == 0) {
            monthlyExpenses = distributor.getContractPrice();
            Contracts contract = new Contracts(consumerID, monthlyExpenses,
                    distributor.getContractLength());
            distributor.addContract(contract);
            return true;
        }
        return false;

    }

    /**
     * The consumer gets his salary before making any payment and then will be verified if
     * he has an unpaid one;
     * In all cases the consumer will be deducted one month from the contract
     *
     * @param distributor The distributor to whom the consumer must pay the monthly installment
     * @param accountant  Specifies to the consumer to whom to pay the monthly installment
     */
    public void payRate(final Distributor distributor, final Accountant accountant) {
        getSalary();

        if (blackList.isPotentialBankrupt()) {
            handlePotentialBankruptcy(distributor, accountant);
        } else {
            handleRegularPayment(distributor, accountant);
        }

        checkEndOfContract(distributor, blackList.isPotentialBankrupt());
    }

    /**
     * Handles the payment process when the consumer is potentially bankrupt.
     * Checks if the consumer can pay the total bill (penalty + monthly expenses),
     * and updates the distributor or accountant accordingly.
     *
     * @param distributor The distributor to whom the consumer must pay the monthly installment
     * @param accountant  Specifies to the consumer to whom to pay the monthly installment
     */
    private void handlePotentialBankruptcy(final Distributor distributor, final Accountant accountant) {
        long penaltyBill = calculatePenalty();
        long totalBill = penaltyBill + monthlyExpenses;

        if (budget >= totalBill) {
            hasMoney(distributor, true);
        } else if (distributor.getId() == blackList.getOldDistributor().getId()) {
            noMoney(accountant, true);
        } else if (budget >= penaltyBill) {
            budget -= penaltyBill;
            blackList.getOldDistributor().getRate(penaltyBill);
            blackList.setOverduePayment(monthlyExpenses);
        } else {
            noMoney(accountant, true);
        }
    }

    /**
     * Handles the regular payment process when the consumer is not potentially bankrupt.
     * Checks if the consumer can pay the monthly expenses, and updates the distributor or accountant accordingly.
     *
     * @param distributor The distributor to whom the consumer must pay the monthly installment
     * @param accountant  Specifies to the consumer to whom to pay the monthly installment
     */
    private void handleRegularPayment(final Distributor distributor, final Accountant accountant) {
        if (budget >= monthlyExpenses) {
            hasMoney(distributor, false);
        } else {
            noMoney(accountant, false);
        }
    }

    /**
     * @return The old payment plus interest
     */
    public long calculatePenalty() {
        return Math.round(Math.floor(1.2 * blackList.getOverduePayment()));
    }

    /**
     * If he is up to date and has money to pay the bills then he pays his rate normally
     * to the distributor;
     * If he is a potential bankrupt but has money to pay all the bills, then he will not
     * be suspicious for bankrupting the next round and he is charged and the money go to
     * the old distributor
     *
     * @param distributor       The distributor to whom the consumer must pay the monthly bill
     * @param potentialBankrupt The current financial status of a consumer
     */
    public void hasMoney(final Distributor distributor, final boolean potentialBankrupt) {
        budget -= monthlyExpenses;
        distributor.getRate(monthlyExpenses);
        if (potentialBankrupt) {
            long penaltyBill = calculatePenalty();
            budget -= penaltyBill;
            blackList.getOldDistributor().getRate(penaltyBill);
            blackList.setPotentialBankrupt(false);
        }
    }

    /**
     * If he has no money to pay this month, he is set as a potential bankrupt;
     * If he is already a potential bankrupt and has no money to pay the current and the old
     * installment, then he is declared bankrupt -> he is added in a list with all bankrupts
     *
     * @param accountant        Add the consumer on the bankrupt list if he can not pay the bills
     * @param potentialBankrupt The current financial status of a consumer
     */
    public void noMoney(final Accountant accountant, final boolean potentialBankrupt) {
        if (potentialBankrupt) {
            blackList.setBankrupt(true);
            accountant.addBankrupt(this);
        } else {
            blackList.setPotentialBankrupt(true);
            blackList.setOverduePayment(monthlyExpenses);
        }
    }

    /**
     * At the end of each month it is checked if the consumer's contract has reached the end in
     * order to sign a new contract next month.
     *
     * @param distributor       The distributor to whom the consumer must pay the monthly bill
     * @param potentialBankrupt The current financial status of a consumer
     */
    public void checkEndOfContract(final Distributor distributor,
                                   final boolean potentialBankrupt) {
        if (distributor.searchContract(consumerID)) {
            monthlyExpenses = 0;
        }
        /* If he has reached the end of the contract but cannot pay the last
          installment, saves the address of the distributor
        */
        if (potentialBankrupt) {
            blackList.setOldDistributor(distributor);
        }
    }
}
