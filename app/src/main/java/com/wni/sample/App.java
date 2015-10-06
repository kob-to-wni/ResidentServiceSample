package com.wni.sample;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Appを格納します
 *
 * @author kob-to
 */
public class App extends Application {
	/**
	 * ログ保存用キー
	 */
	private static final String PREFERENCE_LOG_KEY = "log";

	/**
	 * 改行コード
	 */
	private static final String CRLF = "\r\n";

	/**
	 * Singleton
	 */
	private static App instance;

	/**
	 * 日付フォーマッタ
	 */
	private final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPAN);

	/**
	 * 設定
	 */
	private SharedPreferences mSharedPreferences;

	/**
	 * アプリが起動した際に呼ばれます
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		//Singleton設定
		instance = this;
		//その他のフィールド変数の設定
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		ResidentService.beginResident(this);
	}

	/**
	 * アプリが終了した際に呼ばれます
	 */
	@Override
	public void onTerminate() {
		super.onTerminate();
		//Singleton設定
		instance = null;
	}

	/**
	 * ログを追記します
	 *
	 * @param message ログの内容
	 */
	public void writeLog(String message) {
		Set<String> log = mSharedPreferences.getStringSet(PREFERENCE_LOG_KEY, new HashSet<String>());
		log.add(new StringBuilder().append(mDateFormat.format(new Date())).append(" ").append(message).append(CRLF).toString());
		SharedPreferences.Editor e = mSharedPreferences.edit();
		e.putStringSet(PREFERENCE_LOG_KEY, log).apply();
		e.commit();
	}

	/**
	 * ログを一覧で取得します
	 *
	 * @return ログの配列
	 */
	public Set<String> readLog() {
		return mSharedPreferences.getStringSet(PREFERENCE_LOG_KEY, new HashSet<String>());
	}

	/**
	 * このアプリのインスタンスを取得します
	 *
	 * @return Appのインスタンス
	 */
	public static App getInstance() {
		return instance;
	}
}
