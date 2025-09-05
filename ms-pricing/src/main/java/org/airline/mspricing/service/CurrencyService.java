package org.airline.mspricing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.airline.mspricing.client.CurrencyClient;
import org.airline.mspricing.exception.CurrencyException;
import org.airline.mspricing.exception.PriceException;
import org.airline.mspricing.model.dto.response.CurrencyResponse;
import org.airline.mspricing.model.dto.response.ExchangeRateApiResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyService {

    private final CurrencyClient currencyClient;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String RATE_KEY = "rate:";
    private static final List<String> SUPPORTED_CURRENCIES = Arrays.asList(
            "USD", "EUR", "GBP", "AZN", "TRY", "RUB", "CAD", "AUD", "JPY"
    );

    public BigDecimal convertFromUSD(BigDecimal usdAmount, String toCurrency) {
        if ("USD".equals(toCurrency)) {
            return usdAmount;
        }

        try {
            BigDecimal rate = getRate("USD", toCurrency);
            return usdAmount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            log.error("Currency conversion failed, using fallback rate", e);
            return usdAmount.multiply(getFallbackRate(toCurrency));
        }
    }

    public BigDecimal getRate(String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return BigDecimal.ONE;
        }

        String cacheKey = RATE_KEY + fromCurrency + ":" + toCurrency;
        String cacheRate = redisTemplate.opsForValue().get(cacheKey);

        if (cacheRate != null) {
            return new BigDecimal(cacheRate);
        }

        BigDecimal rate = fetchRateFromApi(fromCurrency, toCurrency);

        redisTemplate.opsForValue().set(cacheKey, rate.toString(), 1, TimeUnit.HOURS);

        return rate;
    }

    private BigDecimal fetchRateFromApi(String fromCurrency, String toCurrency) {
        try {
            ExchangeRateApiResponse response = currencyClient.getExchangeRates(fromCurrency);

            if (response != null && response.getRates() != null) {
                BigDecimal rate = response.getRates().get(toCurrency);
                if (rate != null) {
                    return rate;
                }
            }

            throw new CurrencyException("Rate not found");
        } catch (Exception e) {
            log.error("API call failed: {}", e.getMessage());
            return getFallbackRate(toCurrency);
        }
    }

    private BigDecimal getFallbackRate(String currency) {
        return switch (currency) {
            case "EUR" -> new BigDecimal("1.85");
            case "GBP" -> new BigDecimal("0.75");
            case "AZN" -> new BigDecimal("1.70");
            case "TRY" -> new BigDecimal("29.00");
            case "RUB" -> new BigDecimal("80.00");
            case "CAD" -> new BigDecimal("1.35");
            case "AUD" -> new BigDecimal("1.50");
            case "JPY" -> new BigDecimal("150.00");
            default -> BigDecimal.ONE;
        };
    }

    public List<CurrencyResponse> getAllRates() {
        return SUPPORTED_CURRENCIES.stream()
                .filter(currency -> !"USD".equals(currency))
                .map(currency -> CurrencyResponse.builder()
                        .fromCurrency("USD")
                        .toCurrency(currency)
                        .rate(getRate("USD", currency))
                        .lastUpdated(LocalDateTime.now())
                        .build())
                .toList();
    }
}

