# StrutFit Button Android SDK
This code should be executed when a user visits the product display page. It will render the StrutFit button.


1. Add the jitpack.io url in your root build.gradle at the end of repositories if not already present:
```java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
OR add the jitpack.io url in your project settings.gradle in the repositories section:
```java
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
``` 
  
2. Add the dependency: replace x.x.x with the desired version. Please check the Releases tab to see the latest production release version
```java
	dependencies {
		implementation 'com.github.StrutFit:AndroidSDK:x.x.x'
	}
```

3. Ensure the following permissions are given to the application if they are not already:  
* **Internet:** We are accessing a website  
* **Camera:** For the in-app camera view to allow users to take photos.
* **Read Media Images:** Allows the app to access the camera roll for uploading photos
* **Write External Storage:** To allow the web-app to store variables locally (legacy) 
* **Read External Storage:** Allows the app to access camera roll for uploading photos (legacy) 

```java
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="29" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" android:maxSdkVersion="32"/>
```

4. Setting up the StrutFit button UI  
	In your layout xml you need to use the StrutFitButtonView custom view component
```java
    <strutfit.button.StrutFitButtonView
        android:id="@+id/StrutFitButton"
        android:layout_width="900px"
        android:layout_height="wrap_content" />
```  

Layout width can be modified to best suit your app, but it should be fixed. The layout height should be set to wrap_content so that the button will always be able to fit all of the button text (this will change depending on the user's language and the button's state).
	
5. Initializing the StrutFit button  
	In your Java code you need to initialize the StrutFitButton, passing through the current Activity object and the id of the StrutFitButtonView. This should be done on the product display page, and destroyed and reinitialized when the user navigates to a new product.  

You will also need the following properties:  
* **organizationId** - a constant integer given to you by your StrutFit account manager.  
* **productCode** - string value of the unique identifier of the product that is being viewed. This should a variable that changes depending on the product being viewed.
* **sizeUnit** - optional string, but can be used when you sell the same product in different regions and want to display a different size unit to the user.\
When not supplied, there is internal logic to determine which size unit to display based on previous configuration in the StrutFit system.
* **apparelSizeUnit** - optional string, same as sizeUnit but specific to apparel products. 

```java
Activity activity = this;
int buttonId = R.id.StrutFitButton;
int organizationId = 1 // constant - value will be supplied to you
String productCode = ""Test Product 1" // dynamic
String sizeUnit = "US"; // dynamic - can be null or left out if not needed
String apparelSizeUnit = "US"; // dynamic - can be null or left out if not needed

// Initialize the StrutFitButton
new StrutFitButton(activity, buttonId, organizationId, productCode, sizeUnit, apparelSizeUnit);
```
6. Testing
Your organization will need to have configured some data in StrutFit (https://dashboard.strut.fit) in order for you to test the button.\
They will need to have added at least a test product to StrutFit and linked it up to a size chart. They can then tell you the product code they have used for that product.\
You will also need to provide your application's package name to your StrutFit executive so it can be whitelisted in your StrutFit workspace settings.\
The StrutFit SDK uses **context.getPackageName()** to get your app's package name and then attaches it to API requests for whitelisting purposes.
	
You can test the SDK using different build variants: debug, staging and release.\
Debug will reference our development environment which may have unreleased code which could cause issues. You will also need to use different organizationId and productCode values in this environment.\
We therefore recommend you test while using either the staging or release build variants (as these reference the production environment), unless you have spoken with a StrutFit developer about the current development environment.\
If you have been testing the debug build variant and switch to staging/release or vice versa, especially on a physical device, it is a good idea to delete the test application (or at the very least clear the cache) before resuming testing as you may have locally stored data from the wrong environment which may cause errors.

For a quick test once you have the button appearing, instead of going through the scanning process you may log in using the following test account:   
**Email:** test@test.com    
**Password:** thisisatest    

# StrutFit (Android) Tracking Pixel Integration
Prerequisite: Complete the button integration as shown above.

The tracking pixel is used to record your orders and whether or not StrutFit was used before purchasing. This is to allow us to track the performance of StrutFit in your app/on your website.
You can see the analytics at https://dashboard.strut.fit

1. You must have the StrutFit Android SDK package in your project
2. Go to the area in your code where the user successfully completes an order
3. Apply the following code, i.e. create an instance of StrutFitTracking then register an order

```java
	Context context = this;
        StrutFitTracking sfTracking = new StrutFitTracking(context, 1); // organizationUnitId
        ArrayList<ConversionItem> items = new ArrayList<ConversionItem>();

        ConversionItem item = new ConversionItem("Test Product 1", 50.00, 1, "5 US"); // productIdentifier, price, quantity, size
        items.add(item);

        ConversionItem item2 = new ConversionItem("Test Product 1", 50.00, 2, "8", "US"); // productIdentifier, price, quantity, size, sizeUnit
        items.add(item2);

        sfTracking.registerOrder("ORDER123", 150.00, "USD", items, "test@test.com"); // orderReference, orderValue, currencyCode, items, userEmail
```
**organizationUnitId:** Same as the organizationId you used in the button integration (int)  
**orderReference:** Every order must have a unique order reference that you've generated (string) e.g. ORDER123  
**orderValue:** Total value of the order (double) e.g. 150.00  
**currencyCode:** e.g. "USD", "NZD", "AUD" etc. (string)  
**items:** An array of type **ConversionItem**  
**userEmail:** Optional - useful for user tracking, but not required (string)  
**ConversionItem:** Data structure producded by StrutFit which contains the information for every item that was purchased for this particular order.  
* productIdentifier: same as the productCode you used in the button integration (string)  
* price: price of this particular item (double)  
* quantity: number of this item purchased (int) - in the above example the same product was purchased three times in the order, but since one of those times a different size was purchased, we need two ConversionItems  
* size: the size of the item purchased (leave blank if somehow not applicable)(string)	
* sizeUnit: (Optional) You can separate out the size unit here (if applicable), or just include it in the size value (string)    
* sku: (Optional) unique SKU code for the item, this is not required, but is useful if your productIdentifier is not the SKU (string)  

Talk to your StrutFit account manager when testing this code so they can make sure your orders are coming through as expected. You may use dummy data when testing, just let your StrutFit account executive know when you are ready to release so we can separate test data from real orders.

If you have any issues or suggested changes/improvements please email dev@strut.fit.   
If the way we have implemented the library doesn't quite work for your organization please let us know, we are happy to discuss.
