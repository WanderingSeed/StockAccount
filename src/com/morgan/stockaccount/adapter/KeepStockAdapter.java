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
import com.morgan.stockaccount.model.KeepStock;
import com.morgan.stockaccount.util.StrUtils;

public class KeepStockAdapter extends BaseAdapter {

    private List<KeepStock> mList = new ArrayList<KeepStock>();
    private Context mContext;

    public KeepStockAdapter(Context context, List<KeepStock> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.keep_stock_list_item, null);
            viewHolder.mStockNameTxt = (TextView) convertView.findViewById(R.id.stock_name_textView);
            viewHolder.mStockNumberTxt = (TextView) convertView.findViewById(R.id.stock_number_textView);
            viewHolder.mStockValueTxt = (TextView) convertView.findViewById(R.id.stock_value_textView);
            viewHolder.mStockTotalValueTxt = (TextView) convertView.findViewById(R.id.stock_total_value_textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        KeepStock record = mList.get(position);
        viewHolder.mStockNameTxt.setText(record.getStockName());
        viewHolder.mStockNumberTxt.setText(String.valueOf(record.getStockNumber()));
        if (record.getStockNumber() == 0) {
            viewHolder.mStockValueTxt.setText("--");
            viewHolder.mStockTotalValueTxt.setText(String.valueOf(record.getStockValue()));
        } else {
            viewHolder.mStockValueTxt.setText(String.valueOf(record.getStockValue()));
            viewHolder.mStockTotalValueTxt.setText(StrUtils.toString(record.getStockValue() * record.getStockNumber()));
        }
        return convertView;
    }

    class ViewHolder {

        TextView mStockNameTxt;
        TextView mStockNumberTxt;
        TextView mStockValueTxt;
        TextView mStockTotalValueTxt;
    }

}
