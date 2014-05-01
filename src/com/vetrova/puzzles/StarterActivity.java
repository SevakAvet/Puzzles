package com.vetrova.puzzles;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.vetrova.puzzles.gameutils.GlobalContext;
import com.vetrova.puzzles.gameutils.GlobalStorage;
import com.vetrova.puzzles.main_menu.MainMenuActivity;

public class StarterActivity extends Activity {

	private class LoadFromDeviceTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			GlobalStorage.loadFromDevice();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			onLoadedFromDevice();
		}
	}

	private class SaveToDeviceTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			GlobalStorage.saveToDevice();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			GlobalStorage.saveToDevice();
			GlobalStorage.setBitmap(null);
			onSavedToDevice();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starter);

		GlobalContext.set(this);
		new LoadFromDeviceTask().execute();
	}

	private void onLoadedFromDevice() {
		startActivityForResult(new Intent(this, MainMenuActivity.class), 123);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		new SaveToDeviceTask().execute();
	}

	private void onSavedToDevice() {
		finish();
	}
}