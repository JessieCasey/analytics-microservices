package edu.analytics.prices.service;

import edu.analytics.prices.model.Price;
import edu.analytics.prices.model.IndicatorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.ATRIndicator;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Ta4jHeavyService {

    private final PriceService priceService;

    public Map<String, IndicatorResult> computeForTickers(
            List<String> tickers, LocalDate from, LocalDate to
    ) {
        Map<String, IndicatorResult> results = new HashMap<>();

        for (String ticker : tickers) {
            List<Price> prices = priceService.getEntitiesByTickerAndDateRange(ticker, from, to);
            if (prices.size() < 30) continue;

            BarSeries series = new BaseBarSeriesBuilder()
                    .withName(ticker)
                    .build();

            for (Price p : prices) {
                try {
                    ZonedDateTime endTime = p.getDate()
                            .atStartOfDay(ZoneId.systemDefault());
                    Bar bar = BaseBar.builder()
                            .timePeriod(Duration.ofDays(1))
                            .endTime(endTime)
                            .openPrice( DecimalNum.valueOf(p.getOpen()) )
                            .highPrice( DecimalNum.valueOf(p.getHigh()) )
                            .lowPrice(  DecimalNum.valueOf(p.getLow()) )
                            .closePrice(DecimalNum.valueOf(p.getAdj_close()) )
                            .build();
                    series.addBar(bar);
                } catch (Exception _) {
                }
            }

            ClosePriceIndicator close = new ClosePriceIndicator(series);
            ATRIndicator atr = new ATRIndicator(series, 14);
            RSIIndicator rsi = new RSIIndicator(close, 14);
            BollingerBandsMiddleIndicator bbm =
                    new BollingerBandsMiddleIndicator(close);
            StandardDeviationIndicator stdDev =
                    new StandardDeviationIndicator(close, 20);
            Num k = DecimalNum.valueOf(2);

            BollingerBandsUpperIndicator bbu =
                    new BollingerBandsUpperIndicator(bbm, stdDev, k);
            BollingerBandsLowerIndicator bbl =
                    new BollingerBandsLowerIndicator(bbm, stdDev, k);


            // compute last values
            double lastAtr = 0, lastRsi = 0, lastBbu = 0, lastBbl = 0;
            for (int i = 0; i < series.getBarCount(); i++) {
                lastAtr = atr.getValue(i).doubleValue();
                lastRsi = rsi.getValue(i).doubleValue();
                lastBbu = bbu.getValue(i).doubleValue();
                lastBbl = bbl.getValue(i).doubleValue();
            }

            results.put(ticker, new IndicatorResult(
                    ticker, lastAtr, lastRsi, lastBbu, lastBbl
            ));
        }
        return results;
    }
}
