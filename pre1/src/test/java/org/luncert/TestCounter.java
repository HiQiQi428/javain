package org.luncert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.luncert.counter.AtomicIntegerCounter;
import org.luncert.counter.SynchronizedCounter;
import org.luncert.counter.ThreadSafeCounter;

@RunWith(JUnit4.class)
public class TestCounter {
    
    @Test
    public void test() throws InterruptedException {
        long start = System.currentTimeMillis();
        ThreadSafeCounter counter = new AtomicIntegerCounter();
        counter.run(20, 10000);
        System.out.println(System.currentTimeMillis() - start + " ms");
        
        counter = new SynchronizedCounter();
        counter.run(20, 10000);
        System.out.println(System.currentTimeMillis() - start + " ms");
    }

}