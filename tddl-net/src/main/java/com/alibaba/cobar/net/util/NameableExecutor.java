package com.alibaba.cobar.net.util;

import java.util.concurrent.*;

/**
 * @author xianmao.hexm
 */
public class NameableExecutor extends ThreadPoolExecutor {

    protected String name;

    public NameableExecutor(String name, int size, BlockingQueue<Runnable> queue, ThreadFactory factory) {
        super(size, size, Long.MAX_VALUE, TimeUnit.NANOSECONDS, queue, factory);
        this.name = name;
    }

    public NameableExecutor(String name, int size, BlockingQueue<Runnable> queue, ThreadFactory factory,
                            RejectedExecutionHandler handler) {
        super(size, size, Long.MAX_VALUE, TimeUnit.NANOSECONDS, queue, factory, handler);
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
