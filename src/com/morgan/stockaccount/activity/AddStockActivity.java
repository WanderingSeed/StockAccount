package com.morgan.stockaccount.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.morgan.stockaccount.R;
import com.morgan.stockaccount.data.DataBaseManager;
import com.morgan.stockaccount.model.Stock;

/**
 * 添加新的股票
 * 
 * @author Morgan.Ji
 * @version 1.0
 * @date 2015年8月30日
 */
public class AddStockActivity extends Activity {

    private EditText mStockNameEditText;
    private EditText mStockCodeEditText;
    private Button mSubmitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);
        setTitle(R.string.add_stock);
        initView();
    }

    private void initView() {
        mStockNameEditText = (EditText) findViewById(R.id.stock_name_editText);
        mStockCodeEditText = (EditText) findViewById(R.id.stock_code_editText);
        mSubmitBtn = (Button) findViewById(R.id.btn_submit);
        mSubmitBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String stockName = mStockNameEditText.getText().toString().trim();
                if (TextUtils.isEmpty(stockName)) {
                    Toast.makeText(AddStockActivity.this, R.string.msg_stock_name_can_not_be_empty, Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                String stockCode = mStockCodeEditText.getText().toString().trim();
                if (TextUtils.isEmpty(stockCode)) {
                    Toast.makeText(AddStockActivity.this, R.string.msg_stock_code_can_not_be_empty, Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                // 插入一条数据到数据库，由于所需时间较短，暂时不用异步线程
                Stock stock = new Stock();
                stock.setName(stockName);
                stock.setCode(stockCode);
                if (DataBaseManager.getInstance().addStock(stock)) {
                    Toast.makeText(AddStockActivity.this, R.string.msg_add_success, Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(AddStockActivity.this, R.string.msg_add_fail, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
