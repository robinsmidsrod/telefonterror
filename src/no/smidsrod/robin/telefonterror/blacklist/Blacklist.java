package no.smidsrod.robin.telefonterror.blacklist;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

public class Blacklist extends ContentProvider {

	// Database table names
	static final String PUBLIC_TABLE_NAME = "public";
	static final String PRIVATE_TABLE_NAME = "private";

	// Access URIs
	public static final String AUTHORITY = Blacklist.class.getPackage()
			.getName();
	public static final Uri PUBLIC_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + PUBLIC_TABLE_NAME);
	public static final Uri PRIVATE_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + PRIVATE_TABLE_NAME);

	// Table columns
	public static final String _ID = BaseColumns._ID;
	public static final String _COUNT = BaseColumns._COUNT;
	public static final String NUMBER = "phone_number";
	public static final String NAME = "name";
	public static final String URL = "url";
	public static final String CATEGORY = "category";

	// The content types we support
	private static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd."
			+ AUTHORITY;
	private static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd."
			+ AUTHORITY;

	// UriMatcher targets
	private static final int MATCH_PUBLIC_TABLE = 0;
	private static final int MATCH_PRIVATE_TABLE = 1;
	private static final int MATCH_PUBLIC_ID = 2;
	private static final int MATCH_PRIVATE_ID = 3;

	private UriMatcher uriMatcher;
	private PublicBlacklist publicBlacklist;
	private PrivateBlacklist privateBlacklist;

	public Blacklist() {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, PUBLIC_TABLE_NAME, MATCH_PUBLIC_TABLE);
		uriMatcher.addURI(AUTHORITY, PRIVATE_TABLE_NAME, MATCH_PRIVATE_TABLE);
		uriMatcher.addURI(AUTHORITY, PUBLIC_TABLE_NAME + "/#", MATCH_PUBLIC_ID);
		uriMatcher.addURI(AUTHORITY, PRIVATE_TABLE_NAME + "/#",
				MATCH_PRIVATE_ID);
	}

	@Override
	public boolean onCreate() {
		publicBlacklist = new PublicBlacklist(getContext());
		privateBlacklist = new PrivateBlacklist(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		int match = uriMatcher.match(uri);
		switch (match) {
		case MATCH_PUBLIC_TABLE:
			return CONTENT_TYPE_DIR;
		case MATCH_PRIVATE_TABLE:
			return CONTENT_TYPE_DIR;
		case MATCH_PUBLIC_ID:
			return CONTENT_TYPE_ITEM;
		case MATCH_PRIVATE_ID:
			return CONTENT_TYPE_ITEM;
		}
		throw new IllegalArgumentException("Unsupported URI '" + uri + "'");
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (uriMatcher.match(uri) == MATCH_PUBLIC_TABLE) {
			SQLiteDatabase db = publicBlacklist.getWritableDatabase();
			long newId = db.insertOrThrow(PUBLIC_TABLE_NAME, null, values);
			Uri newUri = ContentUris.withAppendedId(uri, newId);
			getContext().getContentResolver().notifyChange(newUri, null);
			return newUri;
		}
		if (uriMatcher.match(uri) == MATCH_PRIVATE_TABLE) {
			SQLiteDatabase db = privateBlacklist.getWritableDatabase();
			long newId = db.insertOrThrow(PRIVATE_TABLE_NAME, null, values);
			Uri newUri = ContentUris.withAppendedId(uri, newId);
			getContext().getContentResolver().notifyChange(newUri, null);
			return newUri;
		}
		throw new IllegalArgumentException("Unsupported URI '" + uri + "'");
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		if (uriMatcher.match(uri) == MATCH_PUBLIC_TABLE) {
			SQLiteDatabase db = publicBlacklist.getWritableDatabase();
			int count = db.update(PUBLIC_TABLE_NAME, values, selection,
					selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			return count;
		}
		if (uriMatcher.match(uri) == MATCH_PRIVATE_TABLE) {
			SQLiteDatabase db = privateBlacklist.getWritableDatabase();
			int count = db.update(PRIVATE_TABLE_NAME, values, selection,
					selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			return count;
		}
		if (uriMatcher.match(uri) == MATCH_PUBLIC_ID) {
			SQLiteDatabase db = publicBlacklist.getWritableDatabase();
			long id = Long.parseLong(uri.getPathSegments().get(1));
			int count = db.update(PUBLIC_TABLE_NAME, values, appendRowId(
					selection, id), selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			return count;
		}
		if (uriMatcher.match(uri) == MATCH_PRIVATE_ID) {
			SQLiteDatabase db = privateBlacklist.getWritableDatabase();
			long id = Long.parseLong(uri.getPathSegments().get(1));
			int count = db.update(PRIVATE_TABLE_NAME, values, appendRowId(
					selection, id), selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			return count;
		}
		throw new IllegalArgumentException("Unsupported URI '" + uri + "'");
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		if (uriMatcher.match(uri) == MATCH_PUBLIC_TABLE) {
			SQLiteDatabase db = publicBlacklist.getWritableDatabase();
			int count = db.delete(PUBLIC_TABLE_NAME, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			return count;
		}
		if (uriMatcher.match(uri) == MATCH_PRIVATE_TABLE) {
			SQLiteDatabase db = privateBlacklist.getWritableDatabase();
			int count = db.delete(PRIVATE_TABLE_NAME, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			return count;
		}
		if (uriMatcher.match(uri) == MATCH_PUBLIC_ID) {
			SQLiteDatabase db = publicBlacklist.getWritableDatabase();
			long id = Long.parseLong(uri.getPathSegments().get(1));
			int count = db.delete(PUBLIC_TABLE_NAME,
					appendRowId(selection, id), selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			return count;
		}
		if (uriMatcher.match(uri) == MATCH_PRIVATE_ID) {
			SQLiteDatabase db = privateBlacklist.getWritableDatabase();
			long id = Long.parseLong(uri.getPathSegments().get(1));
			int count = db.delete(PRIVATE_TABLE_NAME,
					appendRowId(selection, id), selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			return count;
		}
		throw new IllegalArgumentException("Unsupported URI '" + uri + "'");
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = null;
		String tableName = null;
		if (uriMatcher.match(uri) == MATCH_PUBLIC_TABLE) {
			db = publicBlacklist.getReadableDatabase();
			tableName = PUBLIC_TABLE_NAME;
		}
		if (uriMatcher.match(uri) == MATCH_PRIVATE_TABLE) {
			db = privateBlacklist.getReadableDatabase();
			tableName = PRIVATE_TABLE_NAME;
		}
		if (uriMatcher.match(uri) == MATCH_PUBLIC_ID) {
			db = publicBlacklist.getReadableDatabase();
			tableName = PUBLIC_TABLE_NAME;
			long id = Long.parseLong(uri.getPathSegments().get(1));
			selection = appendRowId(selection, id);
		}
		if (uriMatcher.match(uri) == MATCH_PRIVATE_ID) {
			db = privateBlacklist.getReadableDatabase();
			tableName = PRIVATE_TABLE_NAME;
			long id = Long.parseLong(uri.getPathSegments().get(1));
			selection = appendRowId(selection, id);
		}

		if (db != null && tableName != null) {
			String having = null;
			String groupBy = null;
			Cursor cursor = db.query(tableName, projection, selection,
					selectionArgs, groupBy, having, sortOrder);
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
			return cursor;
		}
		throw new IllegalArgumentException("Unsupported URI '" + uri + "'");

	}

	/** Append an id test to a SQL selection expression */
	private String appendRowId(String selection, long id) {
		String newSelection = !TextUtils.isEmpty(selection) ? " AND ("
				+ selection + ')' : "";
		return _ID + "=" + id + newSelection;
	}
}
