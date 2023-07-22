package ru.gitant.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import ru.gitant.singleton.AccountBalance;
import ru.gitant.singleton.BinanceInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Main configuration class to get necessary variables
 * and define other constants.
 * Also prints a starting logo
 * <p>
 * Created by Anton Dyakov on 22.07.2023
 */
@Slf4j
public class Config {
    public static String API_KEY = "<Your binance api key>";
    public static String SECRET_KEY = "<Your binance secret key>";
    public static String TELEGRAM_API_TOKEN = "<Your telegram bot api token>";
    public static String TELEGRAM_CHAT_ID = "<Your telegram group chat id>";

    public static void initiateApp() {
        printIntroLogo();
        setCredValues();
        setPropValues();
        BinanceInfo binanceInfo = BinanceInfo.getBinanceInfo();
        AccountBalance accountBalance = AccountBalance.getAccountBalance();
        log.info("There are {} coins", BinanceInfo.getExchangeInformation().getSymbols().size());
        log.info("Balance: {}", AccountBalance.getBalanceUsdt());
    }

    private static void setCredValues() {
        Dotenv dotenv = Dotenv.load();
        dotenv.get("MY_ENV_VAR1");
        API_KEY = dotenv.get("API_KEY");
        SECRET_KEY = dotenv.get("SECRET_KEY");
        TELEGRAM_API_TOKEN = dotenv.get("TELEGRAM_API_TOKEN");
        TELEGRAM_CHAT_ID = dotenv.get("TELEGRAM_CHAT_ID");
    }

    private static void setPropValues() {
        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("trading.properties")) {
            Properties prop = new Properties();
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file 'Config.properties' not found in the classpath");
            }
//            TradeProperties.THREAD_NUM = Integer.parseInt(prop.getProperty("THREAD_NUM"));
            TradeProperties.CANDLE_NUM = Integer.parseInt(prop.getProperty("CANDLE_NUM"));
//            COMMAND = prop.getProperty("COMMAND");
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    private static void printIntroLogo() {
        String LOGO_FILENAME = "logo.txt";
        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(LOGO_FILENAME)) {
            assert inputStream != null;
            String string = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            log.info("\n{}", string);
        } catch (IOException e) {
            log.debug("Exception during loading logo file", e);
        }
    }
}