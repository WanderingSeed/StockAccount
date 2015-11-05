package com.morgan.stockaccount.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.morgan.stockaccount.R;
import com.morgan.stockaccount.data.Constants;
import com.morgan.stockaccount.data.DataBaseManager;
import com.morgan.stockaccount.model.DealType;
import com.morgan.stockaccount.model.Stock;
import com.morgan.stockaccount.model.StockDealRecord;
import com.morgan.stockaccount.util.DateUtils;

/**
 * 添加新的股票交易记录,如果传入单个股票代码{@link Constants#STOCK_CODE}，则确定只添加此股票的交易记录
 * 
 * @author Morgan.Ji
 * @version 1.0
 * @date 2015年8月30日
 */
public class AddStockDealRecordActivity extends Activity {

    private Spinner mDealTypeSpinner;
    private String[] mDealTypeArray;
    private Spinner mStockSpinner;
    private String[] mStockArray;
    private List<Stock> mStockList = new ArrayList<Stock>();
    private DealType mCurrentType = DealType.BUY;
    private String mCurrentStockCode;// 当前要交易的股票代码
    private TextView mDealDateTxt;
    private TextView mDealTimeTxt;
    private EditText mDealValueEditText;
    private EditText mDealNumberEditText;
    private Button mSubmitBtn;
    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog mTimePickerDialog;
    private OnDateSetListener mDateSetListener;
    private OnTimeSetListener mTimeSetListener;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock_deal_record);
        setTitle(R.string.stock_deal);
        initView();
        if (mStockList.size() > 0) {// 初始化最开始的交易股票代码
            mCurrentStockCode = mStockList.get(0).getCode();
            mStockSpinner.setSelection(0);
        }
        Intent intent = getIntent();
        if (null != intent && intent.hasExtra(Constants.STOCK_CODE)) {
            String code = intent.getStringExtra(Constants.STOCK_CODE);
            for (int i = 0; i < mStockList.size(); i++) {
                if (mStockList.get(i).getCode().equals(code)) {
                    mCurrentStockCode = code;
                    mStockSpinner.setSelection(i);
                    break;
                }
            }
        }
    }

    private void initView() {
        mDealTypeArray = getResources().getStringArray(R.array.deal_type);
        mDealTypeSpinner = (Spinner) findViewById(R.id.deal_type_spinner);
        mStockList = DataBaseManager.getInstance().getStockList();
        mStockArray = new String[mStockList.size()];
        for (int i = 0; i < mStockList.size(); i++) {
            mStockArray[i] = mStockList.get(i).getName();
        }
        mStockSpinner = (Spinner) findViewById(R.id.deal_stock_spinner);
        mDealDateTxt = (TextView) findViewById(R.id.deal_date_textView);
        mDealTimeTxt = (TextView) findViewById(R.id.deal_time_textView);
        mDealValueEditText = (EditText) findViewById(R.id.deal_stock_value_editText);
        mDealNumberEditText = (EditText) findViewById(R.id.deal_stock_number_editText);
        mSubmitBtn = (Button) findViewById(R.id.btn_submit);

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        updateDateView();

        mDealTypeSpinner.setAdapter(new ArrayAdapter<String>(AddStockDealRecordActivity.this,
                android.R.layout.simple_list_item_1, mDealTypeArray));
        mDealTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCurrentType = DealType.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mStockSpinner.setAdapter(new ArrayAdapter<String>(AddStockDealRecordActivity.this,
                android.R.layout.simple_list_item_1, mStockArray));
        mStockSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCurrentStockCode = mStockList.get(position).getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mDateSetListener = new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear + 1;
                mDay = dayOfMonth;
                updateDateView();
            }
        };
        mDealDateTxt.setClickable(true);
        mDealDateTxt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null == mDatePickerDialog) {
                    mDatePickerDialog = new DatePickerDialog(AddStockDealRecordActivity.this, mDateSetListener, mYear,
                            mMonth - 1, mDay);
                }
                mDatePickerDialog.show();
            }
        });
        mTimeSetListener = new OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
                updateDateView();
            }
        };
        mDealTimeTxt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null == mTimePickerDialog) {
                    mTimePickerDialog = new TimePickerDialog(AddStockDealRecordActivity.this, mTimeSetListener, mHour,
                            mMinute, true);
                }
                mTimePickerDialog.show();
            }
        });
        mSubmitBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mCurrentStockCode)) {// 无可交易股票
                    Toast.makeText(AddStockDealRecordActivity.this, R.string.msg_no_deal_stock, Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                String dealValue = mDealValueEditText.getText().toString().trim();
                if (TextUtils.isEmpty(dealValue)) {
                    Toast.makeText(AddStockDealRecordActivity.this, R.string.msg_value_can_not_be_zero,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String dealNumber = mDealNumberEditText.getText().toString().trim();
                if (TextUtils.isEmpty(dealNumber)) {
                    Toast.makeText(AddStockDealRecordActivity.this, R.string.msg_number_can_not_be_zero,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                // 插入一条数据到数据库，由于所需时间较短，暂时不用异步线程
                StockDealRecord record = new StockDealRecord();
                record.setStockName(DataBaseManager.getInstance().getStockName(mCurrentStockCode));
                record.setStockCode(mCurrentStockCode);
                record.setType(mCurrentType);
                record.setId(getRecordId());
                record.setValue(Float.valueOf(dealValue));
                record.setNumber(Integer.valueOf(dealNumber));
                record.setTime(getSelectedDate());
                if (DataBaseManager.getInstance().addStockDealRecord(record)) {
                    Toast.makeText(AddStockDealRecordActivity.this, R.string.msg_add_success, Toast.LENGTH_SHORT)
                            .show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(AddStockDealRecordActivity.this, R.string.msg_add_fail, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 更新日期控件
     */
    private void updateDateView() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(mYear, mMonth - 1, mDay, mHour, mMinute);
        mDealDateTxt.setText(DateUtils.dateToString(calendar.getTime(), DateUtils.DATE_ONLY_FORMAT));
        mDealTimeTxt.setText(DateUtils.dateToString(calendar.getTime(), DateUtils.TIME_ONLY_FORMAT));
    }

    /**
     * 获取选择的时间
     * 
     * @return
     */
    private String getSelectedDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(mYear, mMonth - 1, mDay, mHour, mMinute);
        return DateUtils.dateToString(calendar.getTime(), DateUtils.COMPLETE_DATE_FORMAT);
    }

    /**
     * 获取新增记录的Id
     * 
     * @return
     */
    private String getRecordId() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(mYear, mMonth - 1, mDay, mHour, mMinute);
        return String.valueOf(calendar.getTimeInMillis());
    }
}
