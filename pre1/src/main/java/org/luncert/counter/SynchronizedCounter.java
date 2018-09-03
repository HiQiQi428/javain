package org.luncert.counter;

public class SynchronizedCounter extends ThreadSafeCounter {

    private int i = 0;

    @Override
    void output() {
        System.out.println(i);
    }

    @Override
    synchronized void increase() {
        i++;
    }

}