package edu.analytics.prices.service;


import edu.analytics.prices.dto.PriceDto;
import edu.analytics.prices.model.Price;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface PriceService {
    List<Price> getEntitiesByTickerAndDateRange(String ticker,
                                                LocalDate start,
                                                LocalDate end);

    Page<PriceDto> getLatestPrices(int page, int size);

    List<Price> findAllByTickerOrderByDate(String ticker);
}
