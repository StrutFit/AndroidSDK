package strutfit.button;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;


import strutfit.button.helpers.StrutFitButtonHelper;

public class StrutFitButton {

    public Boolean _buttonIsReady = false;
    public String _webviewUrl = "";
    public StrutFitButtonView _button;
    public StrutFitButtonHelper _buttonHelper;

    private Context _context;
    private String TAG;

    private Runnable _callback = new Runnable() {
        @Override
        public void run() {
            setInitialButtonValues();
        }
    };

    public StrutFitButton(StrutFitButtonView button, Context context, int organizationID, String shoeID, StrutFitEventListener strutFitEventListener) {

        _button = button;
        _context = context;
        TAG = ((Activity) _context).getClass().getSimpleName();

        try {
            _buttonHelper = new StrutFitButtonHelper(context, _callback, organizationID, shoeID, strutFitEventListener);
            _webviewUrl = _buttonHelper.webViewURL;
            _buttonIsReady = true;
        } catch (Exception e) {
            Log.e(TAG, "Unable to construct button helper and initialize the button", e);
        }
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
