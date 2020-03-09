package cn.xpbootcamp.legacy_code.service;

public interface WalletService {
    String moveMoney(String id, long buyerId, long sellerId, double amount);
}
