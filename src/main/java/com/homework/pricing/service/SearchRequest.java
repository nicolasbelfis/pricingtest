package com.homework.pricing.service;

import java.time.Instant;

public record SearchRequest(long brandId, long productId, Instant applicationDate) {
}
