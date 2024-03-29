package ru.gitant.singleton;

import com.binance.client.SyncRequestClient;
import com.binance.client.model.trade.AccountInformation;
import com.binance.client.model.trade.Asset;
import com.binance.client.model.trade.Position;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AccountBalance {
    private final SyncRequestClient syncRequestClient = RequestClient.getRequestClient().getSyncRequestClient();
    private ConcurrentHashMap<String, Asset> assets;
    private ConcurrentHashMap<String, Position> positions;
    private final ReadWriteLock assetsLock = new ReentrantReadWriteLock();
    private final ReadWriteLock positionsLock = new ReentrantReadWriteLock();

    private static class AccountBalanceHolder {
        private static final AccountBalance accountBalance = new AccountBalance();
    }

    private AccountBalance() {
        assets = new ConcurrentHashMap<>();
        positions = new ConcurrentHashMap<>();
        AccountInformation accountInformation = syncRequestClient.getAccountInformation();
        for (Position position : accountInformation.getPositions())
            positions.put(position.getSymbol(), position);
        for (Asset asset : accountInformation.getAssets())
            assets.put(asset.getAsset(), asset);
    }

    public static AccountBalance getAccountBalance() {
        return AccountBalanceHolder.accountBalance;
    }

    public static BigDecimal getBalanceUsdt() {
        return AccountBalanceHolder.accountBalance.getCoinBalance("usdt");
    }

    public BigDecimal getCoinBalance(String symbol) {
        assetsLock.readLock().lock();
        if (assets.containsKey(symbol.toUpperCase())) {
            BigDecimal coinBalance = assets.get(symbol.toUpperCase()).getWalletBalance();
            assetsLock.readLock().unlock();
            return coinBalance;
        }
        assetsLock.readLock().unlock();
        return null;
    }

    public Position getPosition(String symbol) {
        positionsLock.readLock().lock();
        if (positions.containsKey(symbol)) {
            Position position = positions.get(symbol);
            positionsLock.readLock().unlock();
            return position;
        }
        positionsLock.readLock().unlock();
        return null;
    }

    public void updateBalance() {
        AccountInformation accountInformation = syncRequestClient.getAccountInformation();

        ConcurrentHashMap<String, Position> newPositions = new ConcurrentHashMap<>();
        for (Position position : accountInformation.getPositions())
            newPositions.put(position.getSymbol().toLowerCase(), position);
        positionsLock.writeLock().lock();
        positions = newPositions;
        positionsLock.writeLock().unlock();

        ConcurrentHashMap<String, Asset> newAssets = new ConcurrentHashMap<>();
        for (Asset asset : accountInformation.getAssets())
            newAssets.put(asset.getAsset().toLowerCase(), asset);
        assetsLock.writeLock().lock();
        assets = newAssets;
        assetsLock.writeLock().unlock();
    }

    public List<Position> getOpenPositions() {
        List<Position> openPositions = new ArrayList<>();
        positionsLock.readLock().lock();
        Set<String> keys = positions.keySet();
        for (String key : keys) {
            Position position = positions.get(key);
            if (position.getPositionAmt().compareTo(BigDecimal.valueOf(0.0)) > 0) {
                openPositions.add(position);
            }
        }
        positionsLock.readLock().unlock();
        return openPositions;
    }
}
