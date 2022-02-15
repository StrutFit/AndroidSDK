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

4. Setting up the StrutFit button UI  
	In your layout xml you need to use the StrutFitButtonView custom view component
```ruby
<strutfit.button.StrutFitButtonView
	android:id="@+id/StruftFitButton"
	android:layout_width="800px"
	android:layout_height="100px" />
```  

Layout width and height can be modified to best suit your app, just be sure to test that the size recommendation text is not partially hidden due to the button size.  

We encourage you to modify the button UI to suit your application while conforming to the StrutFit brand guidelines.  
You can do this by using the below optional attributes on the StrutFitButtonView component.  

**buttonColor** - string color hex value to set the background color of the button, e.g. "#ffffff" (defaults as #f2f2f2)  
**buttonPressedColor** - string color hex value to set the background color of the button while button is pressed, e.g. "#ffffff" (defaults as #bcbdbd)  
**textColor** - string color hex value to set the color of the button text, e.g. "#000000" (defaults as #232323)  
**useWhiteLogo** - boolean value, if true it uses the StrutFit logo icon with a colour #e3e3e3, if false or omitted it uses logo with a colour #232323  
**buttonTextFont** - reference to font file, e.g. "@font/my_custom_font" (defaults as Brandon Grotesque)  

Example StrutFitButtonView using all of the options:
```ruby
<strutfit.button.StrutFitButtonView
	android:id="@+id/StruftFitButton"
	android:layout_width="800px"
	android:layout_height="100px"
	app:textColor="#232323"
        app:buttonColor="#f2f2f2"
        app:buttonPressedColor="#bcbdbd"
        app:useWhiteLogo="false"
        app:buttonTextFont="@font/my_custom_font"/>
```  

You will also need to use a WebView component in your layout.  
	
5. Initializing StrutFit button  
	Then in your Java code you need to intialize the StrutFitBridge, passing through references to the StrutFitButtonView and WebView.  

	You will also need the following properties:  
	OrganizationID - an integer given to you by your StrutFit account manager.  
	ProductIdentifer  - string value of the unique identifer of the shoe that is being viewed.  

	When testing you can use the following.  
	**OrganizationID:** 5  
	**ProductIdentifer:** "TestProduct" 

	For a quick test instead of going through the scanning process you may login using the following test account. 
	Before release please remember to put in the actual product identifier and organizationId provided by your StrutFit account manager.  
	**Email:** test@test.com  
	**Password:** thisisatest  

	**Initializing the StrutFitBridge should be done on the product display page.
	Re-create the class when the user navigates to a new product page**

```ruby
	// Create your button and hide it
	StrutFitButtonView button = (Button) findViewById(R.id.StruftFitButton);
	button.setVisibility(View.GONE);

	// Create your web view
	WebView webView = (WebView) findViewById(R.id.StruftFitWebview);
	webView.setVisibility(View.GONE);

	// Pass the two components into the StrutFitBridge
	// along with OrganizationId and ProductIdentifier
	// ActivityContext is the activity context  
	StrutFitBridge bridge = new StrutFitBridge(button, webView, ActivityContext, OrganizationID, ProductIdentifier);
	bridge.initializeStrutFit();
```
You can also pass in the following optional parameters to the StrutFitBridge constructor:  
**strutFitEventListener** - this is an object with a class that implements the **StrutFitEventListener** interface. The onSizeEvent method will receive a size string and size unit enum when the StrutFit button size value is updated. You can use this to update your size dropdowns.   
**sizeUnavailableText** - You can override the text that is displayed on the button when a size is unavailable for the user (default is "Unavailable in your recommended size")  
**childPreSizeText** - You can override the text that is displayed on the button for child shoes before the user has scanned (default is "What is my child's size?")  
**childPostSizeText** - You can override the text that is displayed on the button before the recommended size for child shoes after the user has scanned (default is "Your child's size in this style is")  
**adultPreSizeText** - You can override the text that is displayed on the button for adult shoes before the user has scanned  (default is "What is my size?")  
**adultPostSizeText** - You can override the text that is displayed on the button before the recommended size for adult shoes after the user has scanned (default is "Your size in this style is")  
 
```ruby
public interface StrutFitEventListener {
    void onSizeEvent(String size, SizeUnit unit);
}

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
**ListOfItems:** Create an object **ArrayList&lt;ConversionItem&gt;** ListOfItems  
**ConversionItem:** Data structure producded by StrutFit which contains the information for every item that was purchased for this particular order.  
* sku: unique code for the item (string)  
* productIdentifier: same as the productIdentifer you used in the button integration (sometimes this could be the same as sku) (string)  
* price: price of this particular item (double)  
* quantity: number of this item purchased (int)  
* size: if there is a size to the item (string)
	
