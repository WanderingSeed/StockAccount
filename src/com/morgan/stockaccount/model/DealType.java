package com.morgan.stockaccount.model;

/**
 * 交易类型
 * 
 * @author Morgan.Ji
 * @version 1.0
 * @date 2015年7月29日
 */
public enum DealType {
    BUY(0), SELL(1);

    int value;

    private DealType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DealType valueOf(int value) {
        switch (value) {
        case 0:
            return BUY;
        case 1:
            return SELL;
        default:
            break;
        }
        return BUY;
    }
}
