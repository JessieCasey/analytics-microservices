package edu.analytics.prices.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SMAResult {
    private String ticker;
    private LocalDate date;
    private BigDecimal sma;
}