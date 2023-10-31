package com.homework.pricing.service;

import com.homework.pricing.rest.dto.Pricing;
import com.homework.pricing.repository.PricingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SearchPricingService {
    @Autowired
    private PricingRepository pricingRepository;

    public Mono<Pricing> findPricing(SearchRequest searchRequest) {

        return pricingRepository.findCorrespondingPriceRangeWithMostPriority(
                        searchRequest.brandId(),
                        searchRequest.productId(),
                        searchRequest.applicationDate())
                .map(pricingEntity -> new Pricing(
                        pricingEntity.getBrandId(),
                        pricingEntity.getProductId(),
                        pricingEntity.getPriceListId(),
                        pricingEntity.getPrice(),
                        searchRequest.applicationDate()));
    }
}