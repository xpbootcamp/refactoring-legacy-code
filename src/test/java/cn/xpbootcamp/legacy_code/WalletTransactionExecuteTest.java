package cn.xpbootcamp.legacy_code;

import cn.xpbootcamp.legacy_code.enums.STATUS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.transaction.InvalidTransactionException;

import static org.junit.jupiter.api.Assertions.*;

class WalletTransactionExecuteTest {

    @Test
    void assert_exception_when_buyId_is_null() {
        WalletTransaction walletTransaction = new WalletTransaction("something", null, 1L, 1L, "someId");
        Assertions.assertThrows(InvalidTransactionException.class, () -> {
            walletTransaction.execute();
        }, "This is an invalid transaction");
    }

    @Test
    void assert_exception_when_sellerId_is_null() {
        WalletTransaction walletTransaction = new WalletTransaction("something", 1L, null, 1L, "someId");
        Assertions.assertThrows(InvalidTransactionException.class, () -> {
            walletTransaction.execute();
        }, "This is an invalid transaction");
    }

    @Test
    // TODO: there is no place to set amount, could be a bug
    void assert_exception_when_amount_below_zero() {
        WalletTransaction walletTransaction = new WalletTransaction("something", 1L, 1L, 1L, "someId");
        TestUtil.setValue(walletTransaction, "amount", -2d);
        Assertions.assertThrows(InvalidTransactionException.class, () -> {
            walletTransaction.execute();
        }, "This is an invalid transaction");
    }

    @Test
    void should_return_true_when_status_is_executed() throws InvalidTransactionException {
        WalletTransaction walletTransaction = new WalletTransaction("something", 1L, 1L, 1L, "someId");
        TestUtil.setValue(walletTransaction, "amount", 2d);
        TestUtil.setValue(walletTransaction, "status", STATUS.EXECUTED);
        assertTrue(walletTransaction.execute());
    }
}