package com.morgan.stockaccount.app;

import android.app.Application;

/**
 * 应用类，初始化一些程序的必须数据
 * 
 * @author Morgan.Ji
 * 
 * @version 1.0
 *
 * @date 2015年7月23日
 */
public class APP  extends Application {

	/**
	 * 当前程序运行状态，是否为调试状态，比如一些日志根据此变量来决定是否输出
	 */
	public static final boolean DEBUG = true;
	
	@Override
	public void onCreate() {
	}
}
