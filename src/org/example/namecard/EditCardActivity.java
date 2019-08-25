package org.example.namecard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EditCardActivity extends Activity {

	public static SQLiteDatabase mDb = MainActivity.getDB();
	static final int CALL_CAMERA = 0;
	static final int CALL_GALLERY = 1;
	
	Bitmap bm = null;
	ImageView iv = null;
	Button mSaveBtn;
	Button mDeleteBtn;
	String id;
	
	EditText et1;
	EditText et2;
	EditText et3;
	EditText et4;
	EditText et5;
	
	ArrayList<String> mArMember;
	Cursor mCursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editcard);
		
		iv = (ImageView)this.findViewById(R.id.namecard);
		mSaveBtn = (Button)this.findViewById(R.id.SaveBtn);
		mDeleteBtn = (Button)this.findViewById(R.id.DeleteBtn);
		
		iv.setOnClickListener(listener);
		mSaveBtn.setOnClickListener(listener);
		mDeleteBtn.setOnClickListener(listener);


		et1 = (EditText)findViewById(R.id.editText1);
		et2 = (EditText)findViewById(R.id.editText2);
		et3 = (EditText)findViewById(R.id.editText3);
		et4 = (EditText)findViewById(R.id.editText4);
		et5 = (EditText)findViewById(R.id.editText5);
		
		id = MyCardActivity.Id;
		
		initListView1( Integer.parseInt(id));
	}
	
	public void initListView1(int id) {
		String strQuery = "select _id, name, number, email, workplace, rank from NameCard where _id = " + id ;
		//String strQuery = "select _id, name, number, email from NameCard";
		mCursor = mDb.rawQuery(strQuery,null);
		mCursor.moveToLast();

		String imageName = Integer.toString(id);
		bm = BitmapFactory.decodeFile("/storage/emulated/0/NameCard/"+imageName+".jpg");
		bm = Bitmap.createScaledBitmap(bm,400,400,true);
		iv.setImageBitmap(bm);
		
		et1.setText(mCursor.getString(1));
		et2.setText(mCursor.getString(2));
		et3.setText(mCursor.getString(3));
		et4.setText(mCursor.getString(4));
		et5.setText(mCursor.getString(5));
	}
	
	OnClickListener listener = new View.OnClickListener(){
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
			case R.id.namecard:
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(iv.getContext());

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
				onSaveBtn();
				Toast.makeText(getApplication(),  "저장되었습니다.",Toast.LENGTH_SHORT).show();
				break;
			case R.id.DeleteBtn:
				onDeleteBtn();
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
				iv.setImageBitmap(bm);
				break;
			case CALL_GALLERY:
				try {
					Uri uri = data.getData();
					bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
					bm = Bitmap.createScaledBitmap(bm, 400, 400, true);
					iv.setImageBitmap(bm);

				} catch (Exception e) { e.printStackTrace(); }
				break;
			}
		}
	}
	
	public void onSaveBtn()
	{
		String strName = et1.getText().toString();
		String strPhone = et2.getText().toString();
		String strEmail = et3.getText().toString();
		String strWorkplace = et4.getText().toString();
		String strRank = et5.getText().toString();

		String strQuery = "update NameCard set name = '" + strName +"', number = '" + strPhone + "', email = '" + strEmail + "', workplace = '" + strWorkplace + "', rank = '" + strRank +"' where _id = '" + id + "';";
		mDb.execSQL(strQuery);
		
		String imageName = id;
		SaveBitmapToFileCache(bm, "/storage/emulated/0/NameCard/", imageName+".jpg");
		Intent myintent = new Intent(this, MyCardActivity.class);
		startActivity(myintent);
		finish();
	}
	
	public void onDeleteBtn()
	{
		String imageName = id;
		File file = new File("/storage/emulated/0/NameCard/"+imageName+".jpg");
		file.delete();
		
		String strQuery = "delete from NameCard where _id = '"+ id + "';";
		mDb.execSQL(strQuery);
		
		Intent myintent = new Intent(this, MyCardActivity.class);
		startActivity(myintent);
		finish();
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
		Intent myintent = new Intent(this, MyCardActivity.class);
		startActivity(myintent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.editcard, menu);
		return true;
	}

}
