package edu.analytics.prices.service;


import edu.analytics.prices.dto.PriceDto;
import edu.analytics.prices.mapper.PriceMapper;
import edu.analytics.prices.model.Price;
import edu.analytics.prices.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceRepository repo;
    private final PriceMapper mapper;

    @Override
    public List<Price> getEntitiesByTickerAndDateRange(String ticker,
                                                       LocalDate start,
                                                       LocalDate end) {
        return repo.findByTickerAndDateBetweenOrderByDateAsc(ticker, start, end);
    }

    @Override
    public Page<PriceDto> getLatestPrices(int page, int size) {
        long total = 0;
        int skip = page * size;
        List<Price> hits = repo.findLatestPrices(skip, size);
        List<PriceDto> dtos = hits.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, PageRequest.of(page, size), total);
    }

    @Override
    public List<Price> findAllByTickerOrderByDate(String ticker) {
        return repo.findAllByTickerOrderByDate(ticker);

    }
}
