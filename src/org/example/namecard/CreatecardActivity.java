package org.example.namecard;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CreatecardActivity extends Activity {

	static final int CALL_CAMERA = 0;
	static final int CALL_GALLERY = 1;
	static Bitmap bm = null;
	ImageView mAddimage;
	Button mSaveBtn;
	Button mCancelBtn;

	EditText mEditName;
	EditText mEditPhone;
	EditText mEditEmail;
	EditText mEditWorkplace;
	EditText mEditRank;
	int mSellIndex = -1;
	Drawable draw;

	public static Cursor mCursor;
	public static SQLiteDatabase mDb = MainActivity.getDB();;
	public static ArrayList<String> mArMember = new ArrayList<String>();
	public static Context mContext;
	public static String strRecord;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createcard);

		mAddimage = (ImageView)this.findViewById(R.id.addimage);
		mAddimage.setOnClickListener(listener);
		mSaveBtn = (Button)this.findViewById(R.id.SaveBtn);
		mSaveBtn.setOnClickListener(listener);
		mCancelBtn = (Button)this.findViewById(R.id.CancelBtn);
		mCancelBtn.setOnClickListener(listener);

		
		mEditName = (EditText)findViewById(R.id.editText1);
		mEditPhone = (EditText)findViewById(R.id.editText2);
		mEditEmail = (EditText)findViewById(R.id.editText3);
		mEditWorkplace = (EditText)findViewById(R.id.editText4);
		mEditRank = (EditText)findViewById(R.id.editText5);
		mContext = this;
		
		draw = getResources().getDrawable(R.drawable.standard);
		bm = Bitmap.createBitmap(250,250,Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bm);
		draw.setBounds(0,0,250,250);
		draw.draw(canvas);
		
	}

	public static ArrayList<String> getArrList() {
		mArMember.clear();
		String strQuery = "select _id, name, number, email, workplace, rank from NameCard";
		mCursor = mDb.rawQuery(strQuery, null);

		for(int i=0; i < mCursor.getCount(); i++) {
			mCursor.moveToNext();
			int nId = mCursor.getInt(0);
			String strName = mCursor.getString(1);
			String number = mCursor.getString(2);
			String email = mCursor.getString(3);
			String workplace = mCursor.getString(4);
			String rank = mCursor.getString(5);
			strRecord = nId+ " "+ strName + " " + number + " " + email + " " + workplace + " " + rank;
			mArMember.add(strRecord);         
		}
		mCursor.close();
		return mArMember;
	}

	public int onSaveBtn()
	{
		String strId;
		String strName = mEditName.getText().toString();
		String strPhone = mEditPhone.getText().toString();
		String strEmail = mEditEmail.getText().toString();
		String strworkplace = mEditWorkplace.getText().toString();
		String strrank = mEditRank.getText().toString();
		
		if(strName.length() == 0 || strPhone.length() == 0 ||strEmail.length() == 0 || strworkplace.length() == 0 || strrank.length() == 0)
		{
			Intent myintent = new Intent(this, MyCardActivity.class);
			startActivity(myintent);
			finish();
			return 0;
		}
		//
		String strQuery = "insert into NameCard(name, number, email, workplace, rank) values ('" + strName + "', '" + strPhone + "', '" + strEmail + "', '" + strworkplace + "', '" + strrank + "');";
		mDb.execSQL(strQuery);
		strQuery = "select _id, name, number, email, workplace, rank from NameCard";
		mCursor = mDb.rawQuery(strQuery, null);
		mCursor.moveToLast();
		strId=Integer.toString(mCursor.getInt(0));
		SaveBitmapToFileCache(bm, "/storage/emulated/0/NameCard/", strId+".jpg");
		mCursor.close();
		Intent myintent = new Intent(this, MyCardActivity.class);
		startActivity(myintent);
		finish();
		return 1;
	}

	public void onCancelBtn()
	{
		Intent myintent = new Intent(this, MyCardActivity.class);
		startActivity(myintent);
		finish();
	}

	OnClickListener listener = new View.OnClickListener(){
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
			case R.id.addimage:
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(mAddimage.getContext());

				builder.setTitle("사진선택");
				builder.setNeutralButton("사진촬영", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(intent, CALL_CAMERA);
					}
				});
				builder.setNegativeButton("갤러리", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI); 
						startActivityForResult(intent, CALL_GALLERY);
					}
				});
				builder.show();
				break;
			}
			case R.id.SaveBtn:
				int check = onSaveBtn();
				if(check == 1)
					Toast.makeText(getApplication(),  "저장되었습니다.",Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(getApplication(),  "입력하지 않는 속성이 있어 저장에 실패했습니다.",Toast.LENGTH_SHORT).show();
				break;
			case R.id.CancelBtn:
				onCancelBtn();
				break;
			}
		}
	};


	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CALL_CAMERA:
				bm = (Bitmap) data.getExtras().get("data");
				bm = Bitmap.createScaledBitmap(bm, 400, 400, true);
				mAddimage.setImageBitmap(bm);
				break;
			case CALL_GALLERY:
				try {
					Uri uri = data.getData();
					bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
					bm = Bitmap.createScaledBitmap(bm, 400, 400, true);
					mAddimage.setImageBitmap(bm);

				} catch (Exception e) { e.printStackTrace(); }
				break;
			}
		}
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

	@Override
	public void onBackPressed() {
		Intent myintent = new Intent(CreatecardActivity.this, MyCardActivity.class);
		startActivity(myintent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.createcard, menu);
		return true;
	}

}
