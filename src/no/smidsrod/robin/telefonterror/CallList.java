package no.smidsrod.robin.telefonterror;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.widget.SimpleCursorAdapter;

public class CallList extends ListActivity {

	private static final String[] FROM_CALLS = { CallLog.Calls.DATE, CallLog.Calls.NUMBER, CallLog.Calls.CACHED_NAME, CallLog.Calls.TYPE};
	private static final int[] TO_CALLS = {R.id.call_log_time, R.id.call_log_number, R.id.call_log_name, R.id.call_log_type};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_list);
		Cursor callers = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.call_log_item, callers, FROM_CALLS, TO_CALLS);
		setListAdapter(adapter);
	}

}
