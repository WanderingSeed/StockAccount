package com.morgan.stockaccount.model;

/**
 * 股票类
 * 
 * @author Morgan.Ji
 * @version 1.0
 * @date 2015年7月29日
 */
public class Stock {

    private String name;
    private String code;
    private float value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

}
