package com.chidicivok.civokbank.DTOs.responses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ExchangeRateApiResponse {

    BigDecimal rate;

}
