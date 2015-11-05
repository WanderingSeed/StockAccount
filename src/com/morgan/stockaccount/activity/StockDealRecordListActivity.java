package com.morgan.stockaccount.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.morgan.stockaccount.R;
import com.morgan.stockaccount.adapter.StockDealRecordAdapter;
import com.morgan.stockaccount.data.Constants;
import com.morgan.stockaccount.data.DataBaseManager;
import com.morgan.stockaccount.model.StockDealRecord;

/**
 * 股票交易记录界面，如果传入单个股票代码{@link Constants#STOCK_CODE}，则列表只显示此股票的交易记录
 * 
 * @author Morgan.Ji
 * @version 1.0
 * @date 2015年8月30日
 */
public class StockDealRecordListActivity extends Activity {

    private String mCurrentStockCode;// 当前显示交易列表的股票代码，如果为null则显示全部的交易列表
    /**
     * 添加股票交易记录的请求代码
     */
    private static final int ADD_STOCK_DEAL_RECORD_REQUEST_CODE = 0x00;
    private ListView mTransferrRecordListView;
    private List<StockDealRecord> mStockDealRecords = new ArrayList<StockDealRecord>();
    private StockDealRecordAdapter mStockDealRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_deal_record_list);
        setTitle(R.string.stock_deal_record_list);
        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mTransferrRecordListView = (ListView) findViewById(R.id.stock_deal_record_listView);
        Intent intent = getIntent();
        if (null != intent && intent.hasExtra(Constants.STOCK_CODE)) {
            mCurrentStockCode = intent.getStringExtra(Constants.STOCK_CODE);
        }
        // TODO: 使用异步线程
        loadStockDealRecordList();
        mStockDealRecordAdapter = new StockDealRecordAdapter(StockDealRecordListActivity.this, mStockDealRecords);
        mTransferrRecordListView.setAdapter(mStockDealRecordAdapter);
    }

    private void loadStockDealRecordList() {
        mStockDealRecords.clear();
        if (TextUtils.isEmpty(mCurrentStockCode)) {
            mStockDealRecords.addAll(DataBaseManager.getInstance().getAllStockDealRecordList());
        } else {
            mStockDealRecords.addAll(DataBaseManager.getInstance().getDealRecordList(mCurrentStockCode));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stock_deal_record_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(StockDealRecordListActivity.this, AddStockDealRecordActivity.class);
            if (!TextUtils.isEmpty(mCurrentStockCode)) {
                intent.putExtra(Constants.STOCK_CODE, mCurrentStockCode);
            }
            startActivityForResult(intent, ADD_STOCK_DEAL_RECORD_REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
            case ADD_STOCK_DEAL_RECORD_REQUEST_CODE:
                loadStockDealRecordList();
                mStockDealRecordAdapter.notifyDataSetChanged();
                setResult(RESULT_OK);// 因为需要修改前一个界面的数值
                break;
            default:
                break;
            }
        }
    }
}
