package cn.xpbootcamp.legacy_code;

import cn.xpbootcamp.legacy_code.enums.STATUS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.transaction.InvalidTransactionException;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class WalletTransactionExecuteTest {

    @Test
    void assert_exception_when_buyId_is_null() {
        WalletTransaction walletTransaction = new WalletTransaction("something", null, 1L, 1d);
        Assertions.assertThrows(InvalidTransactionException.class, () -> {
            walletTransaction.execute();
        }, "This is an invalid transaction");
    }

    @Test
    void assert_exception_when_sellerId_is_null() {
        WalletTransaction walletTransaction = new WalletTransaction("something", 1L, null, 1d);
        Assertions.assertThrows(InvalidTransactionException.class, () -> {
            walletTransaction.execute();
        }, "This is an invalid transaction");
    }

    @Test
    void assert_exception_when_amount_below_zero() {
        WalletTransaction walletTransaction = new WalletTransaction("something", 1L, 1L, -2d);
        Assertions.assertThrows(InvalidTransactionException.class, () -> {
            walletTransaction.execute();
        }, "This is an invalid transaction");
    }

    @Test
    void should_return_true_when_status_is_executed() throws InvalidTransactionException {
        WalletTransaction walletTransaction = new WalletTransaction("something", 1L, 1L, 2d);
        TestUtil.setValue(walletTransaction, "status", STATUS.EXECUTED);
        assertTrue(walletTransaction.execute());
    }

    @Test
    void should_return_false_if_not_locked() throws InvalidTransactionException {
        MockWalletTransaction walletTransaction = new MockWalletTransaction("something", 1L, 1L, 1d);
        walletTransaction.setLocked(false);
        assertFalse(walletTransaction.execute());
    }

    @Test
    void should_return_false_if_20_days_ago() throws InvalidTransactionException {
        long current = System.currentTimeMillis();
        MockWalletTransaction walletTransaction = new MockWalletTransaction("something", 1L, 1L, 1d);
        long duration = 20 * 24 * 3600 * 1000 + 5000; // 20days and 5 sec
        walletTransaction.setCurrentTimeMillis(current + duration);
        walletTransaction.setLocked(true);

        assertFalse(walletTransaction.execute());
        assertEquals(STATUS.EXPIRED, walletTransaction.getStatus());
    }

    private class MockWalletTransaction extends WalletTransaction{

        private boolean isLocked;
        private long currentTimeMillis;

        public MockWalletTransaction(String preAssignedId, Long buyerId, Long sellerId, Double amount) {
            super(preAssignedId, buyerId, sellerId, amount);
        }

        @Override
        protected boolean lock(String id) {
            return isLocked;
        }

        @Override
        protected long getCurrentTimeMillis() {
            return currentTimeMillis;
        }

        @Override
        protected void unlock() {
            // do nothing
        }

        public void setLocked(boolean locked) {
            isLocked = locked;
        }

        public void setCurrentTimeMillis(long currentTimeMillis) {
            this.currentTimeMillis = currentTimeMillis;
        }

        public STATUS getStatus() {
            Field field = TestUtil.getField(this.getClass().getSuperclass(), "status");
            return (STATUS) TestUtil.getValue(this, field);
        }
    }
}