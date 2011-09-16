package com.android.vending.billing.test;

public class PurchaseState {
	public static final int STATE_PURCHASED = 0;
	public static final int STATE_CANCELED = 1;
	public static final int STATE_REFUNDED = 2;
	
	long nonce;
	String notificationId;
	String orderId;
	String packageName;
	String productId;
	long purchaseTime;
	int purchaseState;
	String developerPayload;
	
	public static PurchaseState fromJson(String json) {
		return null;
	}
	
	public String toJson() {
		return null;
	}
}
