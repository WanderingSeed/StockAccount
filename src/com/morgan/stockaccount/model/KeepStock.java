package com.morgan.stockaccount.model;

public class KeepStock {

    private String stockName;// 股票名称
    private String stockCode;// 股票代码
    private int stockNumber;// 股票数量
    private float stockValue;// 股票价格，当股票数量为0时此值保存该股总收益

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public int getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(int stockNumber) {
        this.stockNumber = stockNumber;
    }

    /**
     * 股票价格，当股票数量为0时此值返回该股总收益
     * 
     * @return
     */
    public float getStockValue() {
        return stockValue;
    }

    public void setStockValue(float stockValue) {
        this.stockValue = stockValue;
    }

}
