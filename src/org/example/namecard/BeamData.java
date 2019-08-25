package org.example.namecard;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

//Activity 로 만들되, CreateNdefMessageCallback 과 OnNdefPushCompleteCallback 인터페이스를 추가해줍니다.
public class BeamData extends Activity implements CreateNdefMessageCallback,OnNdefPushCompleteCallback {
	private static final int MESSAGE_SENT = 1; //추후 Handler 메시지에 사용
	private NfcAdapter mNfcAdapter; //NfcAdapter 를 선언
	private TextView mTextView;
	Context context;
	String userName, userMsg, userMsg2, userMsg3, userMsg4;
	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);  
		// Nfc 로 전송할 메시지 선언​
		userName = ((ChoiseSendActivity)ChoiseSendActivity.aContext).getName();
		userMsg = ((ChoiseSendActivity)ChoiseSendActivity.aContext).getNumber();
		userMsg2 = ((ChoiseSendActivity)ChoiseSendActivity.aContext).getEmail();
		userMsg3 = ((ChoiseSendActivity)ChoiseSendActivity.aContext).getWork();
		userMsg4 = ((ChoiseSendActivity)ChoiseSendActivity.aContext).getRank();
		setContentView(R.layout.nfc_main);
		mTextView = (TextView) findViewById(R.id.tv);


		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		
		if (mNfcAdapter != null) {
			mTextView.setText("명함을 전송하기 위해 휴대폰 뒷면을 맞대고 화면을 클릭해 주십시오.");
		} else {
			mTextView.setText("This phone is not NFC enabled.");
		}

		mNfcAdapter.setNdefPushMessageCallback(this, this);
		mNfcAdapter.setOnNdefPushCompleteCallback(this, this);

	}
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		
		Bitmap img =((ChoiseSendActivity)ChoiseSendActivity.aContext).getBmp();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		img.compress(Bitmap.CompressFormat.JPEG, 25, stream);
		byte[] byteArray = stream.toByteArray();
		String text = ("userName:" + userName +"\n"+"userMsg:"+ userMsg + "\n"+"userMsg2:"+userMsg2+ "\n"+"userMsg3:"+userMsg3+ "\n"+"userMsg4:"+userMsg4);
		
		NdefMessage msg = new NdefMessage(new NdefRecord[] {
				createImgRecord("application/com.chapter9", byteArray), NdefRecord.createApplicationRecord("com.chapter9"),
				createTextRecord("application/com.example.test", text.getBytes())});
		return msg;
	}
	
	
	public NdefRecord createImgRecord(String mimeType, byte[]payload) {
		byte[] mimeBytes = mimeType.getBytes(Charset.forName("USASCII"));
		NdefRecord mimeRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,mimeBytes, new byte[0], payload);
		return mimeRecord;
		}
	public NdefRecord createTextRecord(String mimeType, byte[] payload) {
		byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
		NdefRecord mimeRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
				mimeBytes, new byte[0], payload);
		Log.d("createMimeRecord", "createMimeRecord");
		return mimeRecord;
	}
	
	@Override
	public void onNdefPushComplete(NfcEvent arg0) {
		Log.d("onNdefPushComplete", "onNdefPushComplete");
		mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
	}
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_SENT:
				Toast.makeText(getApplicationContext(), "send message!!!", 1000)
				.show();
				break;
			}
		}
	};
}