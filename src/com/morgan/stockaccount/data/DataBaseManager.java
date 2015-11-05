package com.morgan.stockaccount.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.morgan.stockaccount.app.App;
import com.morgan.stockaccount.model.DealType;
import com.morgan.stockaccount.model.KeepStock;
import com.morgan.stockaccount.model.Stock;
import com.morgan.stockaccount.model.StockDealRecord;
import com.morgan.stockaccount.model.TransferRecord;
import com.morgan.stockaccount.model.TransferType;

/**
 * 数据库管理器，用于操作数据库中的所有数据
 * 
 * @author Morgan.Ji
 * @version 1.0
 * @date 2015年7月28日
 */
public class DataBaseManager extends SQLiteOpenHelper {

    private static final String TAG = DataBaseManager.class.getName();
    /**
     * 数据库版本
     */
    public static final int DATABASE_VERSION = 1;
    /**
     * 数据库名称
     */
    public static final String DATABASE_NAME = "StockAccount";
    private static Map<String, String> tables;
    private static DataBaseManager mInstance;
    private static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS %1$s";

    interface StockTable {

        public static final String TABLE_NAME = "stock";
        public static final String STOCK_NAME = "stock_name";
        public static final String STOCK_CODE = "stock_code";
        public static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS %1$S (" + STOCK_CODE
                + " TEXT PRIMARY KEY, " + STOCK_NAME + " TEXT )";
    }

    interface StockDealRecordTable {

        public static final String TABLE_NAME = "deal_record";
        public static final String RECORD_ID = "record_id";
        public static final String DEAL_TYPE = "deal_type";
        public static final String STOCK_CODE = "stock_code";
        public static final String STOCK_NAME = "stock_name";
        public static final String DEAL_NUMBER = "deal_number";
        public static final String DEAL_VALUE = "deal_value";
        public static final String DEAL_TIME = "deal_time";
        public static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS %1$S (" + RECORD_ID
                + " TEXT PRIMARY KEY, " + DEAL_TYPE + " TEXT ," + STOCK_NAME + " TEXT, " + STOCK_CODE + " TEXT, "
                + DEAL_NUMBER + " INTEGER," + DEAL_VALUE + " REAL," + DEAL_TIME + " TEXT)";
    }

    interface TransferRecordTable {

        public static final String TABLE_NAME = "transfer_record";
        public static final String RECORD_ID = "record_id";
        public static final String TRANSFER_TYPE = "transfer_type";
        public static final String TRANSFER_VALUE = "transfer_value";
        public static final String TRANSFER_TIME = "transfer_time";
        public static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS %1$S (" + RECORD_ID
                + " TEXT PRIMARY KEY, " + TRANSFER_TYPE + " TEXT ," + TRANSFER_VALUE + " REAL, " + TRANSFER_TIME
                + " TEXT)";
    }

    public static DataBaseManager getInstance() {
        if (null == mInstance) {
            mInstance = new DataBaseManager(App.getContext());
        }
        if (tables == null) {
            tables = new HashMap<String, String>();
            tables.put(StockTable.TABLE_NAME, StockTable.CREATE_TABLE_SQL);
            tables.put(StockDealRecordTable.TABLE_NAME, StockDealRecordTable.CREATE_TABLE_SQL);
            tables.put(TransferRecordTable.TABLE_NAME, TransferRecordTable.CREATE_TABLE_SQL);
        }
        return mInstance;
    }

    private DataBaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (db == null || tables == null) {
            return;
        }
        try {
            db.beginTransaction();
            for (String table : tables.keySet()) {
                db.execSQL(String.format(tables.get(table), table));
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "database tables create error " + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (db == null) {
            return;
        }
        try {
            dropAllTables(db);
            onCreate(db);
        } catch (Exception e) {
            Log.e(TAG, "upgrade database error " + e.getMessage());
        } finally {
            // empty here.
        }
    }

    /**
     * 删除数据库中所有的表
     * 
     * @param db
     */
    private void dropAllTables(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            for (String table : tables.keySet()) {
                db.execSQL(String.format(DROP_TABLE_SQL, table));
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "drop table error " + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 添加新的股票
     * 
     * @param stock
     * @return 是否添加成功
     */
    public boolean addStock(Stock stock) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues value = new ContentValues();
            value.put(StockTable.STOCK_CODE, stock.getCode());
            value.put(StockTable.STOCK_NAME, stock.getName());
            db.insertOrThrow(StockTable.TABLE_NAME, null, value);
        } catch (Exception e) {
            Log.e(TAG, "add stock into database error " + e.getMessage());
            return false;
        } finally {
            // empty here.
        }
        return true;
    }

    /**
     * 根据股票代码获取股票名称
     * 
     * @param stockCode
     *            股票代码
     * @return 股票名称
     */
    public String getStockName(String stockCode) {
        Cursor cursor = null;
        String stockName = "";
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(StockTable.TABLE_NAME, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                stockName = cursor.getString(cursor.getColumnIndex(StockTable.STOCK_NAME));
            }
        } catch (Exception e) {
            Log.e(TAG, "get stock name  error " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return stockName;
    }

    /**
     * 获取交易股票列表
     * 
     * @return
     */
    public List<Stock> getStockList() {
        List<Stock> list = new ArrayList<Stock>();
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(StockTable.TABLE_NAME, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    Stock stock = new Stock();
                    stock.setName(cursor.getString(cursor.getColumnIndex(StockTable.STOCK_NAME)));
                    stock.setCode(cursor.getString(cursor.getColumnIndex(StockTable.STOCK_CODE)));
                    list.add(stock);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "get stock list  error " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 添加新的交易记录
     * 
     * @param record
     * @return 是否添加成功
     */
    public boolean addStockDealRecord(StockDealRecord record) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues value = new ContentValues();
            value.put(StockDealRecordTable.RECORD_ID, record.getId());
            value.put(StockDealRecordTable.STOCK_NAME, record.getStockName());
            value.put(StockDealRecordTable.STOCK_CODE, record.getStockCode());
            value.put(StockDealRecordTable.DEAL_NUMBER, record.getNumber());
            value.put(StockDealRecordTable.DEAL_TIME, record.getTime());
            value.put(StockDealRecordTable.DEAL_TYPE, record.getType().name());
            value.put(StockDealRecordTable.DEAL_VALUE, record.getValue());
            db.insertOrThrow(StockDealRecordTable.TABLE_NAME, null, value);
            App.StockDataManager.onAddStockDealRecord(record);
        } catch (Exception e) {
            Log.e(TAG, "add deal record into database error " + e.getMessage());
            return false;
        } finally {
            // empty here.
        }
        return true;
    }

    /**
     * 获取指定股票的交易记录
     * 
     * @param stockCode
     * @return
     */
    public List<StockDealRecord> getDealRecordList(String stockCode) {
        List<StockDealRecord> list = new ArrayList<StockDealRecord>();
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(StockDealRecordTable.TABLE_NAME, null, StockDealRecordTable.STOCK_CODE + " = ? ",
                    new String[] { stockCode }, null, null, StockDealRecordTable.DEAL_TIME);
            if (cursor.moveToFirst()) {
                do {
                    StockDealRecord record = new StockDealRecord();
                    record.setStockCode(stockCode);
                    record.setStockName(cursor.getString(cursor.getColumnIndex(StockDealRecordTable.STOCK_NAME)));
                    record.setId(cursor.getString(cursor.getColumnIndex(StockDealRecordTable.RECORD_ID)));
                    record.setTime(cursor.getString(cursor.getColumnIndex(StockDealRecordTable.DEAL_TIME)));
                    record.setType(DealType.valueOf(cursor.getString(cursor
                            .getColumnIndex(StockDealRecordTable.DEAL_TYPE))));
                    record.setNumber(cursor.getInt(cursor.getColumnIndex(StockDealRecordTable.DEAL_NUMBER)));
                    record.setValue(cursor.getFloat(cursor.getColumnIndex(StockDealRecordTable.DEAL_VALUE)));
                    list.add(record);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "get deal record list error " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 获取所有股票的交易记录
     * 
     * @return
     */
    public List<StockDealRecord> getAllStockDealRecordList() {
        List<StockDealRecord> list = new ArrayList<StockDealRecord>();
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(StockDealRecordTable.TABLE_NAME, null, null, null, null, null,
                    StockDealRecordTable.DEAL_TIME);
            if (cursor.moveToFirst()) {
                do {
                    StockDealRecord record = new StockDealRecord();
                    record.setStockName(cursor.getString(cursor.getColumnIndex(StockDealRecordTable.STOCK_NAME)));
                    record.setStockCode(cursor.getString(cursor.getColumnIndex(StockDealRecordTable.STOCK_CODE)));
                    record.setId(cursor.getString(cursor.getColumnIndex(StockDealRecordTable.RECORD_ID)));
                    record.setTime(cursor.getString(cursor.getColumnIndex(StockDealRecordTable.DEAL_TIME)));
                    record.setType(DealType.valueOf(cursor.getString(cursor
                            .getColumnIndex(StockDealRecordTable.DEAL_TYPE))));
                    record.setNumber(cursor.getInt(cursor.getColumnIndex(StockDealRecordTable.DEAL_NUMBER)));
                    record.setValue(cursor.getFloat(cursor.getColumnIndex(StockDealRecordTable.DEAL_VALUE)));
                    list.add(record);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "get deal record list error " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 获取总收益
     * 
     * @return 总收益
     */
    public float getTotalRevenue() {
        float totalRevenue = 0f;
        Cursor cursor = null;
        try {
            List<Stock> StockList = getStockList();
            SQLiteDatabase db = getReadableDatabase();
            for (Stock stock : StockList) {
                KeepStock keepStockRecord = new KeepStock();
                keepStockRecord.setStockName(stock.getName());
                keepStockRecord.setStockCode(stock.getCode());
                cursor = db.query(StockDealRecordTable.TABLE_NAME, null, StockDealRecordTable.STOCK_CODE + " = ? ",
                        new String[] { stock.getCode() }, null, null, StockDealRecordTable.DEAL_TIME);
                float totalValue = 0f;
                if (cursor.moveToFirst()) {
                    do {
                        switch (DealType
                                .valueOf(cursor.getString(cursor.getColumnIndex(StockDealRecordTable.DEAL_TYPE)))) {
                        case BUY:
                            int number = cursor.getInt(cursor.getColumnIndex(StockDealRecordTable.DEAL_NUMBER));
                            keepStockRecord.setStockNumber(keepStockRecord.getStockNumber() + number);
                            totalValue += cursor.getFloat(cursor.getColumnIndex(StockDealRecordTable.DEAL_VALUE))
                                    * number;
                            break;
                        case SELL:
                            int number1 = cursor.getInt(cursor.getColumnIndex(StockDealRecordTable.DEAL_NUMBER));
                            keepStockRecord.setStockNumber(keepStockRecord.getStockNumber() - number1);
                            totalValue -= cursor.getFloat(cursor.getColumnIndex(StockDealRecordTable.DEAL_VALUE))
                                    * number1;
                            break;
                        default:
                            break;
                        }
                    } while (cursor.moveToNext());
                    if (keepStockRecord.getStockNumber() == 0) {// 因暂时无法获取实时数据，因此清仓的股票才能计算收益
                        totalRevenue += totalValue;
                    }
                }
                cursor.close();// 每次循环都要关闭指针
                cursor = null;// 防止finally二次关闭
            }
        } catch (Exception e) {
            Log.e(TAG, "get total revenue error " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return totalRevenue;
    }

    /**
     * 获取总持仓价值（此价值为买入价值，被平仓的股票被计入总收益就不在持仓价值内了）
     * 
     * @return 总持仓
     */
    public float getTotalKeepStockValue() {
        float totalKeepStockValue = 0f;
        Cursor cursor = null;
        try {
            List<Stock> StockList = getStockList();
            SQLiteDatabase db = getReadableDatabase();
            for (Stock stock : StockList) {
                KeepStock keepStockRecord = new KeepStock();
                keepStockRecord.setStockName(stock.getName());
                keepStockRecord.setStockCode(stock.getCode());
                cursor = db.query(StockDealRecordTable.TABLE_NAME, null, StockDealRecordTable.STOCK_CODE + " = ? ",
                        new String[] { stock.getCode() }, null, null, StockDealRecordTable.DEAL_TIME);
                float totalValue = 0f;
                if (cursor.moveToFirst()) {
                    do {
                        switch (DealType
                                .valueOf(cursor.getString(cursor.getColumnIndex(StockDealRecordTable.DEAL_TYPE)))) {
                        case BUY:
                            int number = cursor.getInt(cursor.getColumnIndex(StockDealRecordTable.DEAL_NUMBER));
                            keepStockRecord.setStockNumber(keepStockRecord.getStockNumber() + number);
                            totalValue += cursor.getFloat(cursor.getColumnIndex(StockDealRecordTable.DEAL_VALUE))
                                    * number;
                            break;
                        case SELL:
                            int number1 = cursor.getInt(cursor.getColumnIndex(StockDealRecordTable.DEAL_NUMBER));
                            keepStockRecord.setStockNumber(keepStockRecord.getStockNumber() - number1);
                            totalValue -= cursor.getFloat(cursor.getColumnIndex(StockDealRecordTable.DEAL_VALUE))
                                    * number1;
                            break;
                        default:
                            break;
                        }
                    } while (cursor.moveToNext());
                    if (keepStockRecord.getStockNumber() != 0) {
                        totalKeepStockValue += totalValue;
                    }
                }
                cursor.close();// 每次循环都要关闭指针
                cursor = null;// 防止finally二次关闭
            }
        } catch (Exception e) {
            Log.e(TAG, "get total keep stock value error " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return totalKeepStockValue;
    }

    /**
     * 获取持股记录
     * 
     * @return 持股记录
     */
    public List<KeepStock> getKeepStockList() {
        List<KeepStock> list = new ArrayList<KeepStock>();
        Cursor cursor = null;
        try {
            List<Stock> StockList = getStockList();
            SQLiteDatabase db = getReadableDatabase();
            for (Stock stock : StockList) {
                KeepStock keepStockRecord = new KeepStock();
                keepStockRecord.setStockName(stock.getName());
                keepStockRecord.setStockCode(stock.getCode());
                cursor = db.query(StockDealRecordTable.TABLE_NAME, null, StockDealRecordTable.STOCK_CODE + " = ? ",
                        new String[] { stock.getCode() }, null, null, StockDealRecordTable.DEAL_TIME);
                float totalValue = 0f;
                if (cursor.moveToFirst()) {
                    do {
                        switch (DealType
                                .valueOf(cursor.getString(cursor.getColumnIndex(StockDealRecordTable.DEAL_TYPE)))) {
                        case BUY:
                            int number = cursor.getInt(cursor.getColumnIndex(StockDealRecordTable.DEAL_NUMBER));
                            keepStockRecord.setStockNumber(keepStockRecord.getStockNumber() + number);
                            totalValue += cursor.getFloat(cursor.getColumnIndex(StockDealRecordTable.DEAL_VALUE))
                                    * number;
                            break;
                        case SELL:
                            int number1 = cursor.getInt(cursor.getColumnIndex(StockDealRecordTable.DEAL_NUMBER));
                            keepStockRecord.setStockNumber(keepStockRecord.getStockNumber() - number1);
                            totalValue -= cursor.getFloat(cursor.getColumnIndex(StockDealRecordTable.DEAL_VALUE))
                                    * number1;
                            break;
                        default:
                            break;
                        }
                    } while (cursor.moveToNext());
                    if (keepStockRecord.getStockNumber() != 0) {// 为0代表没有持股
                        keepStockRecord.setStockValue(totalValue / keepStockRecord.getStockNumber());
                    } else { // 没有持股的也可能有过交易，此时价格为该股收益
                        keepStockRecord.setStockValue(totalValue);
                    }
                    list.add(keepStockRecord);
                }
                cursor.close();// 每次循环都要关闭指针
                cursor = null;// 防止finally二次关闭
            }
        } catch (Exception e) {
            Log.e(TAG, "get keep stock record list error " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 添加转账记录
     * 
     * @param record
     * @return 是否添加成功
     */
    public boolean addTransferRecord(TransferRecord record) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues value = new ContentValues();
            value.put(TransferRecordTable.RECORD_ID, record.getId());
            value.put(TransferRecordTable.TRANSFER_TIME, record.getTime());
            value.put(TransferRecordTable.TRANSFER_TYPE, record.getType().name());
            value.put(TransferRecordTable.TRANSFER_VALUE, record.getMoney());
            db.insertOrThrow(TransferRecordTable.TABLE_NAME, null, value);
            App.StockDataManager.onAddTransferRecord(record);
        } catch (Exception e) {
            Log.e(TAG, "add transfer record into database error " + e.getMessage());
            return false;
        } finally {
            // empty here.
        }
        return true;
    }

    /**
     * 获取转帐记录列表
     * 
     * @return
     */
    public List<TransferRecord> getTransferRecordList() {
        List<TransferRecord> list = new ArrayList<TransferRecord>();
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(TransferRecordTable.TABLE_NAME, null, null, null, null, null,
                    TransferRecordTable.TRANSFER_TIME);
            if (cursor.moveToFirst()) {
                do {
                    TransferRecord record = new TransferRecord();
                    record.setId(cursor.getString(cursor.getColumnIndex(TransferRecordTable.RECORD_ID)));
                    record.setTime(cursor.getString(cursor.getColumnIndex(TransferRecordTable.TRANSFER_TIME)));
                    record.setType(TransferType.valueOf(cursor.getString(cursor
                            .getColumnIndex(TransferRecordTable.TRANSFER_TYPE))));
                    record.setMoney(cursor.getFloat(cursor.getColumnIndex(TransferRecordTable.TRANSFER_VALUE)));
                    list.add(record);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "get transfer record list error " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 获取转入资产列表
     * 
     * @return
     */
    public List<TransferRecord> getTransferInRecordList() {
        List<TransferRecord> list = new ArrayList<TransferRecord>();
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(TransferRecordTable.TABLE_NAME, null, TransferRecordTable.TRANSFER_TYPE + " = ? ",
                    new String[] { TransferType.IN.name() }, null, null, TransferRecordTable.TRANSFER_TIME);
            if (cursor.moveToFirst()) {
                do {
                    TransferRecord record = new TransferRecord();
                    record.setId(cursor.getString(cursor.getColumnIndex(TransferRecordTable.RECORD_ID)));
                    record.setTime(cursor.getString(cursor.getColumnIndex(TransferRecordTable.TRANSFER_TIME)));
                    record.setType(TransferType.valueOf(cursor.getString(cursor
                            .getColumnIndex(TransferRecordTable.TRANSFER_TYPE))));
                    record.setMoney(cursor.getFloat(cursor.getColumnIndex(TransferRecordTable.TRANSFER_VALUE)));
                    list.add(record);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "get transfer in record list error " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 获取转出资产列表
     * 
     * @return
     */
    public List<TransferRecord> getTransferOutRecordList() {
        List<TransferRecord> list = new ArrayList<TransferRecord>();
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(TransferRecordTable.TABLE_NAME, null, TransferRecordTable.TRANSFER_TYPE + " = ? ",
                    new String[] { TransferType.OUT.name() }, null, null, TransferRecordTable.TRANSFER_TIME);
            if (cursor.moveToFirst()) {
                do {
                    TransferRecord record = new TransferRecord();
                    record.setId(cursor.getString(cursor.getColumnIndex(TransferRecordTable.RECORD_ID)));
                    record.setTime(cursor.getString(cursor.getColumnIndex(TransferRecordTable.TRANSFER_TIME)));
                    record.setType(TransferType.valueOf(cursor.getString(cursor
                            .getColumnIndex(TransferRecordTable.TRANSFER_TYPE))));
                    record.setMoney(cursor.getFloat(cursor.getColumnIndex(TransferRecordTable.TRANSFER_VALUE)));
                    list.add(record);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "get transfer out record list error " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 导出数据库数据到SD卡目录下的stockaccount.db文件内
     * 
     * @return
     */
    public boolean exportData() {
        FileWriter writer = null;
        try {
            String filePath = Environment.getExternalStorageDirectory() + File.separator
                    + Constants.EXPORT_OR_IMPORT_FILE_NAME;
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            String newLine = "\r";
            String spliter = ",";
            if (file.createNewFile()) {
                writer = new FileWriter(file);
                List<Stock> stocks = getStockList();
                writer.write(stocks.size() + newLine);
                for (int i = 0; i < stocks.size(); i++) {
                    Stock stock = stocks.get(i);
                    String context = stock.getName() + spliter + stock.getCode() + spliter + stock.getValue();
                    writer.write(context + newLine);
                }
                List<TransferRecord> transferRecords = getTransferRecordList();
                writer.write(transferRecords.size() + newLine);
                for (int i = 0; i < transferRecords.size(); i++) {
                    TransferRecord transferRecord = transferRecords.get(i);
                    String context = transferRecord.getId() + spliter + transferRecord.getMoney() + spliter
                            + transferRecord.getTime() + spliter + transferRecord.getType().getValue();
                    writer.write(context + newLine);
                }
                List<StockDealRecord> stockDealRecords = getAllStockDealRecordList();
                writer.write(stockDealRecords.size() + newLine);
                for (int i = 0; i < stockDealRecords.size(); i++) {
                    StockDealRecord stockDealRecord = stockDealRecords.get(i);
                    String context = stockDealRecord.getId() + spliter + stockDealRecord.getNumber() + spliter
                            + stockDealRecord.getStockCode() + spliter + stockDealRecord.getStockName() + spliter
                            + stockDealRecord.getTime() + spliter + stockDealRecord.getValue() + spliter
                            + stockDealRecord.getType().getValue();
                    writer.write(context + newLine);
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 把SD卡目录下的stockaccount.db文件内的数据导入到数据库
     * 
     * @return
     */
    public boolean importData() {
        BufferedReader reader = null;
        try {
            String filePath = Environment.getExternalStorageDirectory() + File.separator
                    + Constants.EXPORT_OR_IMPORT_FILE_NAME;
            File file = new File(filePath);
            if (!file.exists()) {
                return false;
            }
            String spliter = ",";
            String line;
            int number;
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            List<Stock> stocks = new ArrayList<Stock>();
            line = reader.readLine();
            if (line != null) {
                number = Integer.valueOf(line);
                for (int i = 0; i < number; i++) {
                    line = reader.readLine();
                    String values[] = line.split(spliter);
                    Stock stock = new Stock();
                    stock.setName(values[0]);
                    stock.setCode(values[1]);
                    stock.setValue(Float.valueOf(values[2]));
                    stocks.add(stock);
                }
            }
            List<TransferRecord> transferRecords = getTransferRecordList();
            line = reader.readLine();
            if (line != null) {
                number = Integer.valueOf(line);
                for (int i = 0; i < number; i++) {
                    line = reader.readLine();
                    String values[] = line.split(spliter);
                    TransferRecord transferRecord = new TransferRecord();
                    transferRecord.setId(values[0]);
                    transferRecord.setMoney(Float.valueOf(values[1]));
                    transferRecord.setTime(values[2]);
                    transferRecord.setType(TransferType.valueOf(Integer.valueOf(values[3])));
                    transferRecords.add(transferRecord);
                }
            }
            List<StockDealRecord> stockDealRecords = getAllStockDealRecordList();
            line = reader.readLine();
            if (line != null) {
                number = Integer.valueOf(line);
                for (int i = 0; i < number; i++) {
                    line = reader.readLine();
                    String values[] = line.split(spliter);
                    StockDealRecord stockDealRecord = new StockDealRecord();
                    stockDealRecord.setId(values[0]);
                    stockDealRecord.setNumber(Integer.valueOf(values[1]));
                    stockDealRecord.setStockCode(values[2]);
                    stockDealRecord.setStockName(values[3]);;
                    stockDealRecord.setTime(values[4]);
                    stockDealRecord.setValue(Float.valueOf(values[5]));
                    stockDealRecord.setType(DealType.valueOf(Integer.valueOf(values[6])));
                    stockDealRecords.add(stockDealRecord);
                }
            }
            for (int i = 0; i < stocks.size(); i++) {
                if (!addStock(stocks.get(i))) {
                    return false;
                }
            }
            for (int i = 0; i < transferRecords.size(); i++) {
                if (!addTransferRecord(transferRecords.get(i))) {
                    return false;
                }
            }
            for (int i = 0; i < stockDealRecords.size(); i++) {
                if (!addStockDealRecord(stockDealRecords.get(i))) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
