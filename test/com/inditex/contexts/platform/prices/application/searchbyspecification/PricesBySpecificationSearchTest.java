package com.inditex.contexts.platform.prices.application.searchbyspecification;

import com.inditex.contexts.platform.prices.domain.Price;
import com.inditex.contexts.platform.prices.domain.PriceMother;
import com.inditex.contexts.platform.prices.infrastructure.mocks.PriceDocumentMother;
import com.inditex.contexts.platform.prices.infrastructure.mocks.h2.PricePageMother;
import com.inditex.contexts.platform.prices.infrastructure.persistence.H2PriceRepository;
import com.inditex.contexts.platform.prices.infrastructure.persistence.h2.JpaPriceRepository;
import com.inditex.contexts.platform.prices.infrastructure.persistence.h2.PriceDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
class PricesBySpecificationSearchTest {

    @Mock
    private JpaPriceRepository jpaPriceRepository;

    @InjectMocks
    private H2PriceRepository h2PriceRepositoryMock;


    private PricesBySpecificationSearch pricesBySpecificationSearch;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        pricesBySpecificationSearch = new PricesBySpecificationSearch(h2PriceRepositoryMock);
    }

    @Test
    void testGetProductPriceOfBrandOnDate_Found() {
        // Arrange
        PricesBySpecificationSearchQuery pricesBySpecificationSearchQuery = PricesBySpecificationSearchQueryMother.createDefault();
        PriceDocument expectedPriceDocument = PriceDocumentMother.createDefault();
        PricesByServiceSpecificationSearchResponse expectedPricesByServiceSpecificationSearchResponse = PricesByServiceSpecificationSearchResponseMother
                .fromArrayPrices(new Price[]{
                        PriceMother.fromPriceDocument(expectedPriceDocument)
                });
        PageImpl<PriceDocument> pricePage = PricePageMother.createOneResult(expectedPriceDocument);

        Mockito.when(jpaPriceRepository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenReturn(pricePage);

        // Act
        PricesByServiceSpecificationSearchResponse result = pricesBySpecificationSearch.run(pricesBySpecificationSearchQuery);
        // Assert
        assertEquals(expectedPricesByServiceSpecificationSearchResponse, result);
    }

    @Test
    void testGetProductPriceOfBrandOnDate_NotFound() {
        // Arrange
        PricesBySpecificationSearchQuery pricesBySpecificationSearchQuery = PricesBySpecificationSearchQueryMother.createDefault();
        PricesByServiceSpecificationSearchResponse expectedPricesByServiceSpecificationSearchResponse = PricesByServiceSpecificationSearchResponseMother
                .fromArrayPrices(new Price[0]);
        PageImpl<PriceDocument> pricePage = PricePageMother.createNoResults();

        Mockito.when(jpaPriceRepository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenReturn(pricePage);

        // Act
        PricesByServiceSpecificationSearchResponse result = pricesBySpecificationSearch.run(pricesBySpecificationSearchQuery);

        // Assert
        assertEquals(expectedPricesByServiceSpecificationSearchResponse, result);
    }
}