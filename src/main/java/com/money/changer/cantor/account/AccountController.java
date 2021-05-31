package com.money.changer.cantor.account;

import com.money.changer.cantor.account.model.AccountDto;
import com.money.changer.cantor.account.model.NewAccount;
import com.money.changer.cantor.account.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@RestController
@AllArgsConstructor
@Validated
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/get-account/{id}")
    public AccountDto getAccount(@PathVariable long id) {
        return accountService.getAccount(id);
    }

    @PostMapping("/create-account")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createAccount(@Valid @RequestBody NewAccount newAccount) {
        return accountService.createAccount(newAccount);
    }

    @PatchMapping("/change-PLN-USD")
    public void changePLNtoUSD(@RequestParam long id, @Min(1) @Digits(integer = 10, fraction = 2) @RequestParam BigDecimal amountPLN) {
        accountService.changePLNtoUSD(id, amountPLN);
    }

    @PatchMapping("/change-USD-PLN")
    public void changeUSDtoPLN(@RequestParam long id, @Min(1) @Digits(integer = 10, fraction = 2) @RequestParam BigDecimal amountUSD) {
        accountService.changeUSDtoPLN(id, amountUSD);
    }
}
