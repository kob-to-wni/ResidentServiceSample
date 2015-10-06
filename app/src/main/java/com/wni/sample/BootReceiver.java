package com.wni.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Androidデバイスの起動完了メッセージを処理します
 *
 * @author kob-to
 */
public class BootReceiver extends BroadcastReceiver {

	/**
	 * メッセージを受信した際に呼ばれます
	 *
	 * @param context コンテキスト
	 * @param intent  インテント
	 */
	@Override
	public void onReceive(final Context context, final Intent intent) {
		if (!Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			//対象外メッセージ
			return;
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				//常駐開始
				ResidentService.beginResident(context);
			}
		}).start();
	}
}
