package org.luncert;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestReentrantLock {

    Lock lock = new ReentrantLock();
    int a = 0;

    @Test
    public void test()  {
        Thread[] threads = new Thread[2];
        threads[0] = new Thread(){
            public void run() {
                inc();
            };
        };
        threads[0].start();
        threads[1] = new Thread(){
            public void run() {
                inc();
            };
        };
        threads[1].start();
        while (Thread.activeCount() > 3)
            Thread.yield();
        System.out.println(a);
    }  
     
    public void inc() {
        Thread thread  = Thread.currentThread();
        lock.lock();
        try {
            System.out.println(thread.getName()+"得到了锁");
            for(int i = 0; i < 100000; i++) {
                a++;
            }
        } catch (Exception e) {
        } finally {
            System.out.println(thread.getName()+"释放了锁");
            lock.unlock();
        }
    }

}