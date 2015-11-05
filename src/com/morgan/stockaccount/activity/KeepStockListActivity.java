package com.morgan.stockaccount.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.morgan.stockaccount.R;
import com.morgan.stockaccount.adapter.KeepStockAdapter;
import com.morgan.stockaccount.app.App;
import com.morgan.stockaccount.data.Constants;
import com.morgan.stockaccount.data.DataBaseManager;
import com.morgan.stockaccount.model.KeepStock;

/**
 * 当前的持股种类、成本、数量
 * 
 * @author Morgan.Ji
 * @version 1.0
 * @date 2015年8月30日
 */
public class KeepStockListActivity extends Activity {

    /**
     * 添加股票交易记录的请求代码
     */
    private static final int ADD_STOCK_DEAL_RECORD_REQUEST_CODE = 0x00;

    private ListView mKeepStockListView;
    private TextView mTotalKeepStockValueTxt;
    private List<KeepStock> mKeepStockList = new ArrayList<KeepStock>();
    private KeepStockAdapter mKeepStockAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keep_stock_list);
        setTitle(R.string.keep_stock_list);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 有可能在各个股票详细记录里面添加了股票交易
        updateTotalKeepStockValueView();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mKeepStockListView = (ListView) findViewById(R.id.keep_stock_listView);
        mTotalKeepStockValueTxt = (TextView) findViewById(R.id.txt_total_keep_stock_value);
        mKeepStockAdapter = new KeepStockAdapter(KeepStockListActivity.this, mKeepStockList);
        mKeepStockListView.setAdapter(mKeepStockAdapter);
        mKeepStockListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(KeepStockListActivity.this, StockDealRecordListActivity.class);
                intent.putExtra(Constants.STOCK_CODE, mKeepStockList.get(position).getStockCode());
                startActivityForResult(intent, ADD_STOCK_DEAL_RECORD_REQUEST_CODE);
            }
        });
        loadKeepStockList();
        mKeepStockAdapter.notifyDataSetChanged();
        updateTotalKeepStockValueView();
    }

    /**
     * 加载持股数据
     */
    private void loadKeepStockList() {
        mKeepStockList.clear();
        mKeepStockList.addAll(DataBaseManager.getInstance().getKeepStockList());
    }

    /**
     * 更新总持仓价值
     */
    private void updateTotalKeepStockValueView() {
        mTotalKeepStockValueTxt.setText(String.valueOf(App.StockDataManager.getTotalStockValues()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.keep_stock_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(KeepStockListActivity.this, AddStockDealRecordActivity.class);
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
                loadKeepStockList();
                mKeepStockAdapter.notifyDataSetChanged();
                updateTotalKeepStockValueView();
                break;
            default:
                break;
            }
        }
    }

}
