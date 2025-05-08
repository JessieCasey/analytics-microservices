package edu.analytics.prices.repository;


import edu.analytics.prices.model.Price;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public interface PriceRepository extends MongoRepository<Price, String> {
    List<Price> findPriceByTicker(String ticker);

    List<Price> findByTickerOrderByDateDesc(String ticker);

    List<Price> findByTickerAndDateBetweenOrderByDateAsc(String ticker, LocalDate start, LocalDate end);
}
