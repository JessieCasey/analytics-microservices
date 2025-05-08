package edu.analytics.prices.api.v1;

import edu.analytics.prices.model.IndicatorResult;
import edu.analytics.prices.service.Ta4jHeavyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/prices")
public class GarchController {

    private final Ta4jHeavyService ta4jHeavyService;

    @GetMapping("/indicators")
    public ResponseEntity<List<IndicatorResult>> run(
            @RequestParam List<String> tickers,
            @RequestParam String start,
            @RequestParam String end
    ) {
        LocalDate from = LocalDate.parse(start);
        LocalDate to   = LocalDate.parse(end);
        Map<String, IndicatorResult> map = ta4jHeavyService.computeForTickers(tickers, from, to);
        if (map.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(new ArrayList<>(map.values()));
    }
}
