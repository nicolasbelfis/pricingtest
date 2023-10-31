package com.homework.pricing.rest.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record Pricing(long brandId, long productId, long priceListId, BigDecimal price, Instant applicationPrice) {
}
