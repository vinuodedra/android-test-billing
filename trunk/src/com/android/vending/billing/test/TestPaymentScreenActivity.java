package com.android.vending.billing.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class TestPaymentScreenActivity extends Activity {
	public static final String DEVELOPER_PAYLOAD_INTENT_KEY = "DEVELOPER_PAYLOAD";
	public static final String ITEM_ID_INTENT_KEY = "ITEM_ID";
	public static final String PACKAGE_NAME_INTENT_KEY = "PACKAGE_NAME";
	public static final String REQUEST_ID_INTENT_KEY = "REQUEST_ID";
	
	private ResponseIntentFactory responseIntentFactory = new ResponseIntentFactory();
	
	private Button createTestButton(String text, OnClickListener listener) {
        Button testButton = new Button(this);
        testButton.setText(text);
        testButton.setOnClickListener(listener);
        return testButton;
	}
	
	private void addTestButton(LinearLayout layout, String text, OnClickListener listener) {
		layout.addView(createTestButton(text, listener));
	}
	
	private String getDeveloperPayload() {
		return getIntent().getExtras().getString(DEVELOPER_PAYLOAD_INTENT_KEY);
	}
	private String getItemId() {
		return getIntent().getExtras().getString(ITEM_ID_INTENT_KEY);
	}
	private String getCallerPackageName() {
		return getIntent().getExtras().getString(PACKAGE_NAME_INTENT_KEY);
	}
	private long getRequestId() {
		return getIntent().getExtras().getLong(REQUEST_ID_INTENT_KEY);
	}
	
	private PurchaseStateOrder createOrder() {
		PurchaseStateOrder order = new PurchaseStateOrder();
		order.setDeveloperPayload(getDeveloperPayload());
		order.setPackageName(getCallerPackageName());
		order.setProductId(getItemId());
		return order;
	}
	
	private void notifyAboutOrder(PurchaseStateOrder order) {
		// request has reached the server
		this.sendBroadcast(responseIntentFactory.createResponseCodeIntent(getRequestId(), ResponseCode.RESULT_OK));
		
		// order notification
		String notificationId = OrderNotificationRepository.getInstance().add(order);
		this.sendBroadcast(responseIntentFactory.createAppNotificationIntent(notificationId));	
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        
        addTestButton(layout, "buy item successfully", new OnClickListener() {
			public void onClick(View v) {
				PurchaseStateOrder order = createOrder();
				order.setPurchaseState(PurchaseStateOrder.STATE_PURCHASED);
				PurchaseStateOrderRepository.getInstance().add(order);
				notifyAboutOrder(order);
			}
		});        
        addTestButton(layout, "cancel buying process", new OnClickListener() {
			public void onClick(View v) {
				PurchaseStateOrder order = createOrder();
				order.setPurchaseState(PurchaseStateOrder.STATE_CANCELED);
				PurchaseStateOrderRepository.getInstance().add(order);
				notifyAboutOrder(order);
			}
		});        
        addTestButton(layout, "refund order", new OnClickListener() {
			public void onClick(View v) {
				// TODO: select order id in a dialog
				String orderId = "";
				
				PurchaseStateOrder order = PurchaseStateOrderRepository.getInstance().get(orderId);
				if (order != null) {
					order.setPurchaseState(PurchaseStateOrder.STATE_REFUNDED);				
					notifyAboutOrder(order);
				}
			}
		});        
        addTestButton(layout, "service unavailable", new OnClickListener() {
			public void onClick(View v) {
				v.getContext().sendBroadcast(responseIntentFactory.createResponseCodeIntent(getRequestId(), ResponseCode.RESULT_SERVICE_UNAVAILABLE));
			}
		});        
        
        // TODO: handle back button with RESULT_USER_CANCELED
        
        this.setContentView(layout);
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    }
    
   
}
