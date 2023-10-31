package com.homework.pricing.service;

import com.homework.pricing.rest.dto.Pricing;
import com.homework.pricing.repository.PricingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;


@ExtendWith(MockitoExtension.class)
class SearchPricingServiceTest {

    public static final Instant APPLICATION_DATE = Instant.now();
    public static final long BRAND_ID = 1L;
    public static final long PRODUCT_ID = 2L;
    public static final long PRICE_LIST_ID = 99L;
    @InjectMocks
    SearchPricingService service;

    @Mock
    PricingRepository pricingRepository;

    @Test
    void return_empty_mono_if_no_result_found() {

        Mockito.when(pricingRepository.findCorrespondingPriceRangeWithMostPriority(BRAND_ID, PRODUCT_ID, APPLICATION_DATE))
                .thenReturn(Mono.empty());

        Mono<Pricing> pricing = service.findPricing(new SearchRequest(BRAND_ID, PRODUCT_ID, APPLICATION_DATE));

        StepVerifier.create(pricing)
                .expectComplete()
                .verify();

    }

    @Test
    void return_pricing_when_pricing_found() {

        Mockito.when(pricingRepository.findCorrespondingPriceRangeWithMostPriority(BRAND_ID, PRODUCT_ID, APPLICATION_DATE)).thenReturn(
                Mono.just(
                        new PricingRepository.PricingEntity(
                                1L,
                                BRAND_ID,
                                PRODUCT_ID,
                                PRICE_LIST_ID,
                                new BigDecimal("12.34"),
                                "EUR",
                                1,
                                Instant.now(),
                                Instant.now())));

        Mono<Pricing> pricing = service.findPricing(new SearchRequest(BRAND_ID, PRODUCT_ID, APPLICATION_DATE));

        StepVerifier.create(pricing)
                .expectNext(new Pricing(BRAND_ID, PRODUCT_ID, PRICE_LIST_ID, new BigDecimal("12.34"), APPLICATION_DATE))
                .expectComplete()
                .verify();

    }
}