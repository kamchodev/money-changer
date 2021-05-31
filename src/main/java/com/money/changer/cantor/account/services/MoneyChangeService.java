package com.money.changer.cantor.account.services;

import com.money.changer.cantor.common.Currency;
import com.money.changer.cantor.nbprate.NbpWebClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@AllArgsConstructor
public class MoneyChangeService {
    private final NbpWebClient nbpWebClient;

    public BigDecimal sellPLN(BigDecimal pln, Currency currency) {
        BigDecimal currentBidRate = nbpWebClient.getNbpRate(currency).getAsk();
        return pln.divide(currentBidRate, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal buyPLN(BigDecimal usd, Currency currency) {
        BigDecimal currentAskRate = nbpWebClient.getNbpRate(currency).getBid();
        return usd.multiply(currentAskRate).setScale(2, RoundingMode.HALF_UP);
    }
}
