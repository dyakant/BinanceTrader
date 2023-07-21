package ru.gitant.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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

    public Config() {
        printIntroLogo();
        setPropValues();
    }

    private void setPropValues() {
        Dotenv dotenv = Dotenv.load();
        dotenv.get("MY_ENV_VAR1");
        API_KEY = dotenv.get("API_KEY");
        SECRET_KEY = dotenv.get("SECRET_KEY");
        TELEGRAM_API_TOKEN = dotenv.get("TELEGRAM_API_TOKEN");
        TELEGRAM_CHAT_ID = dotenv.get("TELEGRAM_CHAT_ID");
    }

    private void printIntroLogo() {
        String LOGO_FILENAME = "logo.txt";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(LOGO_FILENAME)) {
            assert inputStream != null;
            String string = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            log.info("\n{}", string);
        } catch (IOException e) {
            log.debug("Exception during loading logo file", e);
        }
    }
}