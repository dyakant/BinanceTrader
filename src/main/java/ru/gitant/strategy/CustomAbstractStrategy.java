package ru.gitant.strategy;

import com.binance.client.model.enums.CandlestickInterval;
import ru.gitant.exception.BinanceTraderException;
import ru.gitant.singleton.BinanceInfo;

/**
 * Created by Anton Dyakov on 22.07.2023
 */
public abstract class CustomAbstractStrategy implements CustomStrategy {
    protected final String symbol;
    protected final CandlestickInterval interval;

    public CustomAbstractStrategy(String symbol, CandlestickInterval interval) {
        if (BinanceInfo.symbolDoesNotExist(symbol)) {
            throw new BinanceTraderException("Symbol " + symbol + " is not correct");
        }
        this.symbol = symbol;
        this.interval = interval;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + symbol + '/' + interval + ')';
    }
}
