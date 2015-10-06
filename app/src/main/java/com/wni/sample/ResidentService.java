package com.wni.sample;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.wni.sample.util.Services;

import java.util.concurrent.TimeUnit;

/**
 * 常駐サービスです
 *
 * @author kob-to
 */
public class ResidentService extends Service {
	/**
	 * デバッグ用タグ
	 */
	private static final String TAG = ResidentService.class.getSimpleName();

	/**
	 * サービスの動作間隔を格納するキー
	 */
	private static final String INTENT_EXTRA_RUN_INTERVAL_KEY = "resident_interval";

	/**
	 * サービスのデフォルトの動作間隔
	 */
	private static final long DEFAULT_RUN_INTERVAL = TimeUnit.SECONDS.toMillis(10);

	/**
	 * サービスの常駐モードを格納するキー
	 */
	private static final String INTENT_EXTRA_RUN_MODE_KEY = "resident_mode";

	/**
	 * 初回起動
	 */
	private static final String INTENT_EXTRA_RUN_MODE_VALUE_START = "start";

	/**
	 * 継続
	 */
	private static final String INTENT_EXTRA_RUN_MODE_VALUE_CONTINUE = "continue";

	/**
	 * Singleton用
	 */
	private static ResidentService instance = null;

	/**
	 * 続行フラグ
	 */
	private boolean mContinue = false;

	/**
	 * サービスが作成された際に呼ばれます
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		//Singleton
		instance = this;
		mContinue = true;
	}

	/**
	 * サービスが破棄される際に呼ばれます
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		//Singleton
		instance = null;
	}

	/**
	 * サービスがバインドされる際に呼ばれます
	 *
	 * @param intent バインド時のIntent
	 * @return IBinder
	 */
	@Override
	public IBinder onBind(final Intent intent) {
		return null;//このサービスはバインドして使うサービスではない
	}

	/**
	 * startService()が実行された際に呼ばれます
	 *
	 * @param intent  startServiceに渡されたIntent
	 * @param flags   フラグ
	 * @param startId インスタンスのID
	 * @return Serviceの開始状態
	 */
	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		super.onStartCommand(intent, flags, startId);

		cancelNextAlarm(this);

		doSomething();

		if (!mContinue) {
			return Service.START_NOT_STICKY;
		}

		registerNextAlarm(this);

		return Service.START_REDELIVER_INTENT;
	}

	/**
	 * 初回起動・継続起動時に呼ばれます
	 */
	private void doSomething() {
		//TODO
		Log.d(TAG, "doSomething()が呼ばれました。");
		App.getInstance().writeLog("Hello, World.");
	}

	/**
	 * 次回起動を予約します
	 *
	 * @param context コンテキスト
	 */
	private static void registerNextAlarm(Context context) {
		Services.getAlarmManager(context).set(
				AlarmManager.RTC,
				System.currentTimeMillis() + DEFAULT_RUN_INTERVAL,
				PendingIntent.getService(
						context,
						0,
						new Intent(context, ResidentService.class),
						0
				)
		);
	}

	/**
	 * 次回起動をキャンセルします
	 *
	 * @param context コンテキスト
	 */
	private static void cancelNextAlarm(Context context) {
		Services.getAlarmManager(context).cancel(PendingIntent.getService(
				context,
				0,
				new Intent(context, ResidentService.class),
				PendingIntent.FLAG_UPDATE_CURRENT
		));
	}

	/**
	 * 常駐を開始します
	 *
	 * @param context コンテキスト
	 */
	public static void beginResident(Context context) {
		if (instance != null) {
			instance.mContinue = true;
		}
		cancelNextAlarm(context);
		context.startService(new Intent(context, ResidentService.class));
	}

	/**
	 * 常駐を終了します
	 *
	 * @param context コンテキスト
	 */
	public static void endResident(Context context) {
		if (instance != null) {
			instance.mContinue = false;
		}
		cancelNextAlarm(context);
		context.stopService(new Intent(context, ResidentService.class));
	}
}
