package com.money.changer.cantor.account.services;

import com.money.changer.cantor.account.model.Account;
import com.money.changer.cantor.account.model.AccountDto;
import com.money.changer.cantor.account.model.NewAccount;
import com.money.changer.cantor.account.repository.AccountRepository;
import com.money.changer.cantor.common.AccountNotExist;
import com.money.changer.cantor.common.BalanceException;
import com.money.changer.cantor.common.Currency;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class AccountService {

    private final MoneyChangeService moneyChangeService;
    private final AccountRepository accountRepository;

    public AccountDto getAccount(Long id) {
        return AccountDto.createDto(getAccountById(id));
    }


    public AccountDto createAccount(NewAccount newAccount) {
        return AccountDto.createDto(accountRepository.save(NewAccount.createAccount(newAccount)));
    }

    public void changePLNtoUSD(long id, BigDecimal amountPln) {
        Account account = getAccountById(id);
        if (amountPln.compareTo(account.getAccountBalancePLN()) == 1) {
            throw new BalanceException();
        }
        BigDecimal newPlnBalance = account.getAccountBalancePLN().subtract(amountPln);
        BigDecimal newUsdBalance = account.getAccountBalanceUSD().add(moneyChangeService.sellPLN(amountPln, Currency.USD));
        account.setAccountBalancePLN(newPlnBalance);
        account.setAccountBalanceUSD(newUsdBalance);
        accountRepository.save(account);
    }

    public void changeUSDtoPLN(long id, BigDecimal amountUsd) {
        Account account = getAccountById(id);
        if (amountUsd.compareTo(account.getAccountBalanceUSD()) == 1) {
            throw new BalanceException();
        }
        BigDecimal newPlnBalance = account.getAccountBalancePLN().add(moneyChangeService.buyPLN(amountUsd, Currency.USD));
        BigDecimal newUsdBalance = account.getAccountBalanceUSD().subtract(amountUsd);
        account.setAccountBalancePLN(newPlnBalance);
        account.setAccountBalanceUSD(newUsdBalance);
        accountRepository.save(account);
    }

    private Account getAccountById(long id) {
        return accountRepository.findById(id).orElseThrow(AccountNotExist::new);
    }

}
