package ru.gitant;

import lombok.extern.slf4j.Slf4j;
import ru.gitant.configuration.Config;
import ru.gitant.singleton.AccountBalance;
import ru.gitant.singleton.BinanceInfo;

/**
 * Hello world!
 */
@Slf4j
public class BinanceTraderTestApp {
    public static void main(String[] args) {
        Config config = new Config();
        BinanceInfo binanceInfo = BinanceInfo.getBinanceInfo();
        AccountBalance accountBalance = AccountBalance.getAccountBalance();
        log.info("There are {} coins", BinanceInfo.getExchangeInformation().getSymbols().size());
        log.info("Balance: {}", AccountBalance.getBalanceUsdt());
    }
}
