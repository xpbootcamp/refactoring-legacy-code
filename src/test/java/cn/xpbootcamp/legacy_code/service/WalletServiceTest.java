package cn.xpbootcamp.legacy_code.service;

import cn.xpbootcamp.legacy_code.entity.User;
import cn.xpbootcamp.legacy_code.repository.UserRepositoryImpl;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WalletServiceTest {
    @Test
    void should_move_money_correctly() {
        UserRepositoryImpl userRepository = mock(UserRepositoryImpl.class);
        User buyer = new User();
        buyer.setBalance(500);
        long buyerId = 1L;
        when(userRepository.find(buyerId)).thenReturn(buyer);

        long sellerId = 2L;
        User seller = new User();
        seller.setBalance(200);
        when(userRepository.find(sellerId)).thenReturn(seller);
        WalletService walletService = new WalletService(userRepository);

        String id = walletService.moveMoney("123", buyerId, sellerId, 150);

        assertThat(buyer.getBalance()).isEqualTo(350);
        assertThat(seller.getBalance()).isEqualTo(350);
        assertThat(id).endsWith("123");
    }

    @Test
    void should_move_money_failed_given_balanced_is_not_enough() {
        UserRepositoryImpl userRepository = mock(UserRepositoryImpl.class);
        User buyer = new User();
        buyer.setBalance(200);
        long buyerId = 1L;
        when(userRepository.find(buyerId)).thenReturn(buyer);

        long sellerId = 2L;
        User seller = new User();
        seller.setBalance(200);
        when(userRepository.find(sellerId)).thenReturn(seller);
        WalletService walletService = new WalletService(userRepository);

        String id = walletService.moveMoney("123", buyerId, sellerId, 300);

        assertThat(id).isNull();
    }

}