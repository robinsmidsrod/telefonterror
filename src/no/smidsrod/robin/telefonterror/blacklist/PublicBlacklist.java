package no.smidsrod.robin.telefonterror.blacklist;

import no.smidsrod.robin.telefonterror.Main;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class PublicBlacklist extends SQLiteOpenHelper {

	public PublicBlacklist(Context context) {
		super(context, BlacklistDatabase.DATABASE_NAME, null,
				BlacklistDatabase.VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "";
		sql += "CREATE TABLE IF NOT EXISTS " + Blacklist.PUBLIC_TABLE_NAME;
		sql += "(";
		sql += Blacklist._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,";
		sql += Blacklist.NUMBER + " TEXT NOT NULL,";
		sql += Blacklist.NAME + " TEXT,";
		sql += Blacklist.URL + " TEXT,";
		sql += Blacklist.CATEGORY + " TEXT";
		sql += ");";
		db.execSQL(sql);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		try {
			onCreate(db);
		} catch (Exception e) {
			Main.debug("onOpen exception: " + e.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Just wipe the table on upgrade
		db.execSQL("DROP TABLE IF EXISTS " + Blacklist.PUBLIC_TABLE_NAME + ";");
		onCreate(db);
	}

}
