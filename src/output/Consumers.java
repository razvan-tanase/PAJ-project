package output;

/**
 * Class used exclusively to retain the details of a consumer required to be displayed at output
 */
public final class Consumers {

    private final int id;

    private final boolean isBankrupt;

    private final long budget;

    public Consumers(final int id, final boolean isBankrupt, final long budget) {
        this.id = id;
        this.isBankrupt = isBankrupt;
        this.budget = budget;
    }

    public int getId() {
        return id;
    }

    public boolean getIsBankrupt() {
        return isBankrupt;
    }

    public long getBudget() {
        return budget;
    }
}
