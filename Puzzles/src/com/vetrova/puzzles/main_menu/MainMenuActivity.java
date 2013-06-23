package com.vetrova.puzzles.main_menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;

import com.vetrova.puzzles.R;
import com.vetrova.puzzles.gameutils.GlobalStorage;
import com.vetrova.puzzles.gameutils.MenuStatesUtils;
import com.vetrova.puzzles.gameutils.PaymentUtils;

public class MainMenuActivity extends Activity {
	
	private static final int[] itemsIds = new int[] {
			R.id.playItem, R.id.continueGameItem, R.id.ratingItem, R.id.paidItem
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu_activity);
		setListenersForMenuItems();
	}

	private void setListenersForMenuItems() {
		OnClickListener listener = new OnMainMenuItemClickListener(this);
		for (int i = 0; i < itemsIds.length; ++i) {
			findViewById(itemsIds[i]).setOnClickListener(listener);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateItemsClickability();
	}

	public void updateItemsClickability() {
		MenuStatesUtils.setState(findViewById(R.id.continueGameItem), loadGameClickable());
		MenuStatesUtils.setState(findViewById(R.id.paidItem), paidClickable());
	}

	private boolean loadGameClickable() {
		return GlobalStorage.existSavedGame();
	}
	
	private boolean paidClickable() {
		return !PaymentUtils.extraFeaturesHaveBeenPaid();
	}
}
