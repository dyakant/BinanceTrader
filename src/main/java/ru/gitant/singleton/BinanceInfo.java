package ru.gitant.singleton;

import com.binance.client.SyncRequestClient;
import com.binance.client.model.market.ExchangeInfoEntry;
import com.binance.client.model.market.ExchangeInformation;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
public class BinanceInfo {
    SyncRequestClient syncRequestClient = RequestClient.getRequestClient().getSyncRequestClient();
    private static ExchangeInformation exchangeInformation;
    private static Map<String, ExchangeInfoEntry> symbolInformation;

    private static class BinanceInfoHolder {
        private static final BinanceInfo binanceInfo = new BinanceInfo();
    }

    private BinanceInfo() {
        exchangeInformation = syncRequestClient.getExchangeInformation();
        symbolInformation = new HashMap<>();
        for (ExchangeInfoEntry exchangeInfoEntry : exchangeInformation.getSymbols()) {
            symbolInformation.put(exchangeInfoEntry.getSymbol(), exchangeInfoEntry);
        }
    }

    public static BinanceInfo getBinanceInfo() {
        return BinanceInfoHolder.binanceInfo;
    }

    public static ExchangeInformation getExchangeInformation() {
        return exchangeInformation;
    }

    /**
     * @param symbol - the symbol of coin.
     * @return the ExchangeInfoEntry of symbol.
     */
    public static ExchangeInfoEntry getSymbolInformation(String symbol) {
        return symbolInformation.get(symbol.toUpperCase());
    }

    public static boolean symbolDoesNotExist(String symbol) {
        return !symbolInformation.containsKey(symbol.toUpperCase());
    }

    public static String formatQty(double buyingQty, String symbol) {
        String formatter = "%." + getSymbolInformation(symbol).getQuantityPrecision().toString() + "f";
        return String.format(Locale.US, formatter, Math.abs(buyingQty));
    }

    public static String formatPrice(double price, String symbol) {
        return String.format("%." + getSymbolInformation(symbol).getPricePrecision().toString() + "f", price);
    }

}
