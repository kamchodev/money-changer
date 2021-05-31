package com.money.changer.cantor.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewAccount {
    @NotNull(message = "Name has to be present")
    @Size(min = 2)
    private String name;
    @NotNull(message = "Surname has to be present")
    @Size(min = 2)
    private String surname;
    @NotNull(message = "Account Balance PLN has to be present")
    @Digits(integer = 10, fraction = 2)
    @Min(value = 1)
    private BigDecimal accountBalancePLN;
    @NotNull
    @Digits(integer = 10, fraction = 2)
    @Min(value = 0)
    private BigDecimal accountBalanceUSD = new BigDecimal("0");


    public static Account createAccount(NewAccount newAccount) {
        return Account.builder()
                .name(newAccount.getName())
                .surname(newAccount.getSurname())
                .accountBalancePLN(newAccount.getAccountBalancePLN())
                .accountBalanceUSD(newAccount.getAccountBalanceUSD())
                .build();
    }
}
