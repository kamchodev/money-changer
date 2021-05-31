package com.money.changer.cantor.nbprate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rate {
    private String no;
    private Date effectiveDate;
    private BigDecimal bid;
    private BigDecimal ask;
}
