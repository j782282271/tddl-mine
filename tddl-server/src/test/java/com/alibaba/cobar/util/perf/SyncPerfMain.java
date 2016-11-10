package com.alibaba.cobar.util.perf;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xianmao.hexm
 */
public class SyncPerfMain {

    private final Object lockA = new Object();
    private final ReentrantLock lockB = new ReentrantLock();
    long i = 0L;

    public static void main(String[] args) {
        int count = 10000000;
        SyncPerfMain test = new SyncPerfMain();

        System.currentTimeMillis();
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            test.tLockA();
            //test.testLockB();
        }
        long t2 = System.currentTimeMillis();
        System.out.println("take:" + (t2 - t1) + " ms.");
    }

    final void tLockA() {
        final Object lock = this.lockA;
        synchronized (lock) {
            i++;
        }
    }

    final void tLockB() {
        final ReentrantLock lock = this.lockB;
        lock.lock();
        try {
            i++;
        } finally {
            lock.unlock();
        }
    }

}
