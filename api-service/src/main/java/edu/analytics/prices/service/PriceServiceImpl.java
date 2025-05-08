// src/main/java/edu/analytics/prices/service/impl/PriceServiceImpl.java
package edu.analytics.prices.service;

import edu.analytics.prices.dto.PriceDto;
import edu.analytics.prices.mapper.PriceMapper;
import edu.analytics.prices.model.Price;
import edu.analytics.prices.repository.PriceRepository;
import edu.analytics.prices.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceRepository repo;
    private final PriceMapper mapper;
    private final MongoTemplate mongo;

    @Override
    public List<PriceDto> getPricesByTicker(String ticker) {
        return repo.findByTickerOrderByDateDesc(ticker).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Price> getEntitiesByTickerAndDateRange(String ticker,
                                                       LocalDate start,
                                                       LocalDate end) {
        return repo.findByTickerAndDateBetweenOrderByDateAsc(ticker, start, end);
    }

    @Override
    public List<String> getAllTickers() {
        return mongo.query(Price.class)
                .distinct("ticker")
                .as(String.class)
                .all();
    }
}
