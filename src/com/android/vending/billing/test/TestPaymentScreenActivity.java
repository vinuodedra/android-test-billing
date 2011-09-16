package com.android.vending.billing.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class TestPaymentScreenActivity extends Activity {
	public static final String DEVELOPER_PAYLOAD_INTENT_KEY = "DEVELOPER_PAYLOAD";
	public static final String ITEM_ID_INTENT_KEY = "ITEM_ID";
	public static final String PACKAGE_NAME_INTENT_KEY = "PACKAGE_NAME";
	
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
	
	private PurchaseStateOrder createOrder() {
		PurchaseStateOrder order = new PurchaseStateOrder();
		order.setDeveloperPayload(getDeveloperPayload());
		order.setPackageName(getCallerPackageName());
		order.setProductId(getItemId());
		return order;
	}
	
	private void notifyAboutOrder(PurchaseStateOrder order) {
		String notificationId = OrderNotificationRepository.getInstance().add(order);
		broadcastAppNotificationIntent(notificationId);	
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
				// @todo select order id in a dialog
				String orderId = "";
				
				PurchaseStateOrder order = PurchaseStateOrderRepository.getInstance().get(orderId);
				// @todo handle order not found

				order.setPurchaseState(PurchaseStateOrder.STATE_REFUNDED);				
				notifyAboutOrder(order);
			}
		});        
        
        this.setContentView(layout);
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    }
    
	private void broadcastAppNotificationIntent(String notificationId) {
		Intent intent = new Intent("com.android.vending.billing.IN_APP_NOTIFY");
		intent.putExtra("notification_id", notificationId);
		this.sendBroadcast(intent);		
	}
    
}
