package strutfit.button;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import strutfit.button.enums.OnlineScanInstructionsType;
import strutfit.button.enums.PostMessageType;
import strutfit.button.enums.ProductType;
import strutfit.button.enums.SizeUnit;
import strutfit.button.helpers.StrutFitButtonHelper;
import strutfit.button.helpers.StrutFitCommonHelper;
import strutfit.button.models.PostMessageUserBodyMeasurementCodeDataDto;
import strutfit.button.models.PostMessageUserFootMeasurementCodeDataDto;
import strutfit.button.models.PostMessageInitialAppInfoDto;
import strutfit.button.models.PostMessageUserIdDataDto;

public class StrutFitButton {

    // Activity properties
    private Context _context;
    private WebView _webView;
    private StrutFitButtonView _button;

    // Button properties
    public int _organizationId;
    public String _shoeID;
    public SizeUnit _sizeUnit;

    // SF Properties
    private StrutFitButtonHelper _sfButtonHelper;
    private StrutFitButtonWebview _sfWebView;

    public StrutFitButton(Activity activity, int buttonViewId, int organizationId, String shoeID) {
        this(activity, buttonViewId, organizationId, shoeID, null, null);
    }

    public StrutFitButton(Activity activity, int buttonViewId, int organizationId, String shoeID,
                          String sizeUnit) {
        this(activity, buttonViewId, organizationId, shoeID, sizeUnit, null);
    }

    public StrutFitButton(Activity activity, int buttonViewId, int organizationId, String shoeID,
                          String sizeUnit, WebView existingWebView) {
        _context = activity;
        _organizationId = organizationId;
        _shoeID = shoeID;
        _sizeUnit = SizeUnit.getSizeUnitFromString(sizeUnit);

        StrutFitButtonView button = activity.findViewById(buttonViewId);
        button.setVisibility(View.GONE);
        _button = button;

        if(existingWebView == null) {
            // Create the WebView dynamically
            WebView webView = new WebView(activity);
            webView.setVisibility(View.GONE);

            // Set LayoutParams to make the WebView fill the entire screen
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
            );
            webView.setLayoutParams(layoutParams);

            // Add the WebView directly to the root view of the Activity
            FrameLayout rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            rootView.addView(webView);

            _webView = webView;
        } else {
            existingWebView.setVisibility(View.GONE);
            _webView = existingWebView;
        }

        initializeStrutFit();
    }

    private void initializeStrutFit() {
        // SF Button library will initialize the button
        // We start a new thread so the main thread does not get disturbed
        new Thread(new Runnable() {
            @Override
            public void run() {
                Consumer<Boolean> buttonVisibleCallback = showButton -> {
                    if(showButton) {
                        // Ui changes need to run on the UI thread
                        ((Activity) _context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Initialize webView
                                _sfWebView = new StrutFitButtonWebview(_webView, _context, webViewLoaded -> {
                                    if(webViewLoaded) {
                                        _sfButtonHelper.showButton();
                                    }
                                });
                                _webView.loadUrl(_sfButtonHelper.webViewURL);
                                _sfWebView.setJavaScriptInterface(
                                        new StrutFitJavaScriptInterface(
                                                _sfButtonHelper, _sfWebView, _context,
                                                _organizationId, _shoeID, _sizeUnit,
                                                _sfButtonHelper.productType, _sfButtonHelper.isKids,
                                                _sfButtonHelper.onlineScanInstructionsType
                                        )
                                );
                            }
                        });
                    }
                };

                // Construct the SF Button and its properties
                try {
                    _sfButtonHelper = new StrutFitButtonHelper(_button, _context, _organizationId,
                            _shoeID, buttonVisibleCallback);
                } catch (Exception e) {
                    Log.e(((Activity) _context).getClass().getSimpleName(), "Unable to construct button helper and initialize the button", e);
                }

                // Ui changes need to run on the UI thread
                ((Activity) _context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Initialize button on-click function
                        _button.getButton().setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                int MY_PERMISSIONS_REQUEST_CAMERA=0;

                                if (ContextCompat.checkSelfPermission(_context, Manifest.permission.CAMERA) != PERMISSION_GRANTED)
                                {
                                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) _context, Manifest.permission.CAMERA))
                                    {
                                        Toast.makeText(_context, _context.getResources().getString(R.string.InAppCameraAccessToastMessage), Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        ActivityCompat.requestPermissions((Activity)_context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA );
                                    }
                                }

                                _sfWebView.openWebView();
                                _sfButtonHelper.hideButton();
                            }
                        });
                    }
                });
            }
        }).start();
    }
}

class StrutFitJavaScriptInterface {

    // SF Properties
    private StrutFitButtonHelper _sfButtonHelper;
    private StrutFitButtonWebview _sfWebView;
    private Context _context;
    private String TAG;

    private int _organizationId;
    private String _shoeId;
    private SizeUnit _sizeUnit;
    private ProductType _productType;
    private Boolean _isKids;
    private OnlineScanInstructionsType _onlineScanInstructionsType;

    private Boolean _initialAppInfoSent = false;


    StrutFitJavaScriptInterface(StrutFitButtonHelper sfButtonHelper, StrutFitButtonWebview sfWebView, Context context,
                                int organizationId, String shoeId, SizeUnit sizeUnit, ProductType productType,
                                Boolean isKids, OnlineScanInstructionsType onlineScanInstructionsType) {
        _sfButtonHelper = sfButtonHelper;
        _sfWebView = sfWebView;
        _context = context;
        _organizationId = organizationId;
        _shoeId = shoeId;
        _sizeUnit = sizeUnit;
        _productType = productType;
        _isKids = isKids;
        _onlineScanInstructionsType = onlineScanInstructionsType;
        TAG = ((Activity) _context).getClass().getSimpleName();
    }

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @JavascriptInterface
    public void receiveMessage(String message) {
        try {
            JSONObject json = new JSONObject(message);
            PostMessageType result = PostMessageType.valueOf(json.getInt("strutfitMessageType"));

            switch (result)
            {
                case UserAuthData:
                    //update uuid
                    updateUserId(message);
                    break;
                case UserFootMeasurementCodeData:
                    // update m-code
                    updateFootMeasurementCode(message);
                    break;
                case UserBodyMeasurementCodeData:
                    //update body m-code
                    updateBodyMeasurementCode(message);
                    break;
                case CloseIFrame:
                    closeModal();
                    break;
                case IframeReady:
                    if(!_initialAppInfoSent) {
                        PostMessageInitialAppInfoDto input = new PostMessageInitialAppInfoDto();
                        input.strutfitMessageType = PostMessageType.InitialAppInfo.getValue();
                        input.productType = _productType.getValue();
                        input.isKids = _isKids;
                        input.productId = _shoeId;
                        input.organizationUnitId = _organizationId;
                        input.defaultUnit = _sizeUnit != null ? _sizeUnit.getValue() : null;
                        input.onlineScanInstructionsType = _onlineScanInstructionsType.getValue();
                        input.hideSizeGuide = true;
                        input.hideUsualSize = true;
                        input.inApp = true;

                        _sfWebView.sendInitialAppInfo(input);
                        _initialAppInfoSent = true;
                    }
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            Log.e(TAG, "Unable to process message", e);
        }
    }

    private void updateFootMeasurementCode(String json){
        try {
            // JSON to Object using Gson
            Gson gson = new Gson();
            PostMessageUserFootMeasurementCodeDataDto postMessage = gson.fromJson(json, PostMessageUserFootMeasurementCodeDataDto.class);
            String currentFootMeasurementCode = StrutFitCommonHelper.getLocalFootMCode(_context);
            if(!Objects.equals(currentFootMeasurementCode, postMessage.footMeasurementCode)){
                if(_productType == ProductType.Footwear) {
                    String bodyMeasurementCode = StrutFitCommonHelper.getLocalBodyMCode(_context);
                    _sfButtonHelper.getSizeAndVisibility(postMessage.footMeasurementCode, bodyMeasurementCode, false);
                }
                StrutFitCommonHelper.setLocalFootMCode(_context, postMessage.footMeasurementCode);
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable to update measurement code", e);
        }
    }

    private void updateBodyMeasurementCode(String json){
        try {
            // JSON to Object using Gson
            Gson gson = new Gson();
            PostMessageUserBodyMeasurementCodeDataDto postMessage = gson.fromJson(json, PostMessageUserBodyMeasurementCodeDataDto.class);
            String currentBodyMeasurementCode = StrutFitCommonHelper.getLocalFootMCode(_context);
            if(!Objects.equals(currentBodyMeasurementCode, postMessage.bodyMeasurementCode)) {
                if(_productType == ProductType.Apparel) {
                    String footMeasurementCode = StrutFitCommonHelper.getLocalFootMCode(_context);
                    _sfButtonHelper.getSizeAndVisibility(footMeasurementCode, postMessage.bodyMeasurementCode, false);
                }
                StrutFitCommonHelper.setLocalBodyMCode(_context, postMessage.bodyMeasurementCode);
            }

        } catch (Exception e) {
            Log.e(TAG, "Unable to update measurement code", e);
        }
    }

    private void updateUserId(String json){
        try {
            // JSON to Object using Gson
            Gson gson = new Gson();
            PostMessageUserIdDataDto postMessage = gson.fromJson(json, PostMessageUserIdDataDto.class);
            StrutFitCommonHelper.setLocalUserId(_context, postMessage.uuid);

        } catch (Exception e) {
            Log.e(TAG, "Unable to update measurement code", e);
        }
    }


    private void closeModal() {
        ((Activity) _context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _sfWebView.closeWebView();
                _sfButtonHelper.showButton();
            }
        });
    }
}
