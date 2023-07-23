package ru.gitant.strategy.common;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Strategy;

/**
 * Created by Anton Dyakov on 22.07.2023
 */
public interface CustomStrategy {
    /**
     * @param series a bar series
     * @return a defined strategy
     */
    Strategy buildStrategy(BarSeries series);

    void run();
}
