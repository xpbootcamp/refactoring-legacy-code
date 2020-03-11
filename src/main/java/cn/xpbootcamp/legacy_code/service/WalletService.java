package cn.xpbootcamp.legacy_code.service;

import cn.xpbootcamp.legacy_code.entity.User;
import cn.xpbootcamp.legacy_code.repository.UserRepository;

import java.util.UUID;

public class WalletService {
    private UserRepository userRepository;

    public WalletService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String moveMoney(String id, long buyerId, long sellerId, double amount) {
        User buyer = userRepository.find(buyerId);
        User seller = userRepository.find(sellerId);
        if (buyer.transform(seller, amount)) {
            return UUID.randomUUID().toString() + id;
        }
        return null;
    }

}
