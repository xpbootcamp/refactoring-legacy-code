package cn.xpbootcamp.legacy_code;

import cn.xpbootcamp.legacy_code.service.WalletService;
import cn.xpbootcamp.legacy_code.service.WalletServiceImpl;
import cn.xpbootcamp.legacy_code.utils.RedisDistributedLock;
import org.junit.jupiter.api.Test;

import javax.transaction.InvalidTransactionException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class WalletTransactionTest {

    @Test
    void should_execute_successfully() throws InvalidTransactionException {
        WalletTransaction walletTransaction = new WalletTransaction("sjyuan123", 1L, 2L, 500.0);
        WalletService walletService = mock(WalletServiceImpl.class);
        when(walletService.moveMoney("t_sjyuan123", 1L, 2L, 500.0)).thenReturn("123");


        RedisDistributedLock redisDistributedLock = mock(RedisDistributedLock.class);
        when(redisDistributedLock.lock("t_sjyuan123")).thenReturn(true);

        walletTransaction.setWalletService(walletService);
        walletTransaction.setRedisDistributedLock(redisDistributedLock);

        walletTransaction.execute();

        assertTrue(walletTransaction.isSuccessful());
        verify(walletService, times(1)).moveMoney("t_sjyuan123", 1L, 2L, 500.0);
        verify(redisDistributedLock, times(1)).lock("t_sjyuan123");
    }

}