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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class StrutFitBridge {

    // Activity properties
    private Context _context;
    private WebView _webView;
    private StrutFitButtonView _button;

    // Button properties
    public int _organizationId;
    public String _shoeID;
    private boolean _hasInitialized = false;
    private StrutFitEventListener _strutFitEventListener;
    private String _sizeUnavailableText;
    private String _childPreSizeText;
    private String _childPostSizeText;
    private String _adultPreSizeText;
    private String _adultPostSizeText;

    // SF Properties
    private StrutFitButton _sfButton;
    private StrutFitButtonWebview _sfWebView;

    public StrutFitBridge(StrutFitButtonView button, WebView webview, Context context, int organizationId, String shoeID) {
        this(button, webview, context, organizationId, shoeID, null, null, null, null, null, null);
    }

    public StrutFitBridge(StrutFitButtonView button, WebView webview, Context context, int organizationId, String shoeID,
                          StrutFitEventListener sizeEventListener) {
        this(button, webview, context, organizationId, shoeID, sizeEventListener, null, null, null, null, null);
    }

    public StrutFitBridge(StrutFitButtonView button, WebView webview, Context context, int organizationId, String shoeID,
                          StrutFitEventListener strutFitEventListener,
                          Optional<String> sizeUnavailableText, Optional<String> childPreSizeText, Optional<String> childPostSizeText,
                          Optional<String> adultPreSizeText, Optional<String> adultPostSizeText) {
        _webView = webview;
        _button = button;
        _context = context;

        _button = button;
        _organizationId = organizationId;
        _shoeID = shoeID;
        _strutFitEventListener = strutFitEventListener;

        _sizeUnavailableText = sizeUnavailableText == null ? _context.getResources().getString(R.string.defaultSizeUnavailableText) : sizeUnavailableText.toString();
        _childPreSizeText = childPreSizeText == null ? _context.getResources().getString(R.string.defaultChildPreSizeText) : childPreSizeText.toString();
        _childPostSizeText = childPostSizeText == null ? _context.getResources().getString(R.string.defaultChildPostSizeText) : childPostSizeText.toString();
        _adultPreSizeText = adultPreSizeText == null ? _context.getResources().getString(R.string.defaultAdultPreSizeText) : adultPreSizeText.toString();
        _adultPostSizeText = adultPostSizeText == null ? _context.getResources().getString(R.string.defaultAdultPostSizeText) : adultPostSizeText.toString();
    }

    public void initializeStrutFit() {
        // SF Button library will initialize the button
        // We start a new thread so the main thread does not get disturbed
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Construct the SF Button and its properties
                _sfButton = new StrutFitButton(_button, _context, _organizationId, _shoeID, _strutFitEventListener,
                                                _sizeUnavailableText, _childPreSizeText, _childPostSizeText, _adultPreSizeText, _adultPostSizeText);

                // Ui changes need to run on the UI thread
                ((Activity) _context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Initialize webView
                        _sfWebView = new StrutFitButtonWebview(_webView, _sfButton, _context);
                        _sfWebView.setJavaScriptInterface( new StrutFitJavaScriptInterface(_sfButton, _sfWebView, _context));

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

                                if(!_hasInitialized) {
                                    _sfWebView.openAndInitializeWebView();
                                } else {
                                    _sfWebView.openWebView();
                                }
                                _sfButton.hideButton();
                                _hasInitialized = true;
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


    StrutFitJavaScriptInterface(StrutFitButton sfButton, StrutFitButtonWebview sfWebView, Context context) {
        _sfButton = sfButton;
        _sfWebView = sfWebView;
        _context = context;
        TAG = ((Activity) _context).getClass().getSimpleName();
    }

    @JavascriptInterface
    public void receiveMessage(String message) {

        try {
            JSONObject json = new JSONObject(message);
            int result = json.getInt("messageType");

            switch (result)
            {
                case 0:
                case 1:
                    // update m-code
                    updateMeasurementCode(json);
                    break;
                case 2:
                    closeModal();
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            Log.e(TAG, "Unable to process message", e);
        }
    }

    private void updateMeasurementCode(JSONObject json){
        try {
            updateMeasurementCode(json.getString("mcode").toString());
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

    private void updateMeasurementCode(String measurementCode) throws Exception {
        _sfButton.getSizeAndVisibility(measurementCode);
    }
}
