package com.vetrova.puzzles.gameutils;


public class PaymentUtils {
	
	public static void setBoolPurchased(boolean purchasedBool) {
		DeviceSaverLoader.saveBoolean("purchased", purchasedBool);
	}

	public static boolean extraFeaturesHaveBeenPaid() {
		return DeviceSaverLoader.loadBoolean("purchased");
	}

}
