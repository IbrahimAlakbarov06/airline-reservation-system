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
public class PricingResponse {
    private BigDecimal totalPrice;
    private BigDecimal basePrice;
    private BigDecimal totalDiscount;
    private String currency;
    private Map<String, BigDecimal> passengerPrices;

}
