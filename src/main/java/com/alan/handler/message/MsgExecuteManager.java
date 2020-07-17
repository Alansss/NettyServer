package com.alan.handler.message;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: Wang Lijie
 * @Date: 2019/4/23 16:50
 * @Description: TODO
 */
public class MsgExecuteManager {

    private static MsgExecuteManager instance = null;

    public static MsgExecuteManager getInstance() {
        if (null == instance) {
            instance = new MsgExecuteManager();
        }
        return instance;
    }

    public void logic() {

        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("logicThread-%d").build();
        ThreadPoolExecutor pool = new ThreadPoolExecutor(30, 30, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), threadFactory);
        while (true) {
            if (pool.getActiveCount() < pool.getMaximumPoolSize()) {
                pool.execute(() -> NetMessageProcesser.getInstance().dispatchNetMessage());
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
