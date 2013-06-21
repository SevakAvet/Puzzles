package com.vetrova.puzzles.payment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.vetrova.puzzles.R;
import com.vetrova.puzzles.gameutils.PaymentUtils;

public class PaymentActivity extends Activity implements OnClickListener {
	private ImageView btn;
	private Context mContext = this;
	private static final String TAG = "Android BillingService";
	private static String purchasedText = "PURCHASED";
	private static SharedPreferences sPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);

		btn = (ImageView) findViewById(R.id.btn);
		btn.setOnClickListener(this);

		startService(new Intent(mContext, BillingService.class));
		BillingHelper.setCompletedHandler(mTransactionHandler);
	}
	
	
	public Handler mTransactionHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.i(TAG, "Transaction complete");
			Log.i(TAG, "Transaction status: " + BillingHelper.latestPurchase.purchaseState);
			Log.i(TAG, "Item purchased is: " + BillingHelper.latestPurchase.productId);

			sPref = PaymentUtils.getPref();
			sPref = getPreferences(MODE_PRIVATE);

			String purchased = sPref.getString(purchasedText, "");
			
			if (purchased.equals("YES")) {
				PaymentUtils.setBoolPurchased(true);
			} else if (BillingHelper.latestPurchase.isPurchased()) {
				PaymentUtils.setBoolPurchased(true);
				
				Editor ed = sPref.edit();
				ed.putString(purchasedText, "YES");
				
				ed.commit();
			} else {
				PaymentUtils.setBoolPurchased(false);
			}
		};
	};

	@Override
	public void onClick(View v) {

		if (BillingHelper.isBillingSupported()) {
			BillingHelper.requestPurchase(mContext, "puzzle_gallery_item");
		} else {
			Log.i(TAG, "Can't purchase on this device");
			btn.setEnabled(false);
		}
	}

	@Override
	protected void onPause() {
		Log.i(TAG, "onPause())");
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		BillingHelper.stopService();
		super.onDestroy();
	}
}