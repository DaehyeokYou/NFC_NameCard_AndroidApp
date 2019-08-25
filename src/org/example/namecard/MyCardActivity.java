package org.example.namecard;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyCardActivity extends Activity {

	public static Context myContext;
	public static String Id;

	static Bitmap bm = null;
	ImageView nameCard = null;
	TextView tv1;
	TextView tv2;
	TextView tv3;
	TextView tv4;
	TextView tv5;
	ArrayList<String> mArMember;
	HorizontalScrollView scrView;
	SQLiteDatabase mDb = MainActivity.getDB();
	Cursor mCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_card);
		nameCard = (ImageView)this.findViewById(R.id.name_card);

		tv1 = (TextView)findViewById(R.id.workplace);
		tv2 = (TextView)findViewById(R.id.name);
		tv3 = (TextView)findViewById(R.id.rank);
		tv4 = (TextView)findViewById(R.id.phone);
		tv5 = (TextView)findViewById(R.id.email);
		

		String strQuery = "select _id, name, number, email, workplace, rank from NameCard";
		mCursor = mDb.rawQuery(strQuery,null);

		makeimageview();
		scrView = (HorizontalScrollView)findViewById(R.id.horizontalScrollView1);
		if( !(mCursor.moveToFirst())){
			Drawable drawable = getResources().getDrawable(R.drawable.standard);
			nameCard.setImageDrawable(drawable);
			tv1.setText("명함을 추가해 주세요");
			tv2.setText("");
			tv3.setText("");
			scrView.setVisibility(View.INVISIBLE);
		}
		else{
			scrView.setVisibility(View.VISIBLE);
			mCursor.moveToFirst();
			tv1.setText(mCursor.getString(4));
			tv2.setText(mCursor.getString(1));
			tv3.setText(mCursor.getString(5));
			tv4.setText("P.H     : " + mCursor.getString(2));
			tv5.setText("E-mail : " + mCursor.getString(3));
  
			bm = BitmapFactory.decodeFile("/storage/emulated/0/NameCard/"+ mCursor.getString(0) +".jpg");
			bm = Bitmap.createScaledBitmap(bm,350,350,true);
			nameCard.setImageBitmap(bm);
			Id=mCursor.getString(0);
		}
	}

	public void makeimageview(){
		LinearLayout h = (LinearLayout)findViewById(R.id.linearLayout1);
		mArMember = CreatecardActivity.getArrList();
		ImageView v2[] = new ImageView[mArMember.size()];

		for(int i=0;i<v2.length;i++){
			String Phone = "";
			Phone = initListView(i);
			bm = BitmapFactory.decodeFile("/storage/emulated/0/NameCard/"+Phone+".jpg");
			bm = Bitmap.createScaledBitmap(bm,350,350,true);
			v2[i] = new ImageView(this);
			v2[i].setId(i);
			v2[i].setLeft(5);
			v2[i].setRight(5);
			v2[i].setImageBitmap(bm);
			h.addView(v2[i]);			
			v2[i].setOnClickListener(listener);
		}
	}

	View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String imageid = "";
			imageid = initListView(arg0.getId());
			bm = BitmapFactory.decodeFile("/storage/emulated/0/NameCard/"+imageid+".jpg");
			bm = Bitmap.createScaledBitmap(bm,350,350,true);
			nameCard.setImageBitmap(bm);
			Id=imageid;
		}
	};
	public String initListView(int id) {
		mArMember = new ArrayList<String>(); 
		mArMember = CreatecardActivity.getArrList();

		String Name = mArMember.get(id).split(" ")[1];
		String Phone = mArMember.get(id).split(" ")[2];
		String Email = mArMember.get(id).split(" ")[3];
		String Workplace = mArMember.get(id).split(" ")[4];
		String Rank = mArMember.get(id).split(" ")[5];

		tv1.setText(Workplace);
		tv2.setText(Name);
		tv3.setText(Rank);
		tv4.setText("P.H     : " + Phone);
		tv5.setText("E-mail : " + Email);
		
		return mArMember.get(id).split(" ")[0];
	}

	public void go (View v) {
		Intent myintent = new Intent(this, CreatecardActivity.class);
		startActivity(myintent);
		finish();
	}
	public void edit (View v) {
		Intent myintent = new Intent(this, EditCardActivity.class);
		startActivity(myintent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_my_card, menu);
		return true;
	}


}
