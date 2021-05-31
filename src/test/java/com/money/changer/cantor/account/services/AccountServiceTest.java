package com.money.changer.cantor.account.services;

import com.money.changer.cantor.account.model.Account;
import com.money.changer.cantor.account.repository.AccountRepository;
import com.money.changer.cantor.common.Currency;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private MoneyChangeService moneyChangeService;
    @Mock
    private AccountRepository accountRepository;
    @Captor
    ArgumentCaptor<Account> captor;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    public void init() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(new Account(1, "Anna", "Kowalska", new BigDecimal(2000.00), new BigDecimal(150.00))));
    }

    @Test
    public void shouldChangePLNtoUSD() {
        //given
        long id = 1;
        BigDecimal amountPln = new BigDecimal(1000.00);
        when(moneyChangeService.sellPLN(new BigDecimal(1000), Currency.USD)).thenReturn(new BigDecimal(300.00));
        //when
        accountService.changePLNtoUSD(id, amountPln);
        //then
        verify(accountRepository).save(captor.capture());
        Account account = captor.getValue();
        assertEquals(account.getId(), 1);
        assertEquals(account.getName(), "Anna");
        assertEquals(account.getSurname(), "Kowalska");
        assertThat(new BigDecimal(1000.00), Matchers.comparesEqualTo(account.getAccountBalancePLN()));
        assertThat(new BigDecimal(450.00), Matchers.comparesEqualTo(account.getAccountBalanceUSD()));
    }

    @Test
    public void shouldChangeUSDtoPLN() {
        //given
        long id = 1;
        BigDecimal amountUsd = new BigDecimal(100.00);
        when(moneyChangeService.buyPLN(new BigDecimal(100), Currency.USD)).thenReturn(new BigDecimal(350.00));
        //when
        accountService.changeUSDtoPLN(id, amountUsd);
        //then
        verify(accountRepository).save(captor.capture());
        Account account = captor.getValue();
        assertEquals(account.getId(), 1);
        assertEquals(account.getName(), "Anna");
        assertEquals(account.getSurname(), "Kowalska");
        assertThat(new BigDecimal(2350.00), Matchers.comparesEqualTo(account.getAccountBalancePLN()));
        assertThat(new BigDecimal(50.00), Matchers.comparesEqualTo(account.getAccountBalanceUSD()));
    }
}