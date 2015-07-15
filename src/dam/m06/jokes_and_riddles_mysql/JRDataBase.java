package dam.m06.jokes_and_riddles_mysql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @encrypt █║▌│ █│║▌ ║││█║▌ │║║█║
 * @author Author : Regør [★]
 * @who Who in Black Byte
 * @program Program Jokes & Riddles - SQLiteV.1
 */
public class JRDataBase extends AssetDatabaseOpenHelper {

	private static final String DATABASE_NAME = "JRDataBase";

	public JRDataBase(Context context) {
		super(context, DATABASE_NAME);

	}

	/** INSERT JOKE & RIDDLE CATEGORY **/
	public long insertCategory(String nom, String description) {
		SQLiteDatabase db = this.openDatabase();
		ContentValues cv = new ContentValues();
		cv.put("name", nom);
		cv.put("description", description);
		db.insert("JOKES_CATEGORY", null, cv);
		return db.insert("RIDDLE_CATEGORY", null, cv);
	}
	
	public long insertCategory(String nom, String description,String tableName) {
		SQLiteDatabase db = this.openDatabase();
		ContentValues cv = new ContentValues();
		cv.put("name", nom);
		cv.put("description", description);
		return db.insert(tableName, null, cv);
	}

	/** INSERT JOKES **/
	public long insertJoke(String statment, int category_id, int language_id) {
		SQLiteDatabase db = this.openDatabase();
		ContentValues cv = new ContentValues();
		cv.put("statment", statment);
		cv.put("category_id", category_id);
		cv.put("language_id", language_id);

		return db.insert("JOKES", null, cv);
	}

	/** INSERT RIDDLE **/
	public long insertRiddle(String question, String answer, int category_id,
			int language_id) {
		SQLiteDatabase db = this.openDatabase();
		ContentValues cv = new ContentValues();
		cv.put("question", question);
		cv.put("answer", answer);
		cv.put("category_id", category_id);
		cv.put("language_id", language_id);

		return db.insert("RIDDLE", null, cv);
	}

	/** UPDATE RIDDLE **/
	public boolean updateRiddle(long id, String question, String answer,
			String created, int category_id, int language_id) {
		SQLiteDatabase db = this.openDatabase();
		ContentValues cv = new ContentValues();
		cv.put("question", question);
		cv.put("answer", answer);
		cv.put("created", created);
		cv.put("category_id", category_id);
		cv.put("language_id", language_id);
		return db.update("RIDDLE", cv, "_id=" + id, null) > 0;
	}
	public boolean updateRiddle(long id, String question, String answer, int category_id, int language_id) {
		SQLiteDatabase db = this.openDatabase();
		ContentValues cv = new ContentValues();
		cv.put("question", question);
		cv.put("answer", answer);
		cv.put("category_id", category_id);
		cv.put("language_id", language_id);
		return db.update("RIDDLE", cv, "_id=" + id, null) > 0;
	}

	/** UPDATE JOKES **/
	public boolean updateJoke(long id, String statment, String created,
			int category_id, int language_id) {
		SQLiteDatabase db = this.openDatabase();
		ContentValues cv = new ContentValues();
		cv.put("statment", statment);
		cv.put("created", created);
		cv.put("category_id", category_id);
		cv.put("language_id", language_id);

		return db.update("JOKES", cv, "_id=" + id, null) > 0;
	}
	public boolean updateJoke(long id, String statment,
			int category_id, int language_id) {
		SQLiteDatabase db = this.openDatabase();
		ContentValues cv = new ContentValues();
		cv.put("statment", statment);
		cv.put("category_id", category_id);
		cv.put("language_id", language_id);

		return db.update("JOKES", cv, "_id=" + id, null) > 0;
	}

	/** SELECTS **/
	public Cursor getJokesOrRiddlesByCategory(int idCategory, String tableName) {
		return getCursorFrom(tableName + " WHERE category_id = '" + idCategory
				+ "'");
	}

	public Cursor getCursorFrom(String tableName) {
		SQLiteDatabase db = this.openDatabase();
		String sql = "SELECT *  FROM " + tableName;
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return c;
	}

	public Cursor getCursorFromDate(String tableName) {
		SQLiteDatabase db = this.openDatabase();
		String sql = "SELECT DISTINCT strftime('%Y-%m-%d',created) AS _id FROM "
				+ tableName;
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return c;
	}
	
	public boolean deleteFrom(String tableName, int id){
		SQLiteDatabase db = this.openDatabase();
		return db.delete(tableName, "_id = " + id, null) > 0;	
	}

}
