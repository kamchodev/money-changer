package com.money.changer.cantor.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Not enough funds")
public class BalanceException extends RuntimeException {
}