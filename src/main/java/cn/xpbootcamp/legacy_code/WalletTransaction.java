package cn.xpbootcamp.legacy_code;

import cn.xpbootcamp.legacy_code.enums.STATUS;
import cn.xpbootcamp.legacy_code.service.WalletService;
import cn.xpbootcamp.legacy_code.utils.IdGenerator;
import cn.xpbootcamp.legacy_code.utils.RedisDistributedLock;

import javax.transaction.InvalidTransactionException;

public class WalletTransaction {
    private String id;
    private Long buyerId;
    private Long sellerId;
    private Long createdTimestamp;
    private Double amount;
    private STATUS status;
    private String walletTransactionId;
    private RedisDistributedLock redisDistributedLock;
    private WalletService walletService;

    public void setRedisDistributedLock(RedisDistributedLock redisDistributedLock) {
        this.redisDistributedLock = redisDistributedLock;
    }

    public void setWalletService(WalletService walletService) {
        this.walletService = walletService;
    }

    public WalletTransaction(String preAssignedId, Long buyerId, Long sellerId, Double amount) {
        if (preAssignedId != null && !preAssignedId.isEmpty()) {
            this.id = preAssignedId;
        } else {
            this.id = IdGenerator.generateTransactionId();
        }
        if (!this.id.startsWith("t_")) {
            this.id = "t_" + preAssignedId;
        }
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.amount = amount;
        this.status = STATUS.TO_BE_EXECUTED;
        this.createdTimestamp = System.currentTimeMillis();
    }


    public boolean execute() throws InvalidTransactionException {
        if (buyerId == null || (sellerId == null || amount < 0.0)) {
            throw new InvalidTransactionException("This is an invalid transaction");
        }
        if (status == STATUS.EXECUTED) return true;
        boolean isLocked = false;
        try {
            isLocked = redisDistributedLock.lock(id);

            // 锁定未成功，返回false
            if (!isLocked) {
                return false;
            }
            if (status == STATUS.EXECUTED) return true; // double check
            // 交易超过20天
            if (isExpired()) {
                this.status = STATUS.EXPIRED;
                return false;
            }
            String walletTransactionId = walletService.moveMoney(id, buyerId, sellerId, amount);
            if (walletTransactionId != null) {
                this.walletTransactionId = walletTransactionId;
                this.status = STATUS.EXECUTED;
                return true;
            } else {
                this.status = STATUS.FAILED;
                return false;
            }
        } finally {
            if (isLocked) {
                redisDistributedLock.unlock(id);
            }
        }
    }

    boolean isExpired() {
        long executionInvokedTimestamp = System.currentTimeMillis();
        return executionInvokedTimestamp - createdTimestamp > 1728000000;
    }

    public boolean isStatusSuccessful() {
        return status == STATUS.EXECUTED;
    }

    public boolean isStatusExpired() {
        return status == STATUS.EXPIRED;
    }

    public boolean isStatusFailed() {
        return status == STATUS.FAILED;
    }
}