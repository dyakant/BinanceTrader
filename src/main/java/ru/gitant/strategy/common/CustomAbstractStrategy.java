package ru.gitant.strategy.common;

import com.binance.client.model.enums.CandlestickInterval;
import lombok.extern.slf4j.Slf4j;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.backtest.BarSeriesManager;
import org.ta4j.core.criteria.pnl.ReturnCriterion;
import ru.gitant.exception.BinanceTraderException;
import ru.gitant.loader.BinanceBarSeriesLoader;
import ru.gitant.singleton.BinanceInfo;

/**
 * Created by Anton Dyakov on 22.07.2023
 */
@Slf4j
public abstract class CustomAbstractStrategy implements CustomStrategy {
    protected final String symbol;
    protected final CandlestickInterval interval;
    private final BaseBarSeries series;
    private final Strategy strategy;

    public CustomAbstractStrategy(String symbol, CandlestickInterval interval) {
        if (BinanceInfo.symbolDoesNotExist(symbol)) {
            throw new BinanceTraderException("Symbol " + symbol + " is not correct");
        }
        this.symbol = symbol;
        this.interval = interval;
        series = BinanceBarSeriesLoader.getBaseBarSeries(symbol, interval);
        strategy = buildStrategy(series);
    }

    @Override
    public void run() {
        BarSeriesManager seriesManager = new BarSeriesManager(series);
        TradingRecord tradingRecord = seriesManager.run(strategy);
        log.info("Number of positions for the strategy {} is {}", strategy.getName(), tradingRecord.getPositionCount());
        log.info("Total return for the strategy: " + new ReturnCriterion().calculate(series, tradingRecord));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + symbol + '/' + interval + ')';
    }
}
