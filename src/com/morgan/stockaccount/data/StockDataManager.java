package com.morgan.stockaccount.data;

import com.morgan.stockaccount.model.StockDealRecord;
import com.morgan.stockaccount.model.TransferRecord;
import com.morgan.stockaccount.util.PreferenceUtil;

/**
 * 用于存储一些全局的股票相关的数据
 * 
 * @author Morgan.Ji
 * @version 1.0
 * @date 2015年7月23日
 */
public class StockDataManager {

    private static final String KEY_TRANSFER_IN = "key_transfer_in";
    private static final String KEY_TRANSFER_OUT = "key_transfer_out";
    private float mTotalTransferIn = 0.0f; // 总转入
    private float mTotalTransferOut = 0.0f; // 总转出
    private float mTotalStockValues = 0.0f; // 总持仓
    private float mTotalRevenue = 0.0f; // 总收益

    public StockDataManager() {
        mTotalTransferIn = PreferenceUtil.getInstance().getFloat(KEY_TRANSFER_IN, 0);
        mTotalTransferOut = PreferenceUtil.getInstance().getFloat(KEY_TRANSFER_OUT, 0);
        mTotalStockValues = DataBaseManager.getInstance().getTotalKeepStockValue();
        mTotalRevenue = DataBaseManager.getInstance().getTotalRevenue();
    }

    /**
     * 获取所有转入资产
     * 
     * @return
     */
    public float getTotalTransferIn() {
        return mTotalTransferIn;
    }

    /**
     * 设置所有转入资产
     * 
     * @param totalTransferIn
     */
    public void setTotalTransferIn(float totalTransferIn) {
        this.mTotalTransferIn = totalTransferIn;
        PreferenceUtil.getInstance().putFloat(KEY_TRANSFER_IN, totalTransferIn);
    }

    /**
     * 获取所有转出资产
     * 
     * @return
     */
    public float getTotalTransferOut() {
        return mTotalTransferOut;
    }

    /**
     * 设置所有转出资产
     * 
     * @param totalTransferOut
     */
    public void setTotalTransferOut(float totalTransferOut) {
        this.mTotalTransferOut = totalTransferOut;
        PreferenceUtil.getInstance().putFloat(KEY_TRANSFER_OUT, totalTransferOut);
    }

    /**
     * 获取总投资，即转入减转出
     * 
     * @return
     */
    public float getTotalInvestment() {
        return mTotalTransferIn - mTotalTransferOut;
    }

    /**
     * 获取持股价值，此价值为买入价值，暂时不会跟随股价变动
     * 
     * @return
     */
    public float getTotalStockValues() {
        return mTotalStockValues;
    }

    /**
     * 获取总收益
     * 
     * @return
     */
    public double getTotalRevenue() {
        return mTotalRevenue;
    }

    /**
     * 在添加转账记录的时候调用
     * 
     * @param record
     *            新的转账记录
     */
    public void onAddTransferRecord(TransferRecord record) {
        switch (record.getType()) {
        case IN:
            setTotalTransferIn(getTotalTransferIn() + record.getMoney());
            break;
        case OUT:
            setTotalTransferOut(getTotalTransferOut() + record.getMoney());
            break;
        default:
            break;
        }
    }

    /**
     * 在添加股票记录的时候调用
     * 
     * @param record
     *            新的股票交易记录
     */
    public void onAddStockDealRecord(StockDealRecord record) {
        // 有股票交易记录就会更改总持仓
        mTotalStockValues = DataBaseManager.getInstance().getTotalKeepStockValue();
        switch (record.getType()) {
        case SELL:
            // 有股票交易记录就有可能更改总收益，因股票不支持双向交易，故只有卖出才能平仓
            mTotalRevenue = DataBaseManager.getInstance().getTotalRevenue();
            break;
        default:
            break;
        }
    }
}
