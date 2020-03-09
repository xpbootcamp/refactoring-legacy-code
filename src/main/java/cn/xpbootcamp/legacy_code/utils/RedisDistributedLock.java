package cn.xpbootcamp.legacy_code.utils;

public class RedisDistributedLock {
    private static final RedisDistributedLock INSTANCE = new RedisDistributedLock();

    public static RedisDistributedLock getSingletonInstance() {
        return INSTANCE;
    }

    public boolean lock(String transactionId) {
        // Here is connecting to redis server, please do not invoke directly
        throw new RuntimeException("Redis server is connecting......");
    }

    public void unlock(String transactionId) {
        // Here is connecting to redis server, please do not invoke directly
        throw new RuntimeException("Redis server is connecting......");
    }
}
