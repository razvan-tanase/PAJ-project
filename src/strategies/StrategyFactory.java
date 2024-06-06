package strategies;

/**
 * This interface is used to implement a factory and singleton design pattern
 */
public final class StrategyFactory {
    private static StrategyFactory instance;

    private StrategyFactory() {
        // private constructor
    }

    /**
     * Method to return instance of class
     *
     * @return The instance of the factory
     */
    public static StrategyFactory getInstance() {
        if (instance == null) {
            // if instance is null, initialize
            instance = new StrategyFactory();
        }
        return instance;
    }

    /**
     * @param strategy The strategy in the input files
     * @return A distributor's strategy
     */
    public MarketStrategy selectStrategy(final String strategy) {
        return switch (strategy) {
            case "GREEN" -> new GreenStrategy();
            case "PRICE" -> new PriceStrategy();
            case "QUANTITY" -> new QuantityStrategy();
            default -> throw new IllegalStateException("Unexpected value: " + strategy);
        };
    }
}
