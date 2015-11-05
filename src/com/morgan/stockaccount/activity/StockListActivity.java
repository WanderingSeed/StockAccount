package com.morgan.stockaccount.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.morgan.stockaccount.R;
import com.morgan.stockaccount.adapter.StockAdapter;
import com.morgan.stockaccount.data.DataBaseManager;
import com.morgan.stockaccount.model.Stock;

/**
 * 当前关注的持股票列表
 * 
 * @author Morgan.Ji
 * @version 1.0
 * @date 2015年8月30日
 */
public class StockListActivity extends Activity {

    /**
     * 添加关注股票的请求代码
     */
    private static final int ADD_STOCK_REQUEST_CODE = 0x00;

    private ListView mStockListView;
    private List<Stock> mStockList = new ArrayList<Stock>();
    private StockAdapter mStockAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);
        setTitle(R.string.stock_list);
        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mStockListView = (ListView) findViewById(R.id.stock_listView);
        mStockAdapter = new StockAdapter(StockListActivity.this, mStockList);
        mStockListView.setAdapter(mStockAdapter);
        loadStockList();
        mStockAdapter.notifyDataSetChanged();
    }

    /**
     * 加载关注股票列表
     */
    private void loadStockList() {
        mStockList.clear();
        mStockList.addAll(DataBaseManager.getInstance().getStockList());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stock_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(StockListActivity.this, AddStockActivity.class);
            startActivityForResult(intent, ADD_STOCK_REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
            case ADD_STOCK_REQUEST_CODE:
                loadStockList();
                mStockAdapter.notifyDataSetChanged();
                break;
            default:
                break;
            }
        }
    }

}
