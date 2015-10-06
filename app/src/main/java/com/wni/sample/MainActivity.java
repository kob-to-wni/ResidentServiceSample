package com.wni.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

/**
 * アプリのメインです
 *
 * @author kob-to
 */
public class MainActivity extends Activity {

	/**
	 * アクティビティが起動された際に呼ばれます
	 *
	 * @param savedInstanceState 以前の状態
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//ログ表示を更新
		StringBuilder sb = new StringBuilder();
		for (String s : App.getInstance().readLog()) {
			sb.append(s);
		}
		((EditText) findViewById(R.id.editText)).setText(sb);
	}
}
