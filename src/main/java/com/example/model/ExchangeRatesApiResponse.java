package com.example.model;

import java.math.BigDecimal;
import java.util.Map;

public class ExchangeRatesApiResponse {

    private Map<String, BigDecimal> rates;

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }
}