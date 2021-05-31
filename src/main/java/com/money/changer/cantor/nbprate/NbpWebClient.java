package com.money.changer.cantor.nbprate;

import com.money.changer.cantor.common.Currency;
import io.netty.handler.timeout.ReadTimeoutException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.http.HttpTimeoutException;
import java.time.Duration;


@Service
public class NbpWebClient {

    private final String url = "http://api.nbp.pl/api/exchangerates/rates/c/";
    private final WebClient webClient = WebClient.builder().build();

    public Rate getNbpRate(Currency currency) {
        Rate[] rateTable = webClient.get()
                .uri(url + currency.name())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(NbpRate.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorMap(ReadTimeoutException.class, ex -> new HttpTimeoutException("ReadTimeout"))
                .map(s -> s.getRates())
                .block();
        return rateTable[0];
    }
}
