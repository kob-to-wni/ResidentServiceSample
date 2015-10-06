package com.wni.sample.util;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Service用ユーティリティ
 *
 * @author kob-to
 */
public class Services {

	/**
	 * AlarmManagerを取得します
	 *
	 * @param context コンテキスト
	 * @return AlarmManagerのインスタンス
	 */
	public static @NonNull AlarmManager getAlarmManager(Context context) {
		AlarmManager instance = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
		if (instance == null) {
			throw new IllegalStateException("AlarmManagerを取得できませんでした");
		}
		return instance;
	}

}
