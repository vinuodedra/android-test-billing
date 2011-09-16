package com.android.vending.billing.test;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.vending.billing.IMarketBillingService;

public class TestMarketBillingService implements IMarketBillingService {
	enum BillingRequestType {
		CHECK_BILLING_SUPPORTED, 
		REQUEST_PURCHASE, 
		GET_PURCHASE_INFORMATION, 
		CONFIRM_NOTIFICATIONS, 
		RESTORE_TRANSACTIONS,
	}
	
	enum ResponseCode {
		RESULT_OK(0),	
		RESULT_USER_CANCELED(1),	
		RESULT_SERVICE_UNAVAILABLE(2),	
		RESULT_BILLING_UNAVAILABLE(3),	
		RESULT_ITEM_UNAVAILABLE(4),	
		RESULT_DEVELOPER_ERROR(5),	
		RESULT_ERROR(6);	
		
		private final int value;  

		ResponseCode(int value) {
	        this.value = value;
	    }

	    public int getValue() { 
	        return value; 
	    }
	    public static ResponseCode createByValue(int value) {
	    	for (ResponseCode code : values())
	            if (code.getValue() == value)
	                return code;
	        throw new Error("unknown ResponseCode value");
	    }
	}
	
	private Context context;
	
	public TestMarketBillingService(Context context) {
		this.context = context;
	}
	
	public Bundle sendBillingRequest(Bundle bundle) {
		Bundle response = processBillingRequest(bundle);
		broadcastResponseCodeIntent(response);
		return response;
	}
	
	private Bundle processBillingRequest(Bundle bundle) {
		if (!bundle.containsKey("BILLING_REQUEST"))
			throw new Error("BILLING_REQUEST is required");
		if (!bundle.containsKey("API_VERSION"))
			throw new Error("API_VERSION is required");
		if (!bundle.containsKey("PACKAGE_NAME"))
			throw new Error("PACKAGE_NAME is required");
		
		BillingRequestType billingRequestType = BillingRequestType.valueOf((String)bundle.get("BILLING_REQUEST"));
		switch (billingRequestType) {
			case CHECK_BILLING_SUPPORTED:
				return createSyncResponse(ResponseCode.RESULT_OK);
			case REQUEST_PURCHASE:
			{
				if (!bundle.containsKey("ITEM_ID"))
					throw new Error("ITEM_ID is required");

				String developerPayload = bundle.getString("DEVELOPER_PAYLOAD");
				String itemId = bundle.getString("ITEM_ID");	
				String packageName = bundle.getString("PACKAGE_NAME");
				
				Bundle response = createSyncResponse(ResponseCode.RESULT_OK);
				response.putParcelable("PURCHASE_INTENT", createMarketScreenPendingIntent(itemId, developerPayload, packageName));
				return response;
			}
			case GET_PURCHASE_INFORMATION:
			{
				if (!bundle.containsKey("NONCE"))
					throw new Error("NONCE is required");
				if (!bundle.containsKey("NOTIFY_IDS"))
					throw new Error("NOTIFY_IDS is required");
				
				long nonce = bundle.getLong("NONCE");
				String[] notifyIds = bundle.getStringArray("NOTIFY_IDS");
				
				// @todo : multiple ids
				PurchaseStateOrder order = OrderNotificationRepository.getInstance().get(notifyIds[0]);
				
				// @todo: handle order is not found
				
				PurchaseState state = new PurchaseState();
				state.setNonce(nonce);
				state.setOrder(order);
				broadcastPurchaseStateChangeIntent(state);
				
				return createSyncResponse(ResponseCode.RESULT_OK);
			}
			case CONFIRM_NOTIFICATIONS:
			{
				if (!bundle.containsKey("NOTIFY_IDS"))
					throw new Error("NOTIFY_IDS is required");
				
				// @todo : implement confirmation
				
				return createSyncResponse(ResponseCode.RESULT_OK);
			}
			case RESTORE_TRANSACTIONS:
			{
				if (!bundle.containsKey("NONCE"))
					throw new Error("NONCE is required");
				
				// @todo : implement RESTORE_TRANSACTIONS
				
				return createSyncResponse(ResponseCode.RESULT_OK);
			}
			default:
				throw new Error("unknown BillingRequestType");
		}
	}
	
	private Bundle createSyncResponse(ResponseCode code) {
		return createSyncResponse(code, System.currentTimeMillis());
	}
	
	private Bundle createSyncResponse(ResponseCode code, long requestId) {
		Bundle bundle = new Bundle();
		bundle.putInt("RESPONSE_CODE", code.getValue());
		bundle.putLong("REQUEST_ID", requestId);
		return bundle;
	}
	
	private void broadcastResponseCodeIntent(Bundle response) {		
		Intent intent = new Intent("com.android.vending.billing.RESPONSE_CODE");
		intent.putExtra("request_id", response.getLong("REQUEST_ID"));
		intent.putExtra("response_code", response.getInt("RESPONSE_CODE"));
		context.sendBroadcast(intent);
	}
	
	private void broadcastPurchaseStateChangeIntent(PurchaseState purchaseState) {
		String purchaseDataJson = purchaseState.toJson();
		Intent intent = new Intent("com.android.vending.billing.PURCHASE_STATE_CHANGED");
		intent.putExtra("inapp_signed_data", purchaseDataJson);
		intent.putExtra("inapp_signature", PurchaseDataSigner.sign(purchaseDataJson));
		context.sendBroadcast(intent);	
	}
	
	private PendingIntent createMarketScreenPendingIntent(String itemId, String developerPayload, String packageName) {		
		Intent intent = new Intent(context, TestPaymentScreenActivity.class);
		intent.putExtra(TestPaymentScreenActivity.DEVELOPER_PAYLOAD_INTENT_KEY, developerPayload);
		intent.putExtra(TestPaymentScreenActivity.ITEM_ID_INTENT_KEY, itemId);
		intent.putExtra(TestPaymentScreenActivity.PACKAGE_NAME_INTENT_KEY, packageName);
		return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
	}
	
}