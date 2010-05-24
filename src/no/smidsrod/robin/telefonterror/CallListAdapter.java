package no.smidsrod.robin.telefonterror;

import java.util.Calendar;

import no.smidsrod.robin.telefonterror.blacklist.Blacklist;
import no.smidsrod.robin.telefonterror.blacklist.BlacklistItem;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class CallListAdapter extends CursorAdapter implements ListAdapter {

	// Based on this code:
	// private static final String[] FROM_CALLS = { CallLog.Calls.DATE,
	// CallLog.Calls.NUMBER, CallLog.Calls.CACHED_NAME, CallLog.Calls.TYPE};
	// private static final int[] TO_CALLS = {R.id.call_log_time,
	// R.id.call_log_number, R.id.call_log_name, R.id.call_log_type};
	// SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
	// R.layout.call_log_item, callers, FROM_CALLS, TO_CALLS);

	private LayoutInflater inflater;
	private Activity activity;

	private int typeIndex;
	private int timeIndex;
	private int numberIndex;
	private int nameIndex;

	private int unknownColor;
	private int goodColor;
	private int badColor;

	private String unknownNumber;
	private BlacklistItem itemPublic;
	private BlacklistItem itemPrivate;

	public CallListAdapter(Context context, Cursor c) {
		super(context, c);
		init(context, c);
	}

	public CallListAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		init(context, c);
	}

	private void init(Context context, Cursor c) {
		this.activity = (Activity) context;
		inflater = LayoutInflater.from(context);

		typeIndex = c.getColumnIndex(CallLog.Calls.TYPE);
		timeIndex = c.getColumnIndex(CallLog.Calls.DATE);
		numberIndex = c.getColumnIndex(CallLog.Calls.NUMBER);
		nameIndex = c.getColumnIndex(CallLog.Calls.CACHED_NAME);

		unknownColor = context.getResources().getColor(
				R.color.call_log_type_unknown);
		goodColor = context.getResources().getColor(R.color.call_log_type_good);
		badColor = context.getResources().getColor(R.color.call_log_type_bad);

		unknownNumber = context.getResources().getString(
				R.string.unknown_number);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return inflater.inflate(R.layout.call_log_item, null);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		View layout = view.findViewById(R.id.call_log_item);
		TextView typeView = (TextView) view.findViewById(R.id.call_log_type);
		TextView timeView = (TextView) view.findViewById(R.id.call_log_time);
		TextView numberView = (TextView) view
				.findViewById(R.id.call_log_number);
		TextView nameView = (TextView) view.findViewById(R.id.call_log_name);

		int type = cursor.getInt(typeIndex);
		long time = cursor.getLong(timeIndex);
		String number = cursor.getString(numberIndex);
		String name = cursor.getString(nameIndex);

		itemPublic = BlacklistItem.getByNumber(activity,
				Blacklist.PUBLIC_CONTENT_URI, number);
		itemPrivate = BlacklistItem.getByNumber(activity,
				Blacklist.PRIVATE_CONTENT_URI, number);

		typeView.setText(appendCategory(calcTypeText(type)));
		timeView.setText(calcTimeText(time));
		numberView.setText(calcNumberText(number));
		nameView.setText(calcNameText(name));

		if (name != null && name.length() > 0) {
			layout.setBackgroundColor(goodColor);
		} else {
			if (isBadNumber()) {
				layout.setBackgroundColor(badColor);
			} else {
				layout.setBackgroundColor(unknownColor);
			}
		}

	}

	private boolean isBadNumber() {
		if (itemPrivate != null || itemPublic != null) {
			return true;
		}
		return false;
	}

	private String calcTypeText(int callLogType) {
		switch (callLogType) {
		case CallLog.Calls.INCOMING_TYPE:
			return "I";
		case CallLog.Calls.MISSED_TYPE:
			return "M";
		case CallLog.Calls.OUTGOING_TYPE:
			return "O";
		default:
			return "U";
		}
	}

	private String appendCategory(String type) {
		if (itemPublic != null) {
			return type + ": " + itemPublic.getCategory();
		}
		if (itemPrivate != null) {
			return type + ": " + itemPrivate.getCategory();
		}
		return type;
	}

	private String calcTimeText(long time) {
		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(time);
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(System.currentTimeMillis());

		String formatted;
		if (isMoreThanADay(date, now)) {
			formatted = date.get(Calendar.YEAR) + "-"
					+ pad(date.get(Calendar.MONTH) + 1) + "-"
					+ pad(date.get(Calendar.DAY_OF_MONTH)) + " "
					+ pad(date.get(Calendar.HOUR_OF_DAY)) + ":"
					+ pad(date.get(Calendar.MINUTE));
		} else {
			formatted = pad(date.get(Calendar.HOUR_OF_DAY)) + ":"
					+ pad(date.get(Calendar.MINUTE));
		}

		return formatted;
	}

	private String calcNumberText(String number) {
		if (number.equals("-1")) {
			return "<" + unknownNumber + ">";
		}
		return number;
	}

	private String calcNameText(String name) {
		if (itemPrivate != null) {
			return itemPrivate.getName();
		}
		if (itemPublic != null) {
			return itemPublic.getName();
		}
		return name;
	}

	private String pad(int i) {
		if (i <= 0) {
			return "00";
		}
		if (i >= 10) {
			return i + "";
		}
		return "0" + i;
	}

	private boolean isMoreThanADay(Calendar date, Calendar now) {
		long oneDay = 24 * 60 * 60 * 1000;
		long difference = Math.abs(now.getTimeInMillis()
				- date.getTimeInMillis());
		return difference > oneDay;
	}

}
