package com.morgan.stockaccount.model;

/**
 * 股票交易记录类
 * 
 * @author Morgan.Ji
 * @version 1.0
 * @date 2015年7月29日
 */
public class StockDealRecord {

    private String id;
    private DealType type;
    private String time;
    private String stockName;// 名称并不保存到数据库内，只是为了显示方便
    private String stockCode;
    private int number;
    private float value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setType(DealType type) {
        this.type = type;
    }

    public DealType getType() {
        return type;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

}
