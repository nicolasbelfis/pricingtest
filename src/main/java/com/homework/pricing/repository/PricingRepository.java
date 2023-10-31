package com.homework.pricing.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;

public interface PricingRepository extends ReactiveCrudRepository<PricingRepository.PricingEntity, Long> {

    @Query("select * from pricing_table p where " +
            "p.brand_id = :brandId and p.product_id = :productId " +
            "and p.start_date_range < :applicationDate " +
            "and p.end_date_range > :applicationDate " +
            "order by priority asc limit 1")
    Mono<PricingEntity> findCorrespondingPriceRangeWithMostPriority(long brandId,
                                                                    long productId,
                                                                    Instant applicationDate);

    @Table("pricing_table")
    @AllArgsConstructor
    @Data
    public class PricingEntity {
        @Id
        private long id;
        private long brandId;
        private long productId;
        private long priceListId;
        private BigDecimal price;
        private String currency;
        private int priority;
        private Instant startDateRange;
        private Instant endDateRange;

    }

}




