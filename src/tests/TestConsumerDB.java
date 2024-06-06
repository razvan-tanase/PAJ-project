package tests;

import consumer.Consumer;
import databases.Accountant;
import databases.ConsumerDB;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestConsumerDB {
    // Adding a consumer to the database successfully
    @Test
    public void test_add_consumer_successfully() {
        List<Consumer> consumers = new ArrayList<>();
        ConsumerDB consumerDB = new ConsumerDB(consumers);
        Consumer consumer = new Consumer(1, 1000, 100);
        consumerDB.addConsumer(consumer);
        assertEquals(1, consumerDB.getConsumers().size());
        assertEquals(consumer, consumerDB.getConsumers().getFirst());
    }

    // Adding a consumer when the list is empty
    @Test
    public void test_add_consumer_to_empty_list() {
        List<Consumer> consumers = new ArrayList<>();
        ConsumerDB consumerDB = new ConsumerDB(consumers);
        assertTrue(consumerDB.getConsumers().isEmpty());
        Consumer consumer = new Consumer(1, 1000, 100);
        consumerDB.addConsumer(consumer);
        assertEquals(1, consumerDB.getConsumers().size());
    }

    // Removing bankrupt consumers when none are bankrupt
    @Test
    public void test_remove_bankrupts_when_none_are_bankrupt() {
        List<Consumer> consumers = new ArrayList<>();
        Consumer consumer1 = new Consumer(1, 1000, 100);
        Consumer consumer2 = new Consumer(2, 2000, 200);
        consumers.add(consumer1);
        consumers.add(consumer2);
        ConsumerDB consumerDB = new ConsumerDB(consumers);
        Accountant accountant = new Accountant();
        consumerDB.removeBankrupts(accountant);
        assertEquals(2, consumerDB.getConsumers().size());
    }
}
