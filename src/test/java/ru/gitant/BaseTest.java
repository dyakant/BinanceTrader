package ru.gitant;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import ru.gitant.configuration.Config;

/**
 * Created by Anton Dyakov on 22.07.2023
 */
@Slf4j
public abstract class BaseTest {

    @BeforeAll
    public static void init() {
        Config.initiateApp();
    }
}
