package ru.gitant.exception;

/**
 * Created by Anton Dyakov on 23.07.2023
 */
public class BinanceTraderException extends RuntimeException {
    public BinanceTraderException() {
        super();
    }

    public BinanceTraderException(String message) {
        super(message);
    }
}
