package com.aiqin.mgs.order.api.component;

/**
 * @author sunx
 * @create 2018-10-8
 */
public class SyncUtil {
    private final int lockStoreSize;
    private final Object[] lockStore;

    public SyncUtil(int lockStoreSize) {
        this.lockStoreSize = lockStoreSize;
        this.lockStore = new Object[lockStoreSize];

        for(int i = 0; i < this.lockStore.length; ++i) {
            this.lockStore[i] = new Object();
        }

    }

    public SyncUtil() {
        this(64);
    }

    private Object getSyncLock(Object source) {
        if (source == null) {
            throw new RuntimeException("source 不能为null");
        } else {
            return this.lockStore[Math.abs(source.hashCode() % this.lockStoreSize)];
        }
    }

    public <T> T syncExecute(SyncExecutionUnit unit) {
        Object lock = this.getSyncLock(unit.getSyncLockSource());
        if (unit.isExecutable()) {
            synchronized(lock) {
                if (unit.isExecutable()) {
                    return unit.execute();
                }
            }
        }

        return null;
    }
}

