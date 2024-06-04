package consumer;

import distributors.Distributor;

/**
 * Information about the financial situation of a consumer which is not up to date with payments
 */
public final class BlackList {
    /**
     * indicates whether the consumer has an overdue payment
     */
    private boolean potentialBankrupt;
    /**
     * the payment he did not pay and to which a penalty will be added
     */
    private long overduePayment;
    /**
     * the financial situation of a consumer
     */
    private boolean bankrupt;
    /**
     * the distributor to whom he has an overdue payment
     */
    private Distributor oldDistributor;

    public BlackList(final boolean potentialBankrupt, final long overduePayment,
                     final boolean bankrupt) {
        this.potentialBankrupt = potentialBankrupt;
        this.overduePayment = overduePayment;
        this.bankrupt = bankrupt;
        this.oldDistributor = null;
    }

    public boolean isPotentialBankrupt() {
        return potentialBankrupt;
    }

    public long getOverduePayment() {
        return overduePayment;
    }

    public boolean isBankrupt() {
        return bankrupt;
    }

    public Distributor getOldDistributor() {
        return oldDistributor;
    }

    public void setPotentialBankrupt(final boolean potentialBankrupt) {
        this.potentialBankrupt = potentialBankrupt;
    }

    public void setOverduePayment(final long overduePayment) {
        this.overduePayment = overduePayment;
    }

    public void setBankrupt(final boolean bankrupt) {
        this.bankrupt = bankrupt;
    }

    public void setOldDistributor(final Distributor oldDistributor) {
        this.oldDistributor = oldDistributor;
    }
}
