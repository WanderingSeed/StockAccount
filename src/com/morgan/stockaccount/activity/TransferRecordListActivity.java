package com.morgan.stockaccount.activity;

import java.util.ArrayList;
import java.util.List;

import com.morgan.stockaccount.R;
import com.morgan.stockaccount.adapter.TransferRecordAdapter;
import com.morgan.stockaccount.app.App;
import com.morgan.stockaccount.data.DataBaseManager;
import com.morgan.stockaccount.model.TransferRecord;
import com.morgan.stockaccount.model.TransferType;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 转账记录列表，可传入一个整形参数{@link #TRANSFER_RECORD_LIST_TYPE}
 * 
 * @author Morgan.Ji
 * @version 1.0
 * @date 2015年8月8日
 */
public class TransferRecordListActivity extends Activity {

    /**
     * 列表显示类型,其中{@link #TRANSFER_RECORD_LIST_TYPE_ALL}为全部，{@link #TRANSFER_RECORD_LIST_TYPE_IN}为转入，
     * {@link #TRANSFER_RECORD_LIST_TYPE_OUT}为转出
     */
    public static final String TRANSFER_RECORD_LIST_TYPE = "transfer_record_list_type";
    /**
     * 当前列表显示的是全部转账记录
     */
    public static final int TRANSFER_RECORD_LIST_TYPE_ALL = 0x00;
    /**
     * 当前列表显示的是全部转入记录
     */
    public static final int TRANSFER_RECORD_LIST_TYPE_IN = 0x01;
    /**
     * 当前列表显示的是全部转出记录
     */
    public static final int TRANSFER_RECORD_LIST_TYPE_OUT = 0x02;
    /**
     * 添加转入记录的请求代码
     */
    private static final int ADD_TRANSFER_RECORD_REQUEST_CODE = 0x00;
    private int mTransferRecordListType = 0;// 此类型和TransferType不同
    private TextView mTotalTransferTxt;
    private TextView mTotalTransferTypeTxt;
    private ListView mTransferrRecordListView;
    private List<TransferRecord> mTransferRecords = new ArrayList<TransferRecord>();
    private TransferRecordAdapter mTransferRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_record_list);
        setTitle(R.string.transfer_record_list);
        if (null != getIntent()) {
            mTransferRecordListType = getIntent().getIntExtra(TRANSFER_RECORD_LIST_TYPE, 0);
        }
        initView();
    }

    private void initView() {
        mTransferrRecordListView = (ListView) findViewById(R.id.transfer_record_listView);
        mTotalTransferTxt = (TextView) findViewById(R.id.txt_total_transfer);
        mTotalTransferTypeTxt = (TextView) findViewById(R.id.txt_total_transfer_record_type);
        // TODO: 使用异步线程
        loadTransferRecordList();
        mTransferRecordAdapter = new TransferRecordAdapter(TransferRecordListActivity.this, mTransferRecords);
        mTransferrRecordListView.setAdapter(mTransferRecordAdapter);
        updateTransferTotalView();
    }

    /**
     * 加载转账记录列表
     */
    private void loadTransferRecordList() {
        mTransferRecords.clear();
        switch (mTransferRecordListType) {
        case TRANSFER_RECORD_LIST_TYPE_ALL:
            mTransferRecords.addAll(DataBaseManager.getInstance().getTransferRecordList());
            break;
        case TRANSFER_RECORD_LIST_TYPE_IN:
            mTransferRecords.addAll(DataBaseManager.getInstance().getTransferInRecordList());
            break;
        case TRANSFER_RECORD_LIST_TYPE_OUT:
            mTransferRecords.addAll(DataBaseManager.getInstance().getTransferOutRecordList());
            break;
        default:
            break;
        }
    }

    /**
     * 更新总转账记录
     */
    public void updateTransferTotalView() {
        switch (mTransferRecordListType) {
        case TRANSFER_RECORD_LIST_TYPE_ALL:
            mTotalTransferTypeTxt.setText(R.string.total_investment);
            mTotalTransferTxt.setText(String.valueOf(App.StockDataManager.getTotalInvestment()));
            break;
        case TRANSFER_RECORD_LIST_TYPE_IN:
            mTotalTransferTypeTxt.setText(R.string.total_transfer_in);
            mTotalTransferTxt.setText(String.valueOf(App.StockDataManager.getTotalTransferIn()));
            break;
        case TRANSFER_RECORD_LIST_TYPE_OUT:
            mTotalTransferTypeTxt.setText(R.string.total_transfer_out);
            mTotalTransferTxt.setText(String.valueOf(App.StockDataManager.getTotalTransferOut()));
            break;
        default:
            break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.transfer_record_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(TransferRecordListActivity.this, AddTransferRecordActivity.class);
            switch (mTransferRecordListType) {
            case TRANSFER_RECORD_LIST_TYPE_ALL:
                break;
            case TRANSFER_RECORD_LIST_TYPE_IN:
                intent.putExtra(AddTransferRecordActivity.TRANSFER_TYPE, TransferType.IN.getValue());
                break;
            case TRANSFER_RECORD_LIST_TYPE_OUT:
                intent.putExtra(AddTransferRecordActivity.TRANSFER_TYPE, TransferType.OUT.getValue());
                break;
            default:
                break;
            }
            startActivityForResult(intent, ADD_TRANSFER_RECORD_REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
            case ADD_TRANSFER_RECORD_REQUEST_CODE:
                loadTransferRecordList();
                mTransferRecordAdapter.notifyDataSetChanged();
                updateTransferTotalView();
                break;
            default:
                break;
            }
        }
    }
}
