package com.morgan.stockaccount.data;

/**
 * 用于存储一些全局的股票相关的数据
 * 
 * @author Morgan.Ji
 * @version 1.0
 * @date 2015年7月23日
 */
public class StockData {
    private double mInvestment = 0.0; // 总投资
    private double mTotalTransferredIn = 0.0; // 总转入
    private double mTotalTransferredOut = 0.0; // 总转出
    private double mTotalAssets = 0.0; // 总资产
    private double mTotalStockValues = 0.0; // 总持仓
    private double mTotalRevenue = 0.0; // 总收益

    public double getTotalTransferredIn() {
        return mTotalTransferredIn;
    }

    public void setTotalTransferredIn(double totalTransferredIn) {
        this.mTotalTransferredIn = totalTransferredIn;
    }

    public double getTotalTransferredOut() {
        return mTotalTransferredOut;
    }

    public void setTotalTransferredOut(double totalTransferredOut) {
        this.mTotalTransferredOut = totalTransferredOut;
    }

    public double getInvestment() {
        return mInvestment;
    }

    public double getTotalAssets() {
        return mTotalAssets;
    }

    public void setTotalAssets(double totalAssets) {
        this.mTotalAssets = totalAssets;
    }

    public double getTotalStockValues() {
        return mTotalStockValues;
    }

    public void setTotalStockValues(double totalStockValues) {
        this.mTotalStockValues = totalStockValues;
    }

    public double getTotalRevenue() {
        return mTotalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.mTotalRevenue = totalRevenue;
    }
}
