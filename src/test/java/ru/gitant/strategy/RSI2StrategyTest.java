package ru.gitant.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ru.gitant.BaseTest;

import static com.binance.client.model.enums.CandlestickInterval.THREE_MINUTES;
import static ru.gitant.utils.CandlestickIntervalUtils.getCandlestickInterval;

/**
 * Created by Anton Dyakov on 22.07.2023
 */
@Slf4j
public class RSI2StrategyTest extends BaseTest {

    @Test
    public void test_DashUsdt3m() {
        log.info("Start test_DashUsdt3m");
        CustomStrategy strategy = new RSI2Strategy("dashusdt", THREE_MINUTES);
        strategy.run();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/strategy/rsi2strategytest.csv", numLinesToSkip = 1)
    public void testStrategy(String symbol, String interval) {
        CustomStrategy strategy = new RSI2Strategy(symbol, getCandlestickInterval(interval));
        strategy.run();
    }
}