package com.vetrova.puzzles.main_menu;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.vetrova.puzzles.R;
import com.vetrova.puzzles.game.FullImageActivity;
import com.vetrova.puzzles.newgame_menu.NewGameMenuActivity;
import com.vetrova.puzzles.payment.PaymentActivity;
import com.vetrova.puzzles.rating.RatingActivity;


public class OnMainMenuItemClickListener implements OnClickListener {
	private MainMenuActivity activity;
	private Animation anim;
	
	public OnMainMenuItemClickListener(MainMenuActivity activity) {
		this.activity = activity;
		anim = AnimationUtils.loadAnimation(activity, R.anim.click_anim);
	}

	@Override
	public void onClick(View v) {
		v.startAnimation(anim);
		switch (v.getId()) {
		case R.id.playItem:
			onPlayClick();
			break;
		
		case R.id.loadItem:
			onContinueGameClick();
			break;

		case R.id.ratingItem:
			onRatingClick();
			break;
		
		case R.id.paidItem:
			onPaidClick();
			break;
		}
	}
	
	private void onPlayClick() {
		startActivity(NewGameMenuActivity.class);
	}
	
	private void startActivity(Class<?> activityClass) {
		Intent intent = new Intent(activity, activityClass);
		activity.startActivity(intent);
	}

	private void onContinueGameClick() {
		startActivity(FullImageActivity.class);
	}
	
	private void onRatingClick() {
		Intent intent = new Intent(activity, RatingActivity.class);
		intent.putExtra(RatingActivity.MODE, RatingActivity.SHOW_ONLY_RATING_MODE);
		activity.startActivity(intent);
	}

	private void onPaidClick() {
		Intent intent = new Intent(activity, PaymentActivity.class);
		activity.startActivity(intent);
	}
}
