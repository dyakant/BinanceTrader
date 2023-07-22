package ru.gitant.loader;

import com.binance.client.SyncRequestClient;
import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.market.Candlestick;
import org.ta4j.core.BaseBarSeries;
import ru.gitant.singleton.RequestClient;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

import static ru.gitant.configuration.TradeProperties.CANDLE_NUM;
import static ru.gitant.singleton.BinanceInfo.symbolDoesNotExist;
import static ru.gitant.utils.TimeUtils.getZonedDateTime;

/**
 * Created by Anton Dyakov on 22.07.2023
 */
public class BinanceBarSeriesLoader {
    private static final SyncRequestClient syncRequestClient = RequestClient.getRequestClient().getSyncRequestClient();

    public static BaseBarSeries getBaseBarSeries(String symbol, CandlestickInterval interval) {
        if (symbolDoesNotExist(symbol)) return null;

        List<Candlestick> candlestickBars = syncRequestClient.getCandlestick(symbol, interval, null, null, CANDLE_NUM);
        BaseBarSeries baseBarSeries = new BaseBarSeries(symbol + '/' + interval);

        for (Candlestick candlestickBar : candlestickBars) {
            ZonedDateTime closeTime = getZonedDateTime(candlestickBar.getCloseTime());
            Duration candleDuration = Duration.ofMillis(candlestickBar.getCloseTime() - candlestickBar.getOpenTime());
            double open = candlestickBar.getOpen().doubleValue();
            double high = candlestickBar.getHigh().doubleValue();
            double low = candlestickBar.getLow().doubleValue();
            double close = candlestickBar.getClose().doubleValue();
            double volume = candlestickBar.getVolume().doubleValue();
            baseBarSeries.addBar(candleDuration, closeTime, open, high, low, close, volume);
        }
        return baseBarSeries;
    }
}
