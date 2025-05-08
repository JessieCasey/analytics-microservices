// src/main/java/edu/analytics/prices/service/PriceService.java
package edu.analytics.prices.service;

import edu.analytics.prices.dto.PriceDto;
import edu.analytics.prices.model.Price;

import java.time.LocalDate;
import java.util.List;

public interface PriceService {
    /** For API endpoints returning DTOs */
    List<PriceDto> getPricesByTicker(String ticker);

    /** For heavy-compute services needing raw entities */
    List<Price> getEntitiesByTickerAndDateRange(String ticker,
                                                LocalDate start,
                                                LocalDate end);

    /** To iterate through all tickers in the collection */
    List<String> getAllTickers();
}
