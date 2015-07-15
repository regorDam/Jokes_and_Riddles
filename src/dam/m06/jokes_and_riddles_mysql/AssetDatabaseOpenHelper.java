package dam.m06.jokes_and_riddles_mysql;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * █║▌│ █│║▌ ║││█║▌ │║║█║ 
 * Author : Regør [★] 
 * Who in Black Byte 
 * Program Jokes & Riddles - SQLite V.1
 */

public class AssetDatabaseOpenHelper {

	//private static final String DB_NAME = "JRDataBase";

	private Context context;
	private SQLiteDatabase database;
	private final String DATABASE_NAME;
	private File dbFile;

	// private static final int DATABASE_VERSION;

	public AssetDatabaseOpenHelper(Context context, String name) {
		this.context = context;
		this.DATABASE_NAME = name;
	}

	public SQLiteDatabase openDatabase() {
		dbFile = context.getDatabasePath(DATABASE_NAME);
		File test = new File(context.getFilesDir()+"/databases", DATABASE_NAME);
	
		if (!test.exists()) {
			try {
				copyDatabase();
			} catch (IOException e) {
				throw new RuntimeException("Error creating source database", e);
			}
		} else {
			Log.i("LogsAndroid", "database exist");
		}
		try {
			
			database = SQLiteDatabase.openDatabase(test.getPath(), null,
					SQLiteDatabase.OPEN_READWRITE);

            //database = SQLiteDatabase.openDatabase(mDatabasePath + "/" + DB_NAME, mFactory, SQLiteDatabase.OPEN_READWRITE);
		} catch (Exception e) {
			Log.e("LogsAndroid", "MEEEC");
		}
		return database;
	}

	private void copyDatabase() throws IOException {
		Log.i("LogsAndroid", "copy database");
		InputStream is = context.getAssets().open("databases/" + DATABASE_NAME);

		try {
			String pathDatabase = context.getFilesDir()+"/databases";
			File path = new File(pathDatabase);
			Log.i("LogsAndroid", "PATH-->"+path.getPath());
			path.mkdir();
			dbFile = new File(context.getFilesDir()+"/databases", DATABASE_NAME);
			OutputStream os = new FileOutputStream(dbFile);
			Log.i("LogsAndroid", dbFile.getAbsolutePath());

			Log.i("LogsAndroid", ""+dbFile.exists());
			byte[] buffer = new byte[1024];

			while (is.read(buffer) > 0) {
				os.write(buffer);
			}
			os.flush();
			os.close();
			is.close();

			Log.w("LogsAndroid", "database copy complete");
		} catch (Exception e) {
			Log.e("LogsAndroid", e.toString());
		}

	}

	public void close() {
		 database.close();
	}

}
