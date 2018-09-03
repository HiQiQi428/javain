package org.luncert.counter;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerCounter extends ThreadSafeCounter {

    private AtomicInteger i = new AtomicInteger(0);

    @Override
    void output() {
        System.out.println(i);
    }

    @Override
    void increase() {
        i.incrementAndGet();
    }

}