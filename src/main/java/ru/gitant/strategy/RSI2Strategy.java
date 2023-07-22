/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2017-2023 Ta4j Organization & respective
 * authors (see AUTHORS)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package ru.gitant.strategy;

import com.binance.client.model.enums.CandlestickInterval;
import lombok.extern.slf4j.Slf4j;
import org.ta4j.core.*;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;
import org.ta4j.core.rules.OverIndicatorRule;
import org.ta4j.core.rules.UnderIndicatorRule;
import ru.gitant.loader.BinanceBarSeriesLoader;

/**
 * 2-Period RSI Strategy
 *
 * @see <a href=
 * "http://stockcharts.com/school/doku.php?id=chart_school:trading_strategies:rsi2">
 * http://stockcharts.com/school/doku.php?id=chart_school:trading_strategies:rsi2</a>
 */
@Slf4j
public class RSI2Strategy extends CustomAbstractStrategy {
    private final BaseBarSeries series;
    private final Strategy strategy;

    public RSI2Strategy(String symbol, CandlestickInterval interval) {
        super(symbol, interval);
        series = BinanceBarSeriesLoader.getBaseBarSeries(symbol, interval);
        strategy = buildStrategy(series);
        log.info("Stategy {} built", strategy.getName());
//        log.info("Series(name={}, count={})", series.getName(), series.getBarCount());
    }

    /**
     * @param series a bar series
     * @return a 2-period RSI strategy
     */
    private Strategy buildStrategy(BarSeries series) {
        if (series == null) {
            throw new IllegalArgumentException("Series cannot be null");
        }

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        SMAIndicator shortSma = new SMAIndicator(closePrice, 5);
        SMAIndicator longSma = new SMAIndicator(closePrice, 200);

        // We use a 2-period RSI indicator to identify buying
        // or selling opportunities within the bigger trend.
        RSIIndicator rsi = new RSIIndicator(closePrice, 2);

        // Entry rule
        // The long-term trend is up when a security is above its 200-period SMA.
        Rule entryRule = new OverIndicatorRule(shortSma, longSma) // Trend
                .and(new CrossedDownIndicatorRule(rsi, 5)) // Signal 1
                .and(new OverIndicatorRule(shortSma, closePrice)); // Signal 2

        // Exit rule
        // The long-term trend is down when a security is below its 200-period SMA.
        Rule exitRule = new UnderIndicatorRule(shortSma, longSma) // Trend
                .and(new CrossedUpIndicatorRule(rsi, 95)) // Signal 1
                .and(new UnderIndicatorRule(shortSma, closePrice)); // Signal 2

        // TODO: Finalize the strategy

        return new BaseStrategy(this.toString(), entryRule, exitRule);
    }

    @Override
    public void run() {
        BarSeriesManager seriesManager = new BarSeriesManager(series);
        TradingRecord tradingRecord = seriesManager.run(strategy);
        log.info("Number of positions for the strategy: " + tradingRecord.getPositionCount());
//        log.info("Total return for the strategy: " + new ReturnCriterion().calculate(series, tradingRecord));
    }

}
