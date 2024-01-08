package com.example.util;

import com.example.model.ExchangeRatesApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
@Slf4j
public class CurrencyConverter {

    @Value("${exchange.rate.api.url}")
    private String exchangeApiUrl;

    private final RestTemplate restTemplate;

    public CurrencyConverter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return amount; // No conversion needed
        }

        BigDecimal exchangeRate = getExchangeRate(fromCurrency, toCurrency);
        log.info("The exchange rate for {} to {} is {}", fromCurrency, toCurrency, exchangeRate);
        return amount.multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        String apiUrl = exchangeApiUrl + "?base=" + fromCurrency;
        ExchangeRatesApiResponse response = restTemplate.getForObject(apiUrl, ExchangeRatesApiResponse.class);

        if (response != null && response.getRates() != null) {
            BigDecimal toCurrencyRate = response.getRates().get(toCurrency);
            if (toCurrencyRate != null) {
                return toCurrencyRate;
            }
        }

        throw new RuntimeException("Unable to fetch exchange rate for " + fromCurrency + " to " + toCurrency);
    }
}
