package strutfit.button;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class StrutFitBridge {

    // Activity properties
    private Context _context;
    private WebView _webView;
    private Button _button;

    // Button properties
    public int _minWidth;
    public int _maxWidth;
    public int _minHeight;
    public int _maxHeight;
    public int _organizationId;
    public String _backGroundColor;
    public String _shoeID;
    private boolean _hasInitialized = false;
    private String _sizeUnavailableText;
    private String _childPreSizeText;
    private String _childPostSizeText;
    private String _adultPreSizeText;
    private String _adultPostSizeText;

    // SF Properties
    private StrutFitButton _sfButton;
    private StrutFitButtonWebview _sfWebView;

    public StrutFitBridge(Button button, WebView webview, Context context, int minWidth, int maxWidth, int minHeight, int maxHeight, String backGroundColor, int organizationId, String shoeID,
                          Optional<String> sizeUnavailableText, Optional<String> childPreSizeText, Optional<String> childPostSizeText, Optional<String> adultPreSizeText, Optional<String> adultPostSizeText) {
        _webView = webview;
        _button = button;
        _context = context;

        _button = button;
        _minWidth = minWidth;
        _maxWidth = maxWidth;
        _minHeight = minHeight;
        _maxHeight = maxHeight;
        _backGroundColor = backGroundColor;
        _organizationId = organizationId;
        _shoeID = shoeID;

        _sizeUnavailableText = sizeUnavailableText == null ? "Unavailable in your recommended size" : sizeUnavailableText.toString();
        _childPreSizeText = childPreSizeText == null ? "What is my child's size?" : childPreSizeText.toString();
        _childPostSizeText = childPostSizeText == null ? "Your child's size in this style is" : childPostSizeText.toString();
        _adultPreSizeText = adultPreSizeText == null ? "What is my size?" : adultPreSizeText.toString();
        _adultPostSizeText = adultPostSizeText == null ? "Your size in this style is" : adultPostSizeText.toString();
    }

    public void InitializeStrutfit () {
        // SF Button library will initialize the button
        // We start a new thread so the main thread does not get disturbed
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    // Construct the SF Button and its properties
                    _sfButton = new StrutFitButton(_button, _minWidth, _maxWidth, _minHeight, _maxHeight, _backGroundColor, _context, _organizationId, _shoeID, _sizeUnavailableText, _childPreSizeText, _childPostSizeText, _adultPreSizeText, _adultPostSizeText);

                    if (_sfButton._buttonIsReady) {
                        break;
                    }
                }

                // Ui changes need to run on the UI thread
                ((Activity) _context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // StrutFit library will render the text / size
                        _sfButton.SetInitialButtonUI();

                        // Initialize webView
                        _sfWebView = new StrutFitButtonWebview(_webView, _sfButton._webviewUrl, _context);
                        _sfWebView.SetJavaScriptInterface( new StrutFitJavaScriptInterface(_sfButton, _sfWebView, _context));

                        // Initialize button on-click function
                        _button.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                if(!_hasInitialized) {
                                    _sfWebView.OpenAndInitializeWebView();
                                } else {
                                    _sfWebView.OpenWebView();
                                }
                                _sfButton.HideButton();
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


    StrutFitJavaScriptInterface(StrutFitButton sfButton, StrutFitButtonWebview sfWebView, Context context) {
        _sfButton = sfButton;
        _sfWebView = sfWebView;
        _context = context;
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
                    try {
                        updateMeasurementCode(json.getString("mcode").toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    closeModal();
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void closeModal() {
        ((Activity) _context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _sfWebView.CloseWebView();
                _sfButton.ShowButton();
            }
        });
    }

    private void updateMeasurementCode(String measurementCode) throws Exception {
        _sfButton.GetSizeAndVisibility(measurementCode);
    }
}
