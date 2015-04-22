# Why do we need a custom Service class? #

Official documentation advices to have a custom Service class to make requests to the in-app billing service.

This is not a must and in some cases it is easier to implement the processing in the usual class, not in a Service class.

## What do you loose if you don't have a custom Service? ##

When you are notified about the purchase via a broadcast intent, your thread is in foreground and it blocks the UI. So if you have a long-lasting processing logic (for example, you want to validate the purchase on your web server) the UI will be blocked for several seconds, which is not good.

If you need several seconds to process the purchase and you don't want to block the UI, you will want to do the processing in another thread. But you can't do it in your UI thread, because the Google Checkout UI can be active at the moment. So the only option is to process it in a background thread.

If you just start a thread, which is not associated with a Service class, it can be forcibly killed by the OS (as stated in the documentation of the [BroadcastReceiver - Process Lifecycle](http://developer.android.com/reference/android/content/BroadcastReceiver.html)). So the only good way of doing it is to call context.startService() and do the processing in the Service class.