package org.example.namecard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public static DbHelper mDbHelper;
	public static DbHelper2 mDbHelper2;
	public static SQLiteDatabase mDb;
	public static SQLiteDatabase mDb2;
	public static Context mainContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDbHelper = new DbHelper(this);
		mDbHelper2 = new DbHelper2(this);
		
		mDb = mDbHelper.getWritableDatabase();
		mDb2 = mDbHelper2.getWritableDatabase();
		mainContext = this;
	}
	
	class DbHelper extends SQLiteOpenHelper
	{
		public DbHelper(Context context)
		{
			super(context, "NameCard.db", null, 1);
		}

		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL("create table NameCard ( _id integer PRIMARY KEY autoincrement, " +
					"name TEXT, number TEXT, email TEXT, workplace TEXT, rank TEXT);");

		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("drop table if exists NameCard");
			onCreate(db);
		}
	}
	class DbHelper2 extends SQLiteOpenHelper
	{
		public DbHelper2(Context context)
		{
			super(context, "NameCard2.db", null, 1);
		}

		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL("create table NameCard2 ( _id integer PRIMARY KEY autoincrement, " +
					"name TEXT, number TEXT, email TEXT, workplace TEXT, rank TEXT);");
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("drop table if exists NameCard2");
			onCreate(db);
		}
	}
	

	public static SQLiteDatabase getDB()
	{
		return mDb;
	}
	public static SQLiteDatabase getDB2()
	{
		return mDb2;
	}
	
	
	
	public void go (View v) {
		Intent myintent = new Intent(this, MyCardActivity.class);
		startActivity(myintent);
	}
	public void go1 (View v) {
		Intent myintent = new Intent(this, SaveNamecardActivity.class);
		startActivity(myintent);
	}
	public void del1 (View v) {
		mDb.execSQL("delete from NameCard");
	}
	public void del2 (View v) {
		mDb2.execSQL("delete from NameCard2");
	}
	public void receive(View v)
	{
		startActivity(new Intent(this, TagDispatch.class)); 
	}
	public void exchange (View v) {
		startActivity(new Intent(this, ChoiseSendActivity.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
