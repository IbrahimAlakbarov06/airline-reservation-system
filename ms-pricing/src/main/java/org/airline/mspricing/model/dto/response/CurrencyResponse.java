package org.airline.mspricing.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrencyResponse {
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal rate;
    private LocalDateTime lastUpdated;
}
