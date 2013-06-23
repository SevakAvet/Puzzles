package com.vetrova.puzzles.payment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.vetrova.puzzles.R;
import com.vetrova.puzzles.gameutils.PaymentUtils;

public class PaymentActivity extends Activity implements OnClickListener {

	private class TryPurchaseTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... arg0) {
			try {
				return tryPurchase();
			} catch (Exception e) {
				return false;
			}
		}
		
		private Boolean tryPurchase() {
			if (BillingHelper.isBillingSupported()) {
				BillingHelper.requestPurchase(mContext, "puzzle_gallery_item");
				return true;
			} else {
				return false;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean canPurchase) {
			if (!canPurchase && activityNotDestroyed) {
				notifyThatCannotPurchase();
				PaymentActivity.this.finish();
			}
		}
		
		private void notifyThatCannotPurchase() {
			if (activityNotDestroyed) {
				Toast.makeText(PaymentActivity.this,
					"Can't purchase on this device", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private static final String TAG = "Android BillingService";
	private static String purchasedText = "PURCHASED";
	private static SharedPreferences sPref;
	
	private static boolean activityNotDestroyed;

	private Context mContext = this;
	private String paymentTitle;
	private String paymentMessage;
	private String paymentYesButton;
	private String paymentNoButton;
	private TryPurchaseTask tryPurchaseTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);
		
		activityNotDestroyed = true;
		
		startService(new Intent(mContext, BillingService.class));
		BillingHelper.setCompletedHandler(mTransactionHandler);
		showDialog();
	}
	
	private void showDialog() {
		loadLocalStrings();
		new AlertDialog.Builder(this)
				.setTitle(paymentTitle)
				.setMessage(paymentMessage)
				.setPositiveButton(paymentYesButton, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						tryPurchaseTask = new TryPurchaseTask();
						tryPurchaseTask.execute();
					}
				})
				.setNegativeButton(paymentNoButton, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) { 
						dialog.dismiss();
						PaymentActivity.this.finish();
					}
				})
				.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						PaymentActivity.this.finish();
					}
				})
				.show();
	}
	
	
	private void loadLocalStrings() {
		paymentTitle = getResources().getString(R.string.payment_title);
		paymentMessage = getResources().getString(R.string.payment_message);
		paymentYesButton = getResources().getString(R.string.payment_yes_button);
		paymentNoButton = getResources().getString(R.string.payment_no_button);
	}


	public Handler mTransactionHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(PaymentActivity.this,
					"Transaction complete", Toast.LENGTH_SHORT).show();
			Log.i(TAG, "Transaction status: " + BillingHelper.latestPurchase.purchaseState);
			Log.i(TAG, "Item purchased is: " + BillingHelper.latestPurchase.productId);

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
			
			finish();
		};
	};

	@Override
	public void onClick(View v) {
		if (BillingHelper.isBillingSupported()) {
			BillingHelper.requestPurchase(mContext, "puzzle_gallery_item");
		} else {
			Toast.makeText(this, "Can't purchase on this device", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onDestroy() {
		activityNotDestroyed = false;
		BillingHelper.stopService();
		if (tryPurchaseTask != null) {
			tryPurchaseTask.cancel(true);
		}
		super.onDestroy();
	}
}