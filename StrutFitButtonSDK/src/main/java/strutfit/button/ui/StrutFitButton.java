package strutfit.button.ui;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.function.Consumer;

import strutfit.button.R;
import strutfit.button.StrutFitButtonView;
import strutfit.button.enums.SizeUnit;
import strutfit.button.helpers.StrutFitButtonHelper;
import strutfit.button.helpers.StrutFitJavaScriptInterface;

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