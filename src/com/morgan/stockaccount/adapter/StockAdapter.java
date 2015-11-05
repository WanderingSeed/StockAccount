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
import com.morgan.stockaccount.model.Stock;

public class StockAdapter extends BaseAdapter {

    private List<Stock> mList = new ArrayList<Stock>();
    private Context mContext;

    public StockAdapter(Context context, List<Stock> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.stock_list_item, null);
            viewHolder.mStockNameTxt = (TextView) convertView.findViewById(R.id.stock_name_textView);
            viewHolder.mStockCodeTxt = (TextView) convertView.findViewById(R.id.stock_code_textView);
            viewHolder.mStockValueTxt = (TextView) convertView.findViewById(R.id.stock_value_textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Stock stock = mList.get(position);
        viewHolder.mStockNameTxt.setText(stock.getName());
        viewHolder.mStockCodeTxt.setText(stock.getCode());
        if (stock.getValue() == 0) {
            viewHolder.mStockValueTxt.setText("--");
        } else {
            viewHolder.mStockValueTxt.setText(String.valueOf(stock.getValue()));
        }
        return convertView;
    }

    class ViewHolder {

        TextView mStockNameTxt;
        TextView mStockCodeTxt;
        TextView mStockValueTxt;
    }

}
