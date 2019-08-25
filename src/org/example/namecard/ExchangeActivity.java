package org.example.namecard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ExchangeActivity extends Activity {
	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.send);

		Button btag = (Button)findViewById(R.id.buttontag);
		btag.setOnClickListener(new OnClickListener(){
			// @Override
			public void onClick(View arg0) {
				finish();
				startActivity(new Intent(ExchangeActivity.this, TagDispatch.class)); 
			}
		});

		Button bbeam = (Button)findViewById(R.id.buttonbeam);
		bbeam.setOnClickListener(new OnClickListener(){
			// @Override
			public void onClick(View arg0) {
				finish();
				startActivity(new Intent(ExchangeActivity.this, ChoiseSendActivity.class)); 
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

}
