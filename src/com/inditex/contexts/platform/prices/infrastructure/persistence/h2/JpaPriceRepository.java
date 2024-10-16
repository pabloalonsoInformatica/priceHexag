package com.inditex.contexts.platform.prices.infrastructure.persistence.h2;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JpaPriceRepository extends JpaRepository<PriceDocument, Long>, JpaSpecificationExecutor<PriceDocument> {

}
