package com.morgan.stockaccount.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.morgan.stockaccount.R;
import com.morgan.stockaccount.app.App;
import com.morgan.stockaccount.data.DataBaseManager;

/**
 * 程序主界面
 * 
 * @author Morgan.Ji
 * @version 1.0
 * @date 2015年7月23日
 */
public class MainActivity extends Activity {

    private Button mTransferOutBtn;
    private Button mTransferInBtn;
    private Button mComputeBtn;
    private Button mStockValueBtn;
    private TextView mTotalInvestmentTxt;
    private TextView mTotalStockTxt;
    private TextView mTotalRevenueTxt;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mTotalInvestmentTxt = (TextView) findViewById(R.id.txt_total_investment);
        mTotalStockTxt = (TextView) findViewById(R.id.txt_total_stock_value);
        mTotalRevenueTxt = (TextView) findViewById(R.id.txt_total_revenue);
        mTransferInBtn = (Button) findViewById(R.id.btn_transfer_in);
        mTransferOutBtn = (Button) findViewById(R.id.btn_transfer_out);
        mComputeBtn = (Button) findViewById(R.id.btn_compute);
        mStockValueBtn = (Button) findViewById(R.id.btn_stock_value);

        mTransferInBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TransferRecordListActivity.class);
                intent.putExtra(TransferRecordListActivity.TRANSFER_RECORD_LIST_TYPE,
                        TransferRecordListActivity.TRANSFER_RECORD_LIST_TYPE_IN);
                startActivity(intent);
            }
        });
        mTransferOutBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TransferRecordListActivity.class);
                intent.putExtra(TransferRecordListActivity.TRANSFER_RECORD_LIST_TYPE,
                        TransferRecordListActivity.TRANSFER_RECORD_LIST_TYPE_OUT);
                startActivity(intent);
            }
        });
        mStockValueBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, KeepStockListActivity.class);
                startActivity(intent);
            }
        });
        mComputeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ComputeActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHeaderView();
    }

    /**
     * 更新主界面数据
     */
    private void updateHeaderView() {
        mTotalInvestmentTxt.setText(String.valueOf(App.StockDataManager.getTotalInvestment()));
        mTotalStockTxt.setText(String.valueOf(App.StockDataManager.getTotalStockValues()));
        mTotalRevenueTxt.setText(String.valueOf(App.StockDataManager.getTotalRevenue()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_stock:
            startActivity(new Intent(MainActivity.this, StockListActivity.class));
            return true;
        case R.id.action_export:
            mProgressDialog.setMessage(getString(R.string.msg_data_export_now));
            mProgressDialog.show();
            ExportTask exportTask = new ExportTask();
            exportTask.execute();
            break;
        case R.id.action_import:
            mProgressDialog.setMessage(getString(R.string.msg_data_import_now));
            mProgressDialog.show();
            ImportTask importTask = new ImportTask();
            importTask.execute();
            break;

        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 数据库数据导出到SD上
     * 
     * @author Morgan.Ji
     * @version 1.0
     * @date 2015年8月30日
     */
    private class ExportTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(1000);// 防止过快导致进度条闪烁
            } catch (Exception e) {
            }
            return DataBaseManager.getInstance().exportData();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(MainActivity.this, R.string.msg_data_export_success, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, R.string.msg_data_export_fail, Toast.LENGTH_SHORT).show();
            }
            mProgressDialog.hide();
            super.onPostExecute(result);
        }

    }

    /**
     * SD卡上的数据导入到数据库
     * 
     * @author Morgan.Ji
     * @version 1.0
     * @date 2015年8月30日
     */
    private class ImportTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(1000);// 防止过快导致进度条闪烁
            } catch (Exception e) {
            }
            return DataBaseManager.getInstance().importData();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgressDialog.hide();
            if (result) {
                updateHeaderView();
                Toast.makeText(MainActivity.this, R.string.msg_data_import_success, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, R.string.msg_data_import_fail, Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }
}
