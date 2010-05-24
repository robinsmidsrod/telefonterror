package no.smidsrod.robin.telefonterror;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;

public class CallList extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_list);

		// Filter outgoing calls - not interesting
		String where = CallLog.Calls.TYPE + " <> "
				+ CallLog.Calls.OUTGOING_TYPE;
		// Filter out calls that are in the contact list
		// FIXME: Should be a preference option
		where += " AND " + CallLog.Calls.CACHED_NAME + " IS NULL";

		// Sort order - newest first
		String sort = CallLog.Calls.DATE + " DESC";

		Cursor callers = managedQuery(CallLog.Calls.CONTENT_URI, null, where,
				null, sort);

		setListAdapter(new CallListAdapter(this, callers));
	}

}
