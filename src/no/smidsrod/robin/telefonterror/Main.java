package no.smidsrod.robin.telefonterror;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity implements OnClickListener {

	private static final String APP_NAME = "telefonterror";

	private Button viewCallLogButton;
	private Button updateBlacklistButton;
	private TextView lastUpdatedText;

	static void debug(String string) {
		Log.d(APP_NAME, string);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		viewCallLogButton = (Button) findViewById(R.id.view_call_log_button);
		viewCallLogButton.setOnClickListener(this);

		updateBlacklistButton = (Button) findViewById(R.id.update_blacklist_button);
		updateBlacklistButton.setOnClickListener(this);

		lastUpdatedText = (TextView) findViewById(R.id.blacklist_last_updated_text);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(viewCallLogButton)) {
			viewCallLog();
		}
		if (v.equals(updateBlacklistButton)) {
			updateBlacklist();
		}
	}

	private void viewCallLog() {
		Main.debug("View call log button clicked!");
		Intent intent = new Intent(this, CallList.class);
		startActivity(intent);
	}

	private void updateBlacklist() {
		Main.debug("Update blacklist button clicked!");
		lastUpdatedText.setText(getResources().getString(
				R.string.updating_blacklist));
		final Handler uiThread = new Handler();
		ExecutorService updateThread = Executors.newSingleThreadExecutor();
		Runnable updateTask = new Runnable() {
			@Override
			public void run() {
				// do something (in another thread)
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
				// publish results in GUI thread
				uiThread.post(new Runnable() {
					@Override
					public void run() {
						lastUpdatedText.setText(getResources().getString(
								R.string.blacklist_never_updated));
					}
				});
			}
		};
		updateThread.execute(updateTask);
	}

}