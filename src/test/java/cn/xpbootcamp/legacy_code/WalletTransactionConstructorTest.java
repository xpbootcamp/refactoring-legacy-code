package cn.xpbootcamp.legacy_code;

import cn.xpbootcamp.legacy_code.enums.STATUS;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WalletTransactionConstructorTest {

    @Test
    public void should_set_id_as_t_pre_assigned_when_it_has_value() {
        WalletTransaction walletTransaction = new WalletTransaction("abc", 1L, 1L, 1d);
        Object id = TestUtil.getValue(walletTransaction, "id");
        assertEquals("t_abc", id);
    }

    @Test
    // TODO: to check if it is a bug: should_generate_id_and_prefix_with_t_when_no_id_provide
    public void should_be_t_underline_and_when_preAssignedId_is_empty() {
        WalletTransaction walletTransaction = new WalletTransaction("", 1L, 1L, 1d);
        Object id = TestUtil.getValue(walletTransaction, "id");
        assertEquals("t_", id);
    }

    @Test
    // TODO: to check if it is a bug: should_generate_id_and_prefix_with_t_when_no_id_provide
    public void should_be_t_null_and_when_preAssignedId_is_null() {
        WalletTransaction walletTransaction = new WalletTransaction(null, 1L, 1L, 1d);
        Object id = TestUtil.getValue(walletTransaction, "id");
        assertEquals("t_null", id);
    }

    @Test
    public void should_test_other_fields_assignment(){
        WalletTransaction walletTransaction = new WalletTransaction(null, 1L, 2L, 3d);
        assertEquals(1L, TestUtil.getValue(walletTransaction, "buyerId"));
        assertEquals(2L, TestUtil.getValue(walletTransaction, "sellerId"));
        assertEquals(3d, TestUtil.getValue(walletTransaction, "amount"));
        assertEquals(STATUS.TO_BE_EXECUTED, TestUtil.getValue(walletTransaction, "status"));
        assertNotNull(TestUtil.getValue(walletTransaction, "createdTimestamp"));

    }

}
