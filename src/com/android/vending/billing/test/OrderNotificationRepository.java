package com.android.vending.billing.test;

import java.util.HashMap;
import java.util.Map;

public class OrderNotificationRepository {
	private static OrderNotificationRepository instance;
	
	public static OrderNotificationRepository getInstance() {
		if (instance == null)
			instance = new OrderNotificationRepository();
		return instance;
	}
	
	private Map<String, PurchaseStateOrder> notifications = new HashMap<String, PurchaseStateOrder>();
	
	private OrderNotificationRepository() {		
	}
	
	public String add(PurchaseStateOrder order) {
		String notificationId = (notifications.size() + 1) + "";
		notifications.put(notificationId, order);	
		return notificationId;
	}
	
	public PurchaseStateOrder get(String notificationId) {
		PurchaseStateOrder order = notifications.get(notificationId);
		if (order == null)
			return null;
		order = (PurchaseStateOrder)order.clone();
		order.setNotificationId(notificationId);
		return order;
	}
	
	public void remove(String notificationId) {
		notifications.remove(notificationId);
	}

}
