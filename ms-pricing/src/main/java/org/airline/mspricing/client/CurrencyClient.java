package org.airline.mspricing.client;

import org.airline.mspricing.model.dto.response.ExchangeRateApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "currency-api", url = "https://api.exchangerate-api.com/v4")
public interface CurrencyClient {

    @GetMapping("/latest/{baseCurrency}")
    ExchangeRateApiResponse getExchangeRates(@PathVariable String baseCurrency);
}