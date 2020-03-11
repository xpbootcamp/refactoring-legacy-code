package cn.xpbootcamp.legacy_code;

import cn.xpbootcamp.legacy_code.service.WalletService;
import cn.xpbootcamp.legacy_code.service.WalletServiceImpl;
import cn.xpbootcamp.legacy_code.utils.RedisDistributedLock;
import org.junit.jupiter.api.Test;

import javax.transaction.InvalidTransactionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class WalletTransactionTest {

    @Test
    void should_execute_successfully() throws InvalidTransactionException {
        long buyerId = 1L;
        long sellerId = 2L;
        double amount = 500.0;

        TestFixture testFixture = new TestFixture(buyerId, sellerId, amount, true).invoke();

        WalletTransaction walletTransaction = testFixture.getWalletTransaction();
        WalletService walletService = testFixture.getWalletService();
        String updatedPreAssignedId = testFixture.getUpdatedPreAssignedId();
        RedisDistributedLock redisDistributedLock = testFixture.getRedisDistributedLock();

        walletTransaction.execute();

        assertTrue(walletTransaction.isStatusSuccessful());
        verify(walletService, times(1)).moveMoney(updatedPreAssignedId, buyerId, sellerId, amount);
        verify(redisDistributedLock, times(1)).lock(updatedPreAssignedId);
    }

    @Test
    void should_execute_failed_because_of_transaction_expired() throws InvalidTransactionException {
        long buyerId = 1L;
        long sellerId = 2L;
        double amount = 500.0;

        WalletTransaction spyWalletTransaction = spy(new WalletTransaction("sjyuan123", buyerId, sellerId, amount));
        when(spyWalletTransaction.isExpired()).thenReturn(true);

        String updatedPreAssignedId = "t_sjyuan123";

        RedisDistributedLock redisDistributedLock = mock(RedisDistributedLock.class);
        when(redisDistributedLock.lock(updatedPreAssignedId)).thenReturn(true);
        spyWalletTransaction.setRedisDistributedLock(redisDistributedLock);

        spyWalletTransaction.execute();

        assertTrue(spyWalletTransaction.isStatusExpired());
        verify(redisDistributedLock, times(1)).lock(updatedPreAssignedId);
    }

    @Test
    void should_do_nothing_given_transaction_is_executed() throws InvalidTransactionException {
        long buyerId = 1L;
        long sellerId = 2L;
        double amount = 500.0;

        TestFixture testFixture = new TestFixture(buyerId, sellerId, amount, true).invoke();

        WalletTransaction walletTransaction = testFixture.getWalletTransaction();
        WalletService walletService = testFixture.getWalletService();
        String updatedPreAssignedId = testFixture.getUpdatedPreAssignedId();
        RedisDistributedLock redisDistributedLock = testFixture.getRedisDistributedLock();

        walletTransaction.execute();

        walletTransaction.execute();
        assertTrue(walletTransaction.isStatusSuccessful());
        verify(walletService, times(1)).moveMoney(updatedPreAssignedId, buyerId, sellerId, amount);
        verify(redisDistributedLock, times(1)).lock(updatedPreAssignedId);
    }

    @Test
    void should_executed_failed_given_move_money_failed() throws InvalidTransactionException {
        long buyerId = 1L;
        long sellerId = 2L;
        double amount = 500.0;

        TestFixture generate = new TestFixture(buyerId, sellerId, amount, false).invoke();

        WalletTransaction walletTransaction = generate.getWalletTransaction();
        WalletService walletService = generate.getWalletService();
        String updatedPreAssignedId = generate.getUpdatedPreAssignedId();
        RedisDistributedLock redisDistributedLock = generate.getRedisDistributedLock();
        walletTransaction.execute();

        assertTrue(walletTransaction.isStatusFailed());
        verify(walletService, times(1)).moveMoney(updatedPreAssignedId, buyerId, sellerId, amount);
        verify(redisDistributedLock, times(1)).lock(updatedPreAssignedId);
    }

    @Test
    void should_throw_exception_given_buyer_seller_amount_not_suitable() throws InvalidTransactionException {
        assertThrows(InvalidTransactionException.class,
                () -> new WalletTransaction("sjyuan123", null, 2L, 500.0).execute());

        assertThrows(InvalidTransactionException.class,
                () -> new WalletTransaction("sjyuan123", 1L, null, 500.0).execute());

        assertThrows(InvalidTransactionException.class,
                () -> new WalletTransaction("sjyuan123", 1L, 2L, -100.0).execute());
    }


    private class TestFixture {
        private long buyerId;
        private long sellerId;
        private double amount;
        private boolean moveStatus;
        private WalletTransaction walletTransaction;
        private WalletService walletService;
        private String updatedPreAssignedId;
        private RedisDistributedLock redisDistributedLock;

        public TestFixture(long buyerId, long sellerId, double amount, boolean moveStatus) {
            this.buyerId = buyerId;
            this.sellerId = sellerId;
            this.amount = amount;
            this.moveStatus = moveStatus;
        }

        public WalletTransaction getWalletTransaction() {
            return walletTransaction;
        }

        public WalletService getWalletService() {
            return walletService;
        }

        public String getUpdatedPreAssignedId() {
            return updatedPreAssignedId;
        }

        public RedisDistributedLock getRedisDistributedLock() {
            return redisDistributedLock;
        }

        public TestFixture invoke() {
            walletTransaction = new WalletTransaction("sjyuan123", buyerId, sellerId, amount);
            walletService = mock(WalletServiceImpl.class);
            updatedPreAssignedId = "t_sjyuan123";
            when(walletService.moveMoney(updatedPreAssignedId, buyerId, sellerId, amount)).thenReturn(moveStatus ? "123" : null);


            redisDistributedLock = mock(RedisDistributedLock.class);
            when(redisDistributedLock.lock(updatedPreAssignedId)).thenReturn(true);

            walletTransaction.setWalletService(walletService);
            walletTransaction.setRedisDistributedLock(redisDistributedLock);
            return this;
        }
    }
}