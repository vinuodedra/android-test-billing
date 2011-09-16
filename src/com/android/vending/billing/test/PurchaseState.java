package com.android.vending.billing.test;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class PurchaseState {

	private long nonce;
	private PurchaseStateOrder order;
	
	public long getNonce() {
		return nonce;
	}

	public void setNonce(long nonce) {
		this.nonce = nonce;
	}

	public PurchaseStateOrder getOrder() {
		return order;
	}

	public void setOrder(PurchaseStateOrder order) {
		this.order = order;
	}

	public static PurchaseState fromJson(String json) throws JSONException {
		PurchaseState purchaseState = new PurchaseState();
		JSONTokener jsonTokener = new JSONTokener(json);
		JSONObject jsonPurchaseState = (JSONObject)jsonTokener.nextValue();
		purchaseState.setNonce(jsonPurchaseState.getLong("nonce"));

		JSONObject jsonOrder = (JSONObject)jsonPurchaseState.getJSONObject("orders");
		PurchaseStateOrder order = PurchaseStateOrder.fromJsonObject(jsonOrder);
		purchaseState.setOrder(order);
		
		return purchaseState;
	}
	
	public String toJson() {
		JSONObject jsonPurchaseState = new JSONObject();
		try {
			jsonPurchaseState.put("nonce", getNonce());
			jsonPurchaseState.put("orders", getOrder().toJsonObject());
			return jsonPurchaseState.toString(4);
		} catch (JSONException ex) {
			return "null";
		}
	}
}
