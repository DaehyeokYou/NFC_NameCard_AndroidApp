package org.example.namecard;
import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
public class FriendsCard extends Activity {
	ImageView nameCard = null;
	TextView tv1;
	TextView tv2;
	TextView tv3;
	TextView tv4;
	TextView tv5;
	Bitmap bm;
	String phoneNum;
	Button btn;
	public static SQLiteDatabase mDb2 = ((MainActivity)MainActivity.mainContext).getDB2();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends_card);
		nameCard = (ImageView)this.findViewById(R.id.name_card);
		tv1 = (TextView)findViewById(R.id.workplace);
		tv2 = (TextView)findViewById(R.id.name);
		tv3 = (TextView)findViewById(R.id.rank);
		tv4 = (TextView)findViewById(R.id.phone);
		tv5 = (TextView)findViewById(R.id.email);
		
		makeimageview();
		btn = (Button)findViewById(R.id.button1);
		btn.setOnClickListener(listener);
	} 

	public void makeimageview(){
		String str = ((SaveNamecardActivity)SaveNamecardActivity.qContext).getStr();
		phoneNum = str.split(" ")[1];
		tv1.setText(str.split(" ")[3]);
		tv2.setText(str.split(" ")[0]);
		tv3.setText(str.split(" ")[4]);
		tv4.setText("P.H     : " + str.split(" ")[1]);
		tv5.setText("E-mail : " + str.split(" ")[2]);

		bm = BitmapFactory.decodeFile("/storage/emulated/0/NameCard/FriendsCard/"+phoneNum+".jpg");
		bm = Bitmap.createScaledBitmap(bm, 350, 350, true);
		nameCard.setImageBitmap(bm);
	}

	OnClickListener listener = new View.OnClickListener(){
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
			case R.id.button1:
				onDelBtn();
				break;
			}
		}
	};
	public void onDelBtn()
	{
		File file = new File("/storage/emulated/0/NameCard/FriendsCard/"+phoneNum+".jpg");
		file.delete();

		String strQuery = "delete from NameCard2 where number = '"+ phoneNum + "';";
		mDb2.execSQL(strQuery);

		Intent myintent = new Intent(this, SaveNamecardActivity.class);
		startActivity(myintent);
		finish();
	}
	public void call(View v)
	 {
	  Intent myintent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phoneNum) );
	  startActivity(myintent);
	  
	 }
	 public void sms(View v)
	 {
	  Intent myintent = new Intent(Intent.ACTION_VIEW);
	  myintent.putExtra("address", phoneNum);
	  myintent.putExtra("sms_body","");
	  myintent.setType("vnd.android-dir/mms-sms");
	  startActivity(myintent);
	 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friends_card, menu);
		return true;
	}
}