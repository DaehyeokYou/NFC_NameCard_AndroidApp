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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChoiseSendActivity extends Activity {
	public static Bitmap bm;
	ImageView nameCard = null;
	TextView tv1;
	TextView tv2;
	TextView tv3;
	TextView tv4;
	TextView tv5;
	ArrayList<String> mArMember;
	Intent intent;
	int lengh;
	public static Context aContext;
	public static String Name;
	public static String Phone;
	public static String Email;
	public static String Workplace;
	public static String Rank;
	HorizontalScrollView scrView;
	SQLiteDatabase mDb = MainActivity.getDB();
	Cursor mCursor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choise_send);
		nameCard = (ImageView)this.findViewById(R.id.name_card);

		tv1 = (TextView)findViewById(R.id.workplace);
		tv2 = (TextView)findViewById(R.id.name);
		tv3 = (TextView)findViewById(R.id.rank);
		tv4 = (TextView)findViewById(R.id.phone);
		tv5 = (TextView)findViewById(R.id.email);
		
		scrView = (HorizontalScrollView)findViewById(R.id.horizontalScrollView1);
		String strQuery = "select _id, name, number, email, workplace, rank from NameCard";
		mCursor = mDb.rawQuery(strQuery,null);
		
		makeimageview();
		
		if( !(mCursor.moveToFirst())){
			Drawable drawable = getResources().getDrawable(R.drawable.standard);
			nameCard.setImageDrawable(drawable);
			tv1.setText("");
			tv2.setText("  저장된 명함이 없습니다.");
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
		}
		
		Button sendBtn = (Button)findViewById(R.id.sendbutton);
		sendBtn.setOnClickListener(new OnClickListener(){
			// @Override
			public void onClick(View arg0) {
				if( !(mCursor.moveToFirst())){
					Toast.makeText(getApplication(),  "명함을 선택해 주세요.",Toast.LENGTH_SHORT).show();
					return;
				}
				finish();
				startActivity(new Intent(ChoiseSendActivity.this, BeamData.class)); 
			}
		});
		aContext = this;
	}
	public static String getName()
	{
		return Name;
	}
	public static String getNumber()
	{
		return Phone;
	}
	public static String getEmail()
	{
		return Email;
	}
	public static Bitmap getBmp()
	{
		return bm;
	}
	public static String getWork()
	{
		return Workplace;
	}
	public static String getRank()
	{
		return Rank;
	}
	
	public void makeimageview(){
		LinearLayout h = (LinearLayout)findViewById(R.id.linearLayout1);
		mArMember = CreatecardActivity.getArrList();
		ImageView v2[] = new ImageView[mArMember.size()];

		lengh = v2.length;

		for(int i=0;i<v2.length;i++){
			String Phone = "";
			Phone = initListView(i);
			bm = BitmapFactory.decodeFile("/storage/emulated/0/NameCard/"+Phone+".jpg");
			bm = Bitmap.createScaledBitmap(bm,350,350,true);
			v2[i] = new ImageView(this);
			v2[i].setId(i);
			v2[i].setMaxWidth(160);
			v2[i].setMaxHeight(100);
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
			String Phone = "";
			Phone = initListView(arg0.getId());
			bm = BitmapFactory.decodeFile("/storage/emulated/0/NameCard/"+Phone+".jpg");
			bm = Bitmap.createScaledBitmap(bm,350,350,true);
			nameCard.setImageBitmap(bm);
		}
	};
	public String initListView(int id) {
		mArMember = new ArrayList<String>(); 
		mArMember = ((CreatecardActivity)CreatecardActivity.mContext).getArrList();

		Name = mArMember.get(id).split(" ")[1];
		Phone = mArMember.get(id).split(" ")[2];
		Email = mArMember.get(id).split(" ")[3];
		Workplace = mArMember.get(id).split(" ")[4];
		Rank = mArMember.get(id).split(" ")[5];
		
		tv1.setText(Workplace);
		tv2.setText(Name);
		tv3.setText(Rank);
		tv4.setText("P.H     : " + Phone);
		tv5.setText("E-mail : " + Email);
		return mArMember.get(id).split(" ")[0];
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choise_send, menu);
		return true;
	}
}
