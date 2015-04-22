# Official documentation mistakes #

Official documentation contains several inconsistencies, which can be resolved by looking at the provided example code.

  1. NOTIFY\_IDS parameter is the array of strings
  1. The inapp\_signed\_data JSON in the PURCHASE\_STATE\_CHANGED notification has the array of orders. Also the array sometimes is not present at all, so they use JSONObject.optJSONArray() to parse it.

The correct inapp\_signed\_data JSON example would be like this :

```json

{ "nonce" : 1836535032137741465,
"orders" : [
{ "notificationId" : "android.test.purchased",
"orderId" : "transactionId.android.test.purchased",
"packageName" : "com.example.dungeons",
"productId" : "android.test.purchased",
"developerPayload" : "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ",
"purchaseTime" : 1290114783411,
"purchaseState" : 0 }]
}
```