package distributors;

/**
 * Class that implements the functionalities of Observer
 */
public interface Subscriber {
    /**
     * This method is called whenever the observed object is changed
     */
    void update();

    /**
     * @return The ID of the subscribed distributor
     */
    int getId();
}
