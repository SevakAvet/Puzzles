package com.vetrova.puzzles.gameutils;

import android.content.SharedPreferences;

public class PaymentUtils {
	private static SharedPreferences sPref;
	private static boolean purchased;

	public static SharedPreferences getPref() {
		return sPref;
	}

	public static void setBoolPurchased(boolean purchasedBool) {
		purchased = purchasedBool;
	}

	public static boolean extraFeaturesHaveBeenPaid() {
		return purchased;
	}

}
