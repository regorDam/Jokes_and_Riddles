package dam.m06.jokes_and_riddles;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import dam.m06.jokes_and_riddles_mysql.JRDataBase;

/**
 * █║▌│ █│║▌ ║││█║▌ │║║█║ 
 * Author : Regør [★] 
 * Who in Black Byte 
 * Program Jokes & Riddles - SQLite V.1
 */

public class MainActivity extends Activity implements OnClickListener {

	private JRDataBase db;
	// private MyDatabase db;
	private Button btn_jokes;
	private Button btn_riddles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn_jokes = (Button) findViewById(R.id.btn_jokes);
		btn_riddles = (Button) findViewById(R.id.btn_riddles);

		btn_jokes.setOnClickListener(this);
		btn_riddles.setOnClickListener(this);
		try {
			// bd = new cSQLite(this);

			db = new JRDataBase(this);
			db.openDatabase();
			// db = new MyDatabase(this);
			// jokes = db.getJokes();

		} catch (Exception ex) {
			Log.e("LogsAndroid", ex.toString());
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.action_settings:
			//delete all????
			break;
		case R.id.action_addCategory:
			//Mostrar frame per permetre fer inserts
			addCategoryFrame();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void addCategoryFrame(){
		AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
		LayoutInflater li = LayoutInflater.from(MainActivity.this);
		View alertView = li.inflate(R.layout.category_alert, null);
		final EditText nameInput = (EditText) alertView
				.findViewById(R.id.et_cat_name);
		final EditText descriptionInput = (EditText) alertView
				.findViewById(R.id.et_cat_description);

		adb.setView(alertView);
		adb.setTitle(getString(R.string.category_alert_name));
		adb.setNegativeButton(getString(R.string.cancel), null);
		adb.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				String nom = nameInput.getText().toString();
				String description = descriptionInput.getText().toString();
				if (!nom.isEmpty() && !description.isEmpty()){
					long test = db.insertCategory(nom, description);
					db.close();
					Log.i("LogsAndroid","Insert correct :"+test);
				}
			}
		});
		adb.show();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this,JokeAndRiddle.class);
		switch (v.getId()) {
		case R.id.btn_jokes:
			intent.putExtra("KEY", 1);
			break;
		case R.id.btn_riddles:
			intent.putExtra("KEY", 2);
		default:
			break;
		}
		startActivity(intent);
		
	}
}
