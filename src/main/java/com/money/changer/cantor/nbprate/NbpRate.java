package com.money.changer.cantor.nbprate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NbpRate {
    private String table;
    private String currency;
    private String code;
    private Rate[] rates;
}
