package com.inditex.apps.platform.backend.features;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //Spring context
@ActiveProfiles("test")
class PlatformApplicationTests {

    @Value("${local.server.port}")
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost:" + port + "/api";
    }


    @Test
    void testGetProductPriceForDate2020_06_14_10_WithSingleOccurrence() {
        final long productId = 35455;
        final long brandId = 1;

        given()
                .param("productId", productId)
                .param("brandId", brandId)
                .param("priceDate", "2020-06-14T10:00:00")
                .when()
                .get("/prices")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("count", equalTo(1))
                .body("prices.size()", equalTo(1))
                .body("prices[0].priceList", equalTo(1))
                .body("prices[0].productId", equalTo((int) productId))
                .body("prices[0].brandId", equalTo((int) brandId))
                .body("prices[0].startDate", equalTo("2020-06-14T00:00:00"))
                .body("prices[0].endDate", equalTo("2020-12-31T23:59:59"))
                .body("prices[0].value", equalTo(35.50f));
    }

    @Test
    void testGetProductPriceForDate2020_06_14_16_ReturnsOnlyHighestPriority() {
        final long productId = 35455;
        final long brandId = 1;

        given()
                .param("productId", productId)
                .param("brandId", brandId)
                .param("priceDate", "2020-06-14T16:00:00")
                .when()
                .get("/prices")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("count", equalTo(1))
                .body("prices.size()", equalTo(1))
                .body("prices[0].priceList", equalTo(2))
                .body("prices[0].productId", equalTo((int) productId))
                .body("prices[0].brandId", equalTo((int) brandId))
                .body("prices[0].startDate", equalTo("2020-06-14T15:00:00"))
                .body("prices[0].endDate", equalTo("2020-06-14T18:30:00"))
                .body("prices[0].value", equalTo(25.45f));
    }

    @Test
    void testGetProductPriceForDate2020_06_14_21_ReturnsOnlyHighestPriority() {
        final long productId = 35455;
        final long brandId = 1;

        given()
                .param("productId", productId)
                .param("brandId", brandId)
                .param("priceDate", "2020-06-14T21:00:00")
                .when()
                .get("/prices")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("count", equalTo(1))
                .body("prices.size()", equalTo(1))
                .body("prices[0].priceList", equalTo(1))
                .body("prices[0].productId", equalTo((int) productId))
                .body("prices[0].brandId", equalTo((int) brandId))
                .body("prices[0].startDate", equalTo("2020-06-14T00:00:00"))
                .body("prices[0].endDate", equalTo("2020-12-31T23:59:59"))
                .body("prices[0].value", equalTo(35.50f));
    }

    @Test
    void testGetProductPriceForDate2020_06_15_10_ReturnsOnlyHighestPriority() {
        final long productId = 35455;
        final long brandId = 1;

        given()
                .param("productId", productId)
                .param("brandId", brandId)
                .param("priceDate", "2020-06-15T10:00:00")
                .when()
                .get("/prices")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("count", equalTo(1))
                .body("prices.size()", equalTo(1))
                .body("prices[0].priceList", equalTo(3))
                .body("prices[0].productId", equalTo((int) productId))
                .body("prices[0].brandId", equalTo((int) brandId))
                .body("prices[0].startDate", equalTo("2020-06-15T00:00:00"))
                .body("prices[0].endDate", equalTo("2020-06-15T11:00:00"))
                .body("prices[0].value", equalTo(30.50f));
    }


    @Test
    void testGetProductPriceForDate2020_06_16_21_ReturnsOnlyHighestPriority() {
        final long productId = 35455;
        final long brandId = 1;

        given()
                .param("productId", productId)
                .param("brandId", brandId)
                .param("priceDate", "2020-06-16T21:00:00")
                .when()
                .get("/prices")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("count", equalTo(1))
                .body("prices.size()", equalTo(1))
                .body("prices[0].priceList", equalTo(4))
                .body("prices[0].productId", equalTo((int) productId))
                .body("prices[0].brandId", equalTo((int) brandId))
                .body("prices[0].startDate", equalTo("2020-06-15T16:00:00"))
                .body("prices[0].endDate", equalTo("2020-12-31T23:59:59"))
                .body("prices[0].value", equalTo(38.95f));
    }


    @Test
    void testGetProductPriceForDateOutOfRange() {
        final long productId = 35455;
        final long brandId = 1;

        given()
                .param("productId", productId)
                .param("brandId", brandId)
                .param("priceDate", "2021-08-16T21:00:00")
                .when()
                .get("/prices")
                .then()
                .statusCode(404)
                .body(emptyOrNullString());
    }

}
