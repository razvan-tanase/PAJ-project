package producer;

import distributors.Distributor;
import distributors.Subscriber;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements the functionalities of Observable
 */
public class Provider {
    /**
     * List of distributors subscribed in the current month
     */
    private final List<Subscriber> subscribers;

    protected Provider() {
        this.subscribers = new ArrayList<>();
    }

    final List<Subscriber> getSubscribers() {
        return subscribers;
    }

    /**
     * @param distributor The distributor who wants to subscribe
     */
    public void subscribe(final Distributor distributor) {
        subscribers.add(distributor);
    }

    /**
     * @param distributor The distributor who wants to unsubscribe
     */
    public void unsubscribe(final Distributor distributor) {
        subscribers.remove(distributor);
    }

    /**
     * Notify each subscriber distributor that changes have occurred
     */
    public void notifySubscribers() {
        for (Subscriber subscriber : subscribers) {
            subscriber.update();
        }
    }
}
