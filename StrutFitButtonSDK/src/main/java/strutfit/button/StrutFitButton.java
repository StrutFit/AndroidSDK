package strutfit.button;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;


import strutfit.button.helpers.StrutFitButtonHelper;

public class StrutFitButton {

    public Boolean _buttonIsReady = false;
    public String _webviewUrl = "";
    public StrutFitButtonView _button;
    public int _minWidth;
    public int _maxWidth;
    public int _minHeight;
    public int _maxHeight;
    public String _backGroundColor;
    public StrutFitButtonHelper _buttonHelper;

    private Context _context;
    private String TAG;

    private Runnable _callback = new Runnable() {
        @Override
        public void run() {
            setInitialButtonValues();
        }
    };

    public StrutFitButton(StrutFitButtonView button, int minWidth, int maxWidth, int minHeight, int maxHeight, String backGroundColor,
                          Context context, int organizationID, String shoeID,
                          String sizeUnavailableText, String childPreSizeText, String childPostSizeText, String adultPreSizeText, String adultPostSizeText) {

        _button = button;
        _minWidth = minWidth;
        _maxWidth = maxWidth;
        _minHeight = minHeight;
        _maxHeight = maxHeight;
        _backGroundColor = backGroundColor;
        _context = context;
        TAG = ((Activity) _context).getClass().getSimpleName();

        try {
            _buttonHelper = new StrutFitButtonHelper(context, _callback, organizationID, shoeID, sizeUnavailableText, childPreSizeText, childPostSizeText, adultPreSizeText, adultPostSizeText);
            _webviewUrl = _buttonHelper.webViewURL;
            _buttonIsReady = true;
        } catch (Exception e) {
            Log.e(TAG, "Unable to construct button helper and initialize the button", e);
        }
    }

    public void setInitialButtonUI() {
        _button.setBackgroundColor(Color.parseColor((_backGroundColor)));
    }

    private void setInitialButtonValues() {
        if(_buttonHelper.buttonIsVisible) {
            _button.setText(_buttonHelper.buttonText);
            _webviewUrl = _buttonHelper.webViewURL;
            _button.setVisibility(View.VISIBLE);
        } else {
            _button.setVisibility(View.GONE);
        }
    }

    public void hideButton() {
        _button.setVisibility(View.GONE);
    }

    public void showButton() {
        _button.setVisibility(View.VISIBLE);
    }

    public void getSizeAndVisibility(String MeasurementCode) throws Exception {
        _buttonHelper.getSizeAndVisibility(MeasurementCode, false);
    }
}
