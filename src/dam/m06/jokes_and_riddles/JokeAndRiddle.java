package dam.m06.jokes_and_riddles;

import dam.m06.jokes_and_riddles_mysql.JRDataBase;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

/**
 * @encrypt █║▌│ █│║▌ ║││█║▌ │║║█║
 * @author Author : Regør [★]
 * @who Who in Black Byte
 * @program Program Jokes & Riddles - SQLiteV.1
 */

public class JokeAndRiddle extends Activity implements OnItemSelectedListener,
		OnCheckedChangeListener, OnItemClickListener, OnItemLongClickListener {

	private JRDataBase db;
	private Cursor riddles;
	private Cursor jokes;
	private Cursor category;
	private Cursor dates;
	private Cursor language;
	private ListView listView;
	private Spinner spinnerCategory, spinnerDate;
	private int op;
	private String FROM;
	private boolean check;
	private CheckBox checkBox;
	private final int[] idItemListRiddle = { R.id.tv_id,
			R.id.tv_statementQuestion, R.id.tv_answer, R.id.tv_created };
	private final int[] idItemListJokes = { R.id.tv_id,
			R.id.tv_statementQuestion, R.id.tv_created };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joke_and_riddle);
		listView = (ListView) findViewById(R.id.lv_jokeAndRiddle);
		spinnerCategory = (Spinner) findViewById(R.id.sp_category);
		spinnerDate = (Spinner) findViewById(R.id.sp_date);
		checkBox = (CheckBox) findViewById(R.id.checkBox1);

		spinnerCategory.setOnItemSelectedListener(this);
		spinnerCategory.setEnabled(false);
		spinnerDate.setEnabled(false);

		spinnerDate.setOnItemSelectedListener(this);

		checkBox.setOnCheckedChangeListener(this);

		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);

		db = new JRDataBase(this);
		db.openDatabase();
		op = (Integer) getIntent().getExtras().get("KEY");
		LoadSpinner();
	}

	private void LoadSpinner() {
		language = db.getCursorFrom("language");
		switch (op) {
		case 1:
			FROM = "JOKES";
			setTitle(getString(R.string.jokes));
			category = db.getCursorFrom("jokes_category");
			loadAdapterCategory();
			jokes = db.getCursorFrom("jokes");
			loadAdapterJokes();
			dates = db.getCursorFromDate("jokes");
			loadAdapterDate();
			break;
		case 2:
			FROM = "RIDDLE";
			setTitle(getString(R.string.riddles));
			dates = db.getCursorFromDate("riddle");
			loadAdapterDate();
			category = db.getCursorFrom("riddle_category");
			loadAdapterCategory();
			riddles = db.getCursorFrom("riddle");
			loadAdapterRiddles();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.joke_and_riddle, menu);
		return true;
	}

	@SuppressWarnings("deprecation")
	public void loadAdapterLanguage(Spinner spinner) {
		SpinnerAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, language,
				new String[] { "name" }, new int[] { android.R.id.text1 });
		spinner.setAdapter(adapter);

	}

	@SuppressWarnings("deprecation")
	public void loadAdapterCategory() {
		SpinnerAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, category,
				new String[] { "name" }, new int[] { android.R.id.text1 });
		spinnerCategory.setAdapter(adapter);

	}

	@SuppressWarnings("deprecation")
	public void loadAdapterDate() {
		try {
			SpinnerAdapter adapter = new SimpleCursorAdapter(this,
					android.R.layout.simple_list_item_1, dates,
					new String[] { "_id" }, new int[] { android.R.id.text1 });
			spinnerDate.setAdapter(adapter);
		} catch (Exception e) {
			Log.w("LogsAndroid", dates.getCount() + " Error-> " + e.toString());
		}
	}

	@SuppressWarnings("deprecation")
	public void loadAdapterJokes() {
		String[] columns = { "_id", "statment", "created" };
		ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.item_list,
				jokes, columns, idItemListJokes);
		listView.setAdapter(adapter);
	}

	@SuppressWarnings("deprecation")
	public void loadAdapterRiddles() {
		String[] columns = { "_id", "question", "answer", "created" };
		ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.item_list,
				riddles, columns, idItemListRiddle);
		listView.setAdapter(adapter);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if (id == R.id.action_insert) {
			if (op == 1)
				addInsertJokeFrame();
			else
				addInsertRiddleFrame();

			return true;
		} else if (id == R.id.action_addCategory) {
			addCategoryFrame();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View v, int arg2, long arg3) {

		switch (arg0.getId()) {
		case R.id.sp_category:
			Log.i("LogsAndroid", "category");
			if (check) {
				switch (op) {
				case 1:
					jokes = db.getJokesOrRiddlesByCategory(category.getInt(0),
							"jokes");
					loadAdapterJokes();
					break;
				case 2:
					riddles = db.getJokesOrRiddlesByCategory(
							category.getInt(0), "riddle");
					loadAdapterRiddles();
					break;
				default:
					break;
				}
			}
			break;
		case R.id.sp_date:

			Log.i("LogsAndroid", "date");
			break;
		default:
			break;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {
			spinnerCategory.setEnabled(true);
			spinnerDate.setEnabled(true);
			check = true;
		} else {
			spinnerCategory.setEnabled(false);
			spinnerDate.setEnabled(false);
			check = false;
			LoadSpinner();
		}

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View v, int arg2,
			long arg3) {
		String var;
		if (op == 2) {
			var = riddles.getString(0);
			displayAlertDialog(arg2);
		} else {
			var = jokes.getString(0);
			displayAlertDialog(arg2);
		}
		Toast.makeText(this, "id: " + var, Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		switch (arg0.getId()) {
		case R.id.lv_jokeAndRiddle:
			displayUpdateDialog(arg2);
			break;

		default:
			break;
		}

	}

	private void addInsertJokeFrame() {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		LayoutInflater li = LayoutInflater.from(this);
		View alertView = li.inflate(R.layout.insert_joke_alert, null);
		final EditText statment = (EditText) alertView
				.findViewById(R.id.et_statement);
		final Spinner spinnerLanguage = (Spinner) alertView
				.findViewById(R.id.sp_idioma);
		loadAdapterLanguage(spinnerLanguage);
		adb.setView(alertView);
		adb.setTitle(getString(R.string.joke_alert_name));
		adb.setNegativeButton(getString(R.string.cancel), null);
		Log.i("LogsAndroid",
				"INFO :" + category.getInt(0) + " " + language.getInt(0));
		adb.setPositiveButton(getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						String nom = statment.getText().toString();
						// LOAD id laguage
						if (!nom.isEmpty()) {

							long test = db.insertJoke(nom, category.getInt(0),
									language.getInt(0));
							Log.i("LogsAndroid", "Insert correct :" + test);
							LoadSpinner();
						}
					}
				});
		adb.show();
	}

	private void addInsertRiddleFrame() {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		LayoutInflater li = LayoutInflater.from(this);
		View alertView = li.inflate(R.layout.insert_riddle_alert, null);
		final EditText question = (EditText) alertView
				.findViewById(R.id.et_question);
		final EditText answer = (EditText) alertView
				.findViewById(R.id.et_answer);
		final Spinner spinnerLanguage = (Spinner) alertView
				.findViewById(R.id.spinner1);
		loadAdapterLanguage(spinnerLanguage);
		adb.setView(alertView);
		adb.setTitle(getString(R.string.riddle_alert_name));
		adb.setNegativeButton(getString(R.string.cancel), null);
		Log.i("LogsAndroid",
				"INFO :" + category.getInt(0) + " " + language.getInt(0));
		adb.setPositiveButton(getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						String nom = question.getText().toString();
						String sanswer = answer.getText().toString();
						// LOAD id laguage
						if (!nom.isEmpty() && !sanswer.isEmpty()) {

							long test = db.insertRiddle(nom, sanswer,
									category.getInt(0), language.getInt(0));
							Log.i("LogsAndroid", "Insert correct :" + test);
							LoadSpinner();
						}
					}
				});
		adb.show();
	}

	private void addCategoryFrame() {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		LayoutInflater li = LayoutInflater.from(this);
		View alertView = li.inflate(R.layout.category_alert, null);
		final EditText nameInput = (EditText) alertView
				.findViewById(R.id.et_cat_name);
		final EditText descriptionInput = (EditText) alertView
				.findViewById(R.id.et_cat_description);

		adb.setView(alertView);
		adb.setTitle(getString(R.string.category_alert_name));
		adb.setNegativeButton(getString(R.string.cancel), null);
		adb.setPositiveButton(getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						String nom = nameInput.getText().toString();
						String description = descriptionInput.getText()
								.toString();
						if (!nom.isEmpty() && !description.isEmpty()) {
							String tableName = "JOKES_CATEGORY";
							if (op == 2) {
								tableName = "RIDDLE_CATEGORY";
							}
							long test = db.insertCategory(nom, description,
									tableName);
							db.close();
							Log.i("LogsAndroid", "Insert correct :" + test);
							LoadSpinner();
						}
					}
				});
		adb.show();
	}

	public void displayAlertDialog(final int PositionRemove) {

		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle(getString(R.string.delete_alert_name));
		adb.setMessage(getString(R.string.tv_string) + (PositionRemove + 1));
		adb.setNegativeButton(getString(R.string.cancel), null);
		adb.setPositiveButton(getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						if (op == 1)
							db.deleteFrom("jokes", jokes.getInt(0));
							
						else
							db.deleteFrom("riddle", riddles.getInt(0));
						LoadSpinner();
					}
				});
		adb.show();
		
	}

	public void displayUpdateDialog(final int PositionRemove) {

		if (op == 1) {
			// UPDATE JOKE
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			LayoutInflater li = LayoutInflater.from(this);
			View alertView = li.inflate(R.layout.insert_joke_alert, null);
			final EditText statment = (EditText) alertView
					.findViewById(R.id.et_statement);
			final Spinner spinnerLanguage = (Spinner) alertView
					.findViewById(R.id.sp_idioma);
			loadAdapterLanguage(spinnerLanguage);
			adb.setView(alertView);
			adb.setTitle(getString(R.string.update_alert_name));
			adb.setNegativeButton(getString(R.string.cancel), null);
			Log.i("LogsAndroid",
					"INFO :" + category.getInt(0) + " " + language.getInt(0));
			adb.setPositiveButton(getString(R.string.ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface,
								int i) {
							String nom = statment.getText().toString();
							// LOAD id laguage
							if (!nom.isEmpty()) {
								Log.i("LogsAndroid",
										"Joke id: " + jokes.getInt(0) + nom
												+ " cId: " + jokes.getInt(3)
												+ " lId: " + language.getInt(0)
												+ " ");
								boolean test = db.updateJoke(jokes.getInt(0), nom,
										category.getInt(0), language.getInt(0));
								Log.i("LogsAndroid", "UPDATE correct "+test);
								LoadSpinner();
							}
						}
					});
			adb.show();
		} else {
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			LayoutInflater li = LayoutInflater.from(this);
			View alertView = li.inflate(R.layout.insert_riddle_alert, null);
			final EditText question = (EditText) alertView
					.findViewById(R.id.et_question);
			final EditText answer = (EditText) alertView
					.findViewById(R.id.et_answer);
			final Spinner spinnerLanguage = (Spinner) alertView
					.findViewById(R.id.spinner1);
			loadAdapterLanguage(spinnerLanguage);
			adb.setView(alertView);
			adb.setTitle(getString(R.string.update_alert_name));
			adb.setNegativeButton(getString(R.string.cancel), null);
			Log.i("LogsAndroid",
					"INFO :" + category.getInt(0) + " " + language.getInt(0));
			adb.setPositiveButton(getString(R.string.ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface,
								int i) {
							String nom = question.getText().toString();
							String sanswer = answer.getText().toString();
							// LOAD id laguage
							if (!nom.isEmpty() && !sanswer.isEmpty()) {

								boolean test = db.updateRiddle(riddles.getInt(0), nom,
										sanswer, riddles.getInt(3),
										language.getInt(0));
								Log.i("LogsAndroid", "Update correct :" + test);
								LoadSpinner();
							}
						}
					});
			adb.show();
		}
		
	}
}
