package distributors;

/**
 * The information that a distributor keeps in connection with the subscribed consumers
 */
public final class Contracts {
    /**
     * Id of the consumer
     */
    private final int consumerId;
    /**
     * Price of the contract he signed
     */
    private final long price;
    /**
     * The period of time for which the contract is still valid
     */
    private int remainedContractMonths;

    public Contracts(final int consumerId, final long price, final int remainedContractMonths) {
        this.consumerId = consumerId;
        this.price = price;
        this.remainedContractMonths = remainedContractMonths;
    }

    public int getConsumerId() {
        return consumerId;
    }

    public long getPrice() {
        return price;
    }

    public int getRemainedContractMonths() {
        return remainedContractMonths;
    }

    /**
     * @return True if the consumer no longer has to pay this contract
     */
    public boolean endOfContract() {
        return --remainedContractMonths == 0;
    }
}
