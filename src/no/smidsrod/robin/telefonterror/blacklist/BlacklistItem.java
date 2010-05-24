package no.smidsrod.robin.telefonterror.blacklist;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;

public class BlacklistItem {

	private static final String[] FIELDS = { Blacklist._ID, Blacklist.NUMBER,
			Blacklist.NAME, Blacklist.URL, Blacklist.CATEGORY };

	private long id;
	private String number;
	private String name;
	private String url;
	private String category;

	private BlacklistItem(long id, String number, String name, String url,
			String category) {
		this.id = id;
		this.number = number;
		this.name = name;
		this.url = url;
		this.category = category;
	}

	public long getId() {
		return id;
	}

	public String getNumber() {
		return number;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public String getCategory() {
		return category;
	}

	public static BlacklistItem getByNumber(Activity activity, Uri uri,
			String number) {

		// Setup query parameters
		String selection = Blacklist.NUMBER + " LIKE ?";
		String[] selectionArgs = new String[] { "%" + number };
		String sortOrder = Blacklist.NUMBER + " ASC";

		Cursor c = activity.managedQuery(uri, FIELDS, selection, selectionArgs,
				sortOrder);
		if (c.moveToFirst()) {

			int idIndex = c.getColumnIndex(FIELDS[0]);
			int numberIndex = c.getColumnIndex(FIELDS[1]);
			int nameIndex = c.getColumnIndex(FIELDS[2]);
			int urlIndex = c.getColumnIndex(FIELDS[3]);
			int categoryIndex = c.getColumnIndex(FIELDS[4]);

			long id = c.getLong(idIndex);
			String fetchedNumber = c.getString(numberIndex);
			String name = c.getString(nameIndex);
			String url = c.getString(urlIndex);
			String category = c.getString(categoryIndex);

			return new BlacklistItem(id, fetchedNumber, name, url, category);
		}
		return null;
	}

}
