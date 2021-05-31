package com.money.changer.cantor.account.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private long id;
    private String name;
    private String surname;
    private BigDecimal accountBalancePLN;
    private BigDecimal accountBalanceUSD;

    public static AccountDto createDto(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .name(account.getName())
                .surname(account.getSurname())
                .accountBalancePLN(account.getAccountBalancePLN())
                .accountBalanceUSD(account.getAccountBalanceUSD())
                .build();
    }
}
