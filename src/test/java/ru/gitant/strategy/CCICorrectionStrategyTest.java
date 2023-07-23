package ru.gitant.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ru.gitant.BaseTest;
import ru.gitant.strategy.common.CustomStrategy;

import static ru.gitant.utils.CandlestickIntervalUtils.getCandlestickInterval;

/**
 * Created by Anton Dyakov on 22.07.2023
 */
@Slf4j
public class CCICorrectionStrategyTest extends BaseTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/strategy/symbol_interval.csv", numLinesToSkip = 1)
    public void testStrategy(String symbol, String interval) {
        CustomStrategy strategy = new CCICorrectionStrategy(symbol, getCandlestickInterval(interval));
        strategy.run();
    }
}