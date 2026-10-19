package com.anson.localservices.utils;

public interface ILock {

    boolean tryLock(long timeoutSec);

    void unlock();
}
