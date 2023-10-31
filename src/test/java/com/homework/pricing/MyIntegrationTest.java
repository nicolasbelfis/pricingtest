package com.homework.pricing;

import com.homework.pricing.rest.dto.Pricing;
import com.homework.pricing.repository.PricingRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class MyIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PricingRepository pricingRepository;

    @Test
    public void application_date_param_must_be_present() {
        webTestClient.get()
                .uri("/pricing/123/456")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .value(Matchers.containsString("Required query parameter 'applicationDate' is not present."));
    }

    @Test
    public void must_find_pricing_if_date_in_corresponding_range_case_1() {

        String applicationDate = "2020-12-14T10:00:00Z";
        webTestClient.get()
                .uri("/pricing/1/35455?applicationDate=" + applicationDate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Pricing.class)
                .value(Matchers.equalTo(new Pricing(
                        1,
                        35455,
                        1,
                        new BigDecimal("35.50"),
                        Instant.parse(applicationDate))));

    }

    @Test
    public void must_find_pricing_if_date_in_corresponding_range_case_2() {

        String applicationDate = "2020-12-14T21:00:00Z";
        webTestClient.get()
                .uri("/pricing/1/35455?applicationDate=" + applicationDate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Pricing.class)
                .equals(new Pricing(
                        1,
                        35455,
                        1,
                        new BigDecimal("35.50"),
                        Instant.parse(applicationDate)));
    }

    @Test
    public void must_find_pricing_with_highest_priority_if_date_in_corresponding() {

        String applicationDate = "2020-12-14T16:00:00Z";
        webTestClient.get()
                .uri("/pricing/1/35455?applicationDate=" + applicationDate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Pricing.class)
                .equals(new Pricing(
                        1,
                        35455,
                        2,
                        new BigDecimal("25.45"),
                        Instant.parse(applicationDate)));
    }

    @Test
    public void must_find_pricing_with_highest_priority_if_date_in_corresponding_case_2() {

        String applicationDate = "2020-12-15T10:00:00Z";
        webTestClient.get()
                .uri("/pricing/1/35455?applicationDate=" + applicationDate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Pricing.class)
                .equals(new Pricing(
                        1,
                        35455,
                        3,
                        new BigDecimal("30.50"),
                        Instant.parse(applicationDate)));
    }

    @Test
    public void must_find_pricing_with_highest_priority_if_date_in_corresponding_case_3() {

        String applicationDate = "2020-12-16T21:00:00Z";
        webTestClient.get()
                .uri("/pricing/1/35455?applicationDate=" + applicationDate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Pricing.class)
                .equals(new Pricing(
                        1,
                        35455,
                        4,
                        new BigDecimal("38.95"),
                        Instant.parse(applicationDate)));
    }

    @Test
    public void must_not_find_pricing_when_date_is_not_in_a_known_range() {

        String applicationDate = "2099-12-14T16:00:00Z";
        webTestClient.get()
                .uri("/pricing/1/35455?applicationDate=" + applicationDate)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .isEmpty();
    }
}



