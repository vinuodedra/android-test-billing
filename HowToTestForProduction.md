# How to test the real API for the production #

When you have finished testing on the emulator, you want to switch to the real API.
In order to do that you need to :

  1. remove the android-test-billing library reference
  1. include IMarketBillingService.aidl
  1. change service creation code
  1. sign and upload your application for testing
  1. test your application

## 1. Remove the library reference ##
Follow the same steps as you did to reference the library, and remove the reference on the android-test-billing library from the project.

## 2. Include IMarketBillingService.aidl ##
Include IMarketBillingService.aidl file into the project to the src\com\android\vending\billing directory.

```java

package com.android.vending.billing;
import android.os.Bundle;
interface IMarketBillingService {
// Given the arguments in bundle form, returns a bundle for results.
Bundle sendBillingRequest(in Bundle bundle);
}
```

You can download the file here :
[IMarketBillingService.aidl](http://marketbilling.googlecode.com/hg-history/release_1/src/com/android/vending/billing/IMarketBillingService.aidl)

## 3. Change service creation code ##
The real billing service object is created asynchronously : first you make a call to the Context.bindService() and pass a callback (ServiceConnection interface), then you receive a service instance through the callback. This may change your code.

Here is the code example :
```java

```
Intent billingServiceBindIntent = new Intent("com.android.vending.billing.MarketBillingService.BIND"); 

boolean bindResult = context.bindService(billingServiceBindIntent,
	new ServiceConnection() {				
		public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
			IMarketBillingService service = IMarketBillingService.Stub.asInterface(serviceBinder));
			// TODO: use the service ...
		}
		public void onServiceDisconnected(ComponentName name) {
			// TODO: handle the service disconnection ...			
		}
	}, Context.BIND_AUTO_CREATE);
if (!bindResult) {
	// TODO: handle bindService() error ...
}

```
```

## 4. Sign and upload your application for testing ##
Follow the documentation steps about signing and publication :
[Signing Your Applications](http://developer.android.com/guide/publishing/app-signing.html)
Sign the application and upload it to the Android Market. You can leave it "Unpublished".

## 5. Test your application ##
First test with predefined "test" product items, then test with the real products.
Follow the documentation about testing your application :
[Testing In-app Billing](http://developer.android.com/guide/market/billing/billing_testing.html)