package org.example.namecard;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SaveNamecardActivity extends Activity {
	ArrayList<String> mArMember = new ArrayList<String>();
	TextView tv;
	ListView mListMember;
	public static Context qContext;
	public static String strItem;
	public static Cursor mCursor;
	public static SQLiteDatabase mDb2 = MainActivity.getDB2();;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.save_namecard);
		
		tv = (TextView)findViewById(R.id.textView1);
		initListView();
		mListMember.setOnItemClickListener(mItemClickListener);
		qContext = this;
		
	}
	 AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {


             @SuppressWarnings("unchecked")


             public void onItemClick(AdapterView parent, View view, int position, long id) {


                        strItem = mArMember.get(position);
                        Intent intent = new Intent(SaveNamecardActivity.this, FriendsCard.class);
                        startActivity(intent);
                        finish();
             }
   };

	public static String getStr()
	{
		return strItem;
	}
	
	 public void initListView() {
		 
		 
         mArMember = new ArrayList<String>();
         
         mArMember=TagDispatch.getArrList2();
         

         ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,


                    android.R.layout.simple_list_item_1,mArMember);


         mListMember= (ListView)findViewById(R.id.listMember);
         mListMember.setAdapter(adapter);
}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.save_namecard, menu);
		return true;
	}

}