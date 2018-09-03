package org.luncert.counter;

public abstract class ThreadSafeCounter {

    abstract void output();

    abstract void increase();

    public void run(int threadsCount, int incTime) throws InterruptedException {
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                for (int i = 0; i < incTime; i++)
                    increase();
            }
        };
        Thread[] threads = new Thread[threadsCount];
        for (int i = 0; i < threadsCount; i++) {
            threads[i] = new Thread(runnable);
            threads[i].start();
            // threads[i].join();
        }
        while (Thread.activeCount() > 3)
            Thread.yield();
        output();
    }
    
}