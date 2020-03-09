package cn.xpbootcamp.legacy_code.utils;

import java.util.UUID;

public class IdGenerator {
    public static String generateTransactionId() {
        return UUID.randomUUID().toString();
    }
}
