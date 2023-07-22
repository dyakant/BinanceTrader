package ru.gitant.utils;

import com.binance.client.model.enums.CandlestickInterval;
import ru.gitant.exception.BinanceTraderException;

/**
 * Created by Anton Dyakov on 23.07.2023
 */
public class CandlestickIntervalUtils {
    /**
     * Get {@link CandlestickInterval} from string value
     *
     * @param interval string value
     * @return correspond {@link CandlestickInterval} if exists, otherwise return an exception
     */
    public static CandlestickInterval getCandlestickInterval(String interval) {
        for (CandlestickInterval candlestickInterval : CandlestickInterval.values()) {
            if (candlestickInterval.toString().equals(interval)) {
                return candlestickInterval;
            }
        }
        throw new BinanceTraderException("Interval " + interval + " is not correct");
    }
}
