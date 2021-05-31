package com.money.changer.cantor.account.services;

import com.money.changer.cantor.common.Currency;
import com.money.changer.cantor.nbprate.NbpWebClient;
import com.money.changer.cantor.nbprate.Rate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;

import java.math.BigDecimal;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
class MoneyChangeServiceTest {

    @Mock
    private NbpWebClient nbpWebClient;

    @InjectMocks
    private MoneyChangeService moneyChangeService;

    @BeforeEach
    public void init() {
        when(nbpWebClient.getNbpRate(Currency.USD)).thenReturn(new Rate("Test", new Date(), new BigDecimal(3.2500), new BigDecimal(3.5000)));
    }


    @Test
    public void shouldChangePlnToUsd() {
        //given
        BigDecimal pln = new BigDecimal(250.50);
        //when
        BigDecimal result = moneyChangeService.sellPLN(pln, Currency.USD);
        //then
        assertThat(new BigDecimal("71.57"), Matchers.comparesEqualTo(result));
    }

    @Test
    public void shouldChangeUsdToPln() {
        //given
        BigDecimal usd = new BigDecimal(150_869.00);
        //when
        BigDecimal result = moneyChangeService.buyPLN(usd, Currency.USD);
        //then
        assertThat(new BigDecimal("490324.25"), Matchers.comparesEqualTo(result));
    }
}