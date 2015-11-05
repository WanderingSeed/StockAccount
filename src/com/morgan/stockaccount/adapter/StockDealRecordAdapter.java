package com.morgan.stockaccount.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.morgan.stockaccount.R;
import com.morgan.stockaccount.model.DealType;
import com.morgan.stockaccount.model.StockDealRecord;

public class StockDealRecordAdapter extends BaseAdapter {

    private List<StockDealRecord> mRecords = new ArrayList<StockDealRecord>();
    private Context mContext;

    public StockDealRecordAdapter(Context context, List<StockDealRecord> records) {
        this.mContext = context;
        this.mRecords = records;
    }

    @Override
    public int getCount() {
        return mRecords.size();
    }

    @Override
    public Object getItem(int position) {
        return mRecords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.stock_deal_record_list_item, null);
            viewHolder.mDealStockNameTxt = (TextView) convertView.findViewById(R.id.deal_stock_name_textView);
            viewHolder.mDealStockValueTxt = (TextView) convertView.findViewById(R.id.deal_stock_value_textView);
            viewHolder.mDealStockNumberTxt = (TextView) convertView.findViewById(R.id.deal_stock_number_textView);
            viewHolder.mDealStockTypeTxt = (TextView) convertView.findViewById(R.id.deal_type_textView);
            viewHolder.mDealStockDateTxt = (TextView) convertView.findViewById(R.id.deal_stock_date_textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        StockDealRecord record = mRecords.get(position);
        viewHolder.mDealStockNameTxt.setText(record.getStockName());
        viewHolder.mDealStockNumberTxt.setText(String.valueOf(record.getNumber()));
        viewHolder.mDealStockValueTxt.setText(String.valueOf(record.getValue()));
        viewHolder.mDealStockTypeTxt.setText(record.getType() == DealType.BUY ? mContext.getResources().getString(
                R.string.buy) : mContext.getResources().getString(R.string.sell));
        viewHolder.mDealStockDateTxt.setText(record.getTime());
        return convertView;
    }

    class ViewHolder {

        TextView mDealStockDateTxt;
        TextView mDealStockTypeTxt;
        TextView mDealStockValueTxt;
        TextView mDealStockNumberTxt;
        TextView mDealStockNameTxt;
    }

}
