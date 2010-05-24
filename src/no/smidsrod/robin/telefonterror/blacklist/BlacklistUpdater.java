package no.smidsrod.robin.telefonterror.blacklist;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import no.smidsrod.robin.telefonterror.Main;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import au.com.bytecode.opencsv.CSVReader;

public class BlacklistUpdater {

	private static final String TELEFONTERROR_CSV_URL = "http://www.telefonterror.no/uke_alle/terrorliste.csv";

	private ContentResolver cr;

	public int updatePublic(ContentResolver cr) {
		this.cr = cr;

		try {
			CSVReader reader = new CSVReader(getReader());
			wipePublicBlacklist();
			String[] columns;
			reader.readNext(); // Skip first line
			int count = 0;
			while ((columns = reader.readNext()) != null) {
				// nextLine[] is an array of values from the line
				// Main.debug("line: " + Main.join(columns));

				String name = columns[1];
				String number = columns[2];
				String url = columns[3];
				String category = columns[4];

				insertItem(number, name, url, category);
				count++;
			}
			return count;
		} catch (Exception e) {
			Main.debug("Exception occured: " + e.getMessage());
		}
		return 0;
	}

	private Reader getReader() throws ClientProtocolException, IOException {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet req = new HttpGet(TELEFONTERROR_CSV_URL);
		HttpResponse res = client.execute(req);
		if (res.getStatusLine().getStatusCode() >= 400) {
			Main.debug("HTTP error: " + res.getStatusLine().getStatusCode()
					+ ": " + res.getStatusLine().getReasonPhrase());
			return null;
		}
		HttpEntity entity = res.getEntity();
		if (entity == null) {
			return null;
		}
		return new InputStreamReader(entity.getContent());
	}

	private void wipePublicBlacklist() {
		String where = null;
		String[] selectionArgs = null;
		cr.delete(Blacklist.PUBLIC_CONTENT_URI, where, selectionArgs);
	}

	// For the future:
	//
	// private void wipePrivateBlacklist() {
	// String where = null;
	// String[] selectionArgs = null;
	// cr.delete(Blacklist.PRIVATE_CONTENT_URI, where, selectionArgs);
	// }

	private Uri insertItem(String number, String name, String url,
			String category) {

		ContentValues values = new ContentValues();
		values.put(Blacklist.NUMBER, number);
		values.put(Blacklist.CATEGORY, category);
		values.put(Blacklist.URL, url);
		values.put(Blacklist.NAME, name);

		Uri newUri = cr.insert(Blacklist.PUBLIC_CONTENT_URI, values);
		Main.debug("URI inserted: " + number + ", " + name + ", " + url
				+ ", " + category + ", " + newUri);
		return newUri;
	}

}
