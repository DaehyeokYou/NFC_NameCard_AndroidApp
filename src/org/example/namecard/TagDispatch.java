package org.example.namecard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
public class TagDispatch extends Activity{
	private TextView textView;
	private NfcAdapter mNfcAdapter; 
	Intent intent; 
	private PendingIntent mPendingIntent; 
	private IntentFilter[] mIntentFilters; 
	String userName, userMsg, userMsg2, userMsg3, userMsg4;
	ImageView view;
	public static Cursor mCursor;
	public static SQLiteDatabase mDb2 = ((MainActivity)MainActivity.mainContext).getDB2();;
	public static ArrayList<String> mArMember = new ArrayList<String>();
	public static Context cContext;
	public static String strRecord;
	
	
	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.nfc_main);
		textView = (TextView) findViewById(R.id.tv);
		view = (ImageView)findViewById(R.id.imageView);
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mNfcAdapter != null) {
			textView.setText("명함을 수신하기 위해 휴대폰 뒷면을 맞대고 가만히 계십시오.");
		} else {
			textView.setText("This phone is not NFC enabled.");
		}

		
		intent = new Intent(getApplicationContext(), TagDispatch.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		mPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
				intent, 0);
		IntentFilter ndefIntent = new IntentFilter(
				NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			ndefIntent.addDataType("*/*");
			mIntentFilters = new IntentFilter[] { ndefIntent };
		} catch (Exception e) {
			Log.e("TagDispatch", e.toString());
		}
		cContext = this;
	}
	public static ArrayList<String> getArrList2() {
		mArMember.clear();
		String strQuery = "select _id, name, number, email, workplace, rank from NameCard2";
		mCursor = mDb2.rawQuery(strQuery, null);

		for(int i=0; i < mCursor.getCount(); i++) {
			mCursor.moveToNext();
			int nId = mCursor.getInt(0);
			String strName = mCursor.getString(1);
			String number = mCursor.getString(2);
			String email = mCursor.getString(3);
			String workplace = mCursor.getString(4);
			String rank = mCursor.getString(5);
			strRecord = strName + " " + number + " " + email + " " + workplace + " " + rank;
			Log.d("tag", "Rec-" + strRecord);
			mArMember.add(strRecord);         
		}
		return mArMember;
	}
	@Override
	public void onResume() {
		super.onResume();
		mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, null);
		Log.d("onResume", "onResume");
	}

	@Override
	public void onPause(){
		super.onPause();
		mNfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	public void onNewIntent(Intent intent) {

		textView = (TextView) findViewById(R.id.tv);
		Parcelable[] rawMsgs = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		
		
		
		
		NdefMessage msg = (NdefMessage) rawMsgs[0];

		byte[] bytes = msg.getRecords()[0].getPayload();
		Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0,bytes.length);
		view.setImageBitmap(bmp);
		
		
		String strSite = new String(msg.getRecords()[2].getPayload());
		if (strSite != null && !strSite.equals("")) {
			userName = strSite.substring(strSite.indexOf("userName") + 9,
					strSite.indexOf("\n"));
			userMsg = strSite.substring(strSite.indexOf("userMsg") + 8,
					strSite.indexOf("\n", strSite.indexOf("userMsg") + 8));
			userMsg2 = strSite.substring(strSite.indexOf("userMsg2") + 9,
					strSite.indexOf("\n", strSite.indexOf("userMsg2") + 9));
			userMsg3 = strSite.substring(
					strSite.indexOf("userMsg3:") + 9,
					strSite.indexOf("\n", strSite.indexOf("userMsg3") + 9));
			userMsg4 = strSite.substring(strSite.indexOf("userMsg4") + 9,
					strSite.length());
		}

		textView.setText("이  름 : "+userName +"\n"+
						 "P.H : "+userMsg +"\n"+
						 "e-mail : " +userMsg2+"\n"+
						 "직  장 : "+userMsg3+"\n"+
						 "직  책 : " +userMsg4);
		String strQuery = "insert into NameCard2(name, number, email, workplace, rank) values ('" + userName + "', '" + userMsg + "', '" + userMsg2 + "', '" + userMsg3 + "', '" + userMsg4 + "');";
		mDb2.execSQL(strQuery);
		
		strQuery = "select _id, name, number, email, workplace, rank  from NameCard2";
		mCursor = mDb2.rawQuery(strQuery, null);
		mCursor.moveToLast();
		String strNum=mCursor.getString(2);
		SaveBitmapToFileCache(bmp, "/storage/emulated/0/NameCard/FriendsCard/", strNum+".jpg");
		setIntent(intent);
		return;
	}
	
	public void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath, String filename) {
		File file = new File(strFilePath);
		// If no folders
		if (!file.exists()) {
			file.mkdirs();
		}
		File fileCacheItem = new File(strFilePath + filename);
		OutputStream out = null;
		try {
			fileCacheItem.createNewFile();
			out = new FileOutputStream(fileCacheItem);
			bitmap.compress(CompressFormat.JPEG, 25, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}