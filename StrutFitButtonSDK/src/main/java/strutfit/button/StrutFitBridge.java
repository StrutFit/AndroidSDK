package strutfit.button;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import strutfit.button.enums.PostMessageType;
import strutfit.button.enums.ProductType;
import strutfit.button.helpers.StrutFitCommonHelper;
import strutfit.button.models.PostMessageUserBodyMeasurementCodeDataDto;
import strutfit.button.models.PostMessageUserFootMeasurementCodeDataDto;
import strutfit.button.models.PostMessageInitialAppInfoDto;
import strutfit.button.models.PostMessageUserIdDataDto;

public class StrutFitBridge {

    // Activity properties
    private Context _context;
    private WebView _webView;
    private StrutFitButtonView _button;

    // Button properties
    public int _organizationId;
    public String _shoeID;
    private StrutFitEventListener _strutFitEventListener;

    // SF Properties
    private StrutFitButton _sfButton;
    private StrutFitButtonWebview _sfWebView;

    public StrutFitBridge(StrutFitButtonView button, WebView webview, Context context, int organizationId, String shoeID) {
        this(button, webview, context, organizationId, shoeID, null);
    }

    public StrutFitBridge(StrutFitButtonView button, WebView webview, Context context, int organizationId, String shoeID,
                          StrutFitEventListener strutFitEventListener) {
        _webView = webview;
        _context = context;
        _button = button;
        _organizationId = organizationId;
        _shoeID = shoeID;
        _strutFitEventListener = strutFitEventListener;
    }

    public void initializeStrutFit() {
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
                                _sfWebView = new StrutFitButtonWebview(_webView, _sfButton, _context, webViewLoaded -> {
                                    if(webViewLoaded) {
                                        _sfButton.showButton();
                                    }
                                });
                                _webView.loadUrl(_sfButton._webviewUrl);
                                _sfWebView.setJavaScriptInterface( new StrutFitJavaScriptInterface(_sfButton, _sfWebView, _context, _organizationId, _shoeID, _sfButton._productType, _sfButton._isKids));
                            }
                        });
                    }
                };

                // Construct the SF Button and its properties
                _sfButton = new StrutFitButton(_button, _context, _organizationId,
                        _shoeID, _strutFitEventListener, buttonVisibleCallback
);

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
                                        Toast.makeText(_context, _context.getResources().getString(R.string.cameraAccessToastMessage), Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        ActivityCompat.requestPermissions((Activity)_context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA );
                                    }
                                }

                                _sfWebView.openWebView();
                                _sfButton.hideButton();
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
    private StrutFitButton _sfButton;
    private StrutFitButtonWebview _sfWebView;
    private Context _context;
    private String TAG;

    private int _organizationId;
    private String _shoeId;
    private ProductType _productType;

    private Boolean _isKids;


    StrutFitJavaScriptInterface(StrutFitButton sfButton, StrutFitButtonWebview sfWebView, Context context,
                                int organizationId, String shoeId, ProductType productType, Boolean isKids) {
        _sfButton = sfButton;
        _sfWebView = sfWebView;
        _context = context;
        _organizationId = organizationId;
        _shoeId = shoeId;
        _productType = productType;
        _isKids = isKids
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
                    //IFrame ready
                    PostMessageInitialAppInfoDto input = new PostMessageInitialAppInfoDto();
                    input.strutfitMessageType = PostMessageType.InitialAppInfo.getValue();
                    input.productType = _productType.getValue();
                    input.isKids = _isKids;
                    input.productId = _shoeId;
                    input.organizationUnitId = _organizationId;
                    input.hideSizeGuide = true;
                    input.inApp = true;

                    _sfWebView.sendInitialAppInfo(input);
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
            String bodyMeasurementCode = StrutFitCommonHelper.getLocalBodyMCode(_context);
            _sfButton.getSizeAndVisibility(postMessage.footMeasurementCode, bodyMeasurementCode);
            StrutFitCommonHelper.setLocalFootMCode(_context, postMessage.footMeasurementCode);

        } catch (Exception e) {
            Log.e(TAG, "Unable to update measurement code", e);
        }
    }

    private void updateBodyMeasurementCode(String json){
        try {
            // JSON to Object using Gson
            Gson gson = new Gson();
            PostMessageUserBodyMeasurementCodeDataDto postMessage = gson.fromJson(json, PostMessageUserBodyMeasurementCodeDataDto.class);
            String footMeasurementCode = StrutFitCommonHelper.getLocalFootMCode(_context);
            _sfButton.getSizeAndVisibility(footMeasurementCode, postMessage.bodyMeasurementCode);
            StrutFitCommonHelper.setLocalBodyMCode(_context, postMessage.bodyMeasurementCode);

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
                _sfButton.showButton();
            }
        });
    }
}
