package com.ecommerce.common.distributedlock;

import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SimpleLock;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

// Used directly by client code to run with distributed lock
public class DistributedLockExecutor {
    private static final Duration MAX_RUN_TIME = Duration.ofMinutes(5);

    private final LockProvider lockProvider;

    public DistributedLockExecutor(LockProvider lockProvider) {
        this.lockProvider = lockProvider;
    }

    public <T> T tryExecute(Supplier<T> supplier, String lockKey) {
        LockConfiguration lockConfiguration = new LockConfiguration(lockKey, Instant.now().plus(MAX_RUN_TIME));
        return this.tryExecute(supplier, lockConfiguration);

    }

    public <T> T tryExecute(Supplier<T> supplier, LockConfiguration configuration) {
        Optional<SimpleLock> lock = lockProvider.lock(configuration);
        if (!lock.isPresent()) {
            throw new LockAlreadyOccupiedException(configuration.getName());
        }

        try {
            return supplier.get();
        } finally {
            lock.get().unlock();
        }
    }

    public <T> T execute(Supplier<T> supplier, LockConfiguration configuration) {
        Optional<SimpleLock> lock;
        while (true) {
            lock = lockProvider.lock(configuration);
            if (lock.isPresent()) {
                break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            return supplier.get();
        } finally {
            lock.get().unlock();
        }
    }

    public <T> T execute(Supplier<T> supplier, String lockKey) {
        LockConfiguration lockConfiguration = new LockConfiguration(lockKey, Instant.now().plus(MAX_RUN_TIME));
        return this.execute(supplier, lockConfiguration);
    }


}
