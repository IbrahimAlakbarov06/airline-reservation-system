package org.airline.mspricing.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeRateApiResponse {
    private String base;
    private String date;
    private Map<String, BigDecimal> rates;
}
