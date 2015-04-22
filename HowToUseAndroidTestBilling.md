# How to setup Android Test Billing in your project #

  1. Reference the library
  1. Edit AndroidManifest.xml
  1. Create the test service object
  1. Add the test code

## 1. Reference the library ##
You have 2 options : reference the source code or reference the jar library.

The simplest is to reference the jar :
  * Download the latest distribution package from [Downloads](http://code.google.com/p/android-test-billing/downloads/list) and copy it to your "lib" directory
  * Reference the jar in your Eclipse project (Project menu -> Properties -> Java Build Path -> Libraries -> Add JARs...)

If you want to be able to debug, it's better to reference android-test-billing source code. Follow the instructions [Source code](http://code.google.com/p/android-test-billing/source/checkout) to download the code.
  * Checkout the code to your Eclipse workspace directory.
  * Add a reference from your main project (Project menu -> Properties -> Android -> Library -> Add)

## 2. Edit AndroidManifest.xml ##
Add the following to AndroidManifest.xml :
```xml

```
        <activity android:name="com.android.vending.billing.test.TestPaymentScreenActivity" />
```
```
This registers the test payment screen.

## 3. Create the test service object ##
As simple as :
```java

```
	private IMarketBillingService createMarketBillingService() {
		return new com.android.vending.billing.test.TestMarketBillingService(this);
	}
```
```

## 4. Add the test code ##
Add the test code as described in the documentation ([Implementing In-app Billing](http://developer.android.com/guide/market/billing/billing_integrate.html)). You will need to create :
  * a payment Activity (the screen where you select what to buy and proceed to the checkout)
  * a BroadcastReceiver (it receives the notifications from the Android Market application)
  * and a Service, which connects BroadcastReceiver and the payment Activity

Don't add IMarketBillingService.aidl file to your project, or it will conflict with the library.