package com.morgan.stockaccount.task;

import com.morgan.stockaccount.model.TransferType;

import android.os.AsyncTask;

/**
 * 加载转账记录列表，加载类型为传入参数，如要加载所有类型，则只需传入空值
 * 
 * @author Morgan.Ji
 * @version 1.0
 * @date 2015年7月29日
 */
public class LoadTransferRecordListTask extends AsyncTask<TransferType, Void, Boolean> {

    @Override
    protected Boolean doInBackground(TransferType... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
    }
}
