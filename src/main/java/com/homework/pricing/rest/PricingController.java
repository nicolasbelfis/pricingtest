package com.homework.pricing.rest;

import com.homework.pricing.rest.dto.Pricing;
import com.homework.pricing.service.SearchPricingService;
import com.homework.pricing.service.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RestController
@RequestMapping("/pricing")
public class PricingController {

    @Autowired
    private SearchPricingService searchPricingService;


    @GetMapping(value = "/{brandId}/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<? extends ResponseEntity<Pricing>> getMostPrioritaryPricing(
            @PathVariable long brandId,
            @PathVariable long productId,
            @RequestParam(required = true) Instant applicationDate
    ) {

        return searchPricingService.findPricing(new SearchRequest(brandId, productId, applicationDate))
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

}