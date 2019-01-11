package com.aiqin.mgs.order.api.component;

public interface SyncExecutionUnit {
    boolean isExecutable();
    Object getSyncLockSource();
    <R> R execute();
}
