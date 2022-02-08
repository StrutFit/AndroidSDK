# StrutFit (Android) Button integration
SDK for StrutFit Android integration

If you have any issues or suggested changes/improvements please email nish@strut.fit. 
If what we have implemented in the library doesnt quite work for your organisation, please let us know, we are happy to discuss.

This code should be executed when a user visits the product display page.


1. Add the jitpack.io url in your root build.gradle at the end of repositories if not already present:
```ruby
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
OR add the jitpack.io url in your project settings.gradle in the repositories section:
```ruby
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
``` 
  
2. Add the dependency: replace x.x.x with the desired version. Please check the release tab to see the latest production release version
```ruby
	dependencies {
		implementation 'com.github.StrutFit:AndroidSDK:x.x.x'
	}
```

3. Ensure the following permissions are given to the application if it does not already:  
	**Internet:** We are accessing a website  
	**Write External Storage:** To allow the web-app to store variables locally  
	**Read External Stoarge:** Allows the app to access camera roll for uploading photos  
	**Camera & Audio:** For the in-app camera view to allow users to take photos.  
			Although we don't actually record audio we still need the permission.
```ruby
	<uses-permission android:name="android.permission.INTERNET"/>
	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
	<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
	<uses-permission android:name="android.permission.CAMERA" />
```

4. Initializing StrutFit button  
	OrganizationID - an integer given to you by your StrutFit account manager.  
	ProductIdentifer  - string value of the unique identifer of the shoe that is being viewed.  

	When testing you can use the following.  
	**OrganizationID:** 5  
	**ProductIdentifer:** "TestProduct" 

	For a quick test instead of going to the scanning process you may login using the following test account. 
	Before release please remember to put in the actual product identifier and organizationId provided by your StrutFit account manager.  
	**Email:** test@test.com  
	**Password:** thisisatest  
	**ActivityContext:** is the activity context  
	800, 800, 100, 100 = Dimensions of the button  
	"#f2f2f2" = Background color of the button (this is the recommended StrutFit colour) 

	We encourage you to modify the button UI to suit your application while conforming to the StrutFit brand guidelines.  
	
	**Initiating the StrutFitBridge should be done on the product display page.
	Re-create the class when the user navigates to a new product page**

```ruby
	// Create your button and hide it
	Button button = (Button) findViewById(R.id.StruftFitButton);
	button.setVisibility(View.GONE);

	// Create your web view
	WebView webView = (WebView) findViewById(R.id.StruftFitWebview);
	webView.setVisibility(View.GONE);

	// Pass the the two components into the StrutFit bridge
	StrutFitBridge bridge = new StrutFitBridge(button, webView, ActivityContext, 800, 800, 100, 100, "#f2f2f2", OrganizationID, ProductIdentifier, null, null, null, null, null);
	bridge.initializeStrutFit();
```
# StrutFit (Android) tracking pixel integration
Prerquisite: Complete the button integration as shown above.

The tracking pixel is used to record orders from the retailer. This is to allow us to track the preformace of StrutFit on your website.
You can see the analytics in the Retailer dashboard/

1. You must have the StrutFit Android SDK package in your project.
2. Go to the area in your code where the end consumer successfully completes an order
3. Consider the following code: create an instance of StrutFitTracking then register an order

```ruby
	StrutFitTracking sfTracking = new StrutFitTracking(ActivityContext, OrganizationID);
	sfTracking.registerOrder(OrderReference, OrderValue, CurrencyCode, ListOfItems);
```
**OrderReference:** Typically every order has a unique order reference (string)  
**OrderValue:** Total value of the order (double)  
**CurrencyCode:** e.g. "USD", "NZD", "AUD" etc.  
**ListOfItems:** Create an object **ArrayList<ConversionItem>** ListOfItems  
**ConversionItem:** Data structure producded by StrutFit which contains the information for every item that was purchased for this particular order.  
	-sku: unique code for the item (string)  
	-productIdentifier: same as the productIdentifer you used in the button integration (sometimes this could be the same as sku) (string)  
	-price: price of this particular item (double)  
	-quantity: number of this item purchased (int)  
	-size: if there is a size to the item (string)
	
