package strutfit.button;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;


import java.util.function.Consumer;

import strutfit.button.enums.ProductType;
import strutfit.button.helpers.StrutFitButtonHelper;

public class StrutFitButton {

    public Boolean _buttonIsReady = false;
    public String _webviewUrl = "";

    public ProductType _productType = ProductType.Footwear;
    public Boolean _isKids = false;

    public StrutFitButtonView _button;
    public StrutFitButtonHelper _buttonHelper;

    private Context _context;
    private String TAG;

    private Consumer<Boolean> _buttonVisibleCallback;

    private Runnable _callback = new Runnable() {
        @Override
        public void run() {
            setInitialButtonValues();
        }
    };

    public StrutFitButton(StrutFitButtonView button, Context context, int organizationID,
                          String shoeID, StrutFitEventListener strutFitEventListener,
                          Consumer<Boolean> buttonVisibleCallback) {

        _button = button;
        _context = context;
        _buttonVisibleCallback = buttonVisibleCallback;
        TAG = ((Activity) _context).getClass().getSimpleName();

        try {
            _buttonHelper = new StrutFitButtonHelper(context, _callback, organizationID, shoeID, strutFitEventListener);
            _buttonIsReady = true;
        } catch (Exception e) {
            Log.e(TAG, "Unable to construct button helper and initialize the button", e);
        }
    }

    private void setInitialButtonValues() {
        if(_buttonHelper.buttonIsVisible) {
            _button.setText(_buttonHelper.buttonText);
            _webviewUrl = _buttonHelper.webViewURL;
            _productType = _buttonHelper.productType;
            _isKids = _buttonHelper.isKids;
            _buttonVisibleCallback.accept(true);
        }
    }

    public void hideButton() {
        _button.setVisibility(View.GONE);
    }

    public void showButton() {
        _button.setVisibility(View.VISIBLE);
    }

    public void getSizeAndVisibility(String footMeasurementCode, String bodyMeasurementCode) throws Exception {
        _buttonHelper.getSizeAndVisibility(footMeasurementCode, bodyMeasurementCode, false);
    }
}
