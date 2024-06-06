package tests;

import strategies.StrategyFactory;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestStrategyFactory {

    // getInstance returns the same instance on multiple calls
    @Test
    public void test_get_instance_returns_same_instance() {
        StrategyFactory instance1 = StrategyFactory.getInstance();
        StrategyFactory instance2 = StrategyFactory.getInstance();
        assertSame(instance1, instance2);
    }

    // getInstance handles concurrent calls correctly
    @Test
    public void test_get_instance_handles_concurrent_calls() throws InterruptedException {
        final StrategyFactory[] instances = new StrategyFactory[2];
        Thread thread1 = new Thread(() -> instances[0] = StrategyFactory.getInstance());
        Thread thread2 = new Thread(() -> instances[1] = StrategyFactory.getInstance());
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        assertSame(instances[0], instances[1]);
    }
}