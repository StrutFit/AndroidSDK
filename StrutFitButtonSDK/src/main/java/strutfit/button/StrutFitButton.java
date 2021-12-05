package strutfit.button;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Optional;

public class StrutFitButton {

    public Boolean _buttonIsReady = false;
    public String _webviewUrl = "";
    public Button _button;
    public int _minWidth;
    public int _maxWidth;
    public int _minHeight;
    public int _maxHeight;
    public String _backGroundColor;
    public StrutFitButtonHelper _buttonHelper;

    public StrutFitButton(Button button, int minWidth, int maxWidth, int minHeight, int maxHeight, String backGroundColor,
                          Context context, int organizationID, String shoeID,
                          String sizeUnavailableText, String childPreSizeText, String childPostSizeText, String adultPreSizeText, String adultPostSizeText) {

        _button = button;
        _minWidth = minWidth;
        _maxWidth = maxWidth;
        _minHeight = minHeight;
        _maxHeight = maxHeight;
        _backGroundColor = backGroundColor;

        try {
            _buttonHelper = new StrutFitButtonHelper(context, organizationID, shoeID, sizeUnavailableText, childPreSizeText, childPostSizeText, adultPreSizeText, adultPostSizeText);
            _webviewUrl = _buttonHelper.webViewURL;
            _buttonIsReady = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SetInitialButtonUI() {
        _button.setMinimumWidth(_minWidth);
        _button.setMaxWidth(_maxWidth);
        _button.setMinimumHeight(_minHeight);
        _button.setMaxHeight(_maxHeight);
        _button.setBackgroundColor(Color.parseColor((_backGroundColor)));
        _button.setTextSize(12);

        if(_buttonHelper.buttonIsVisible) {
            _button.setText(_buttonHelper.buttonText);
            _button.setVisibility(View.VISIBLE);
        } else {
            _button.setVisibility(View.GONE);
        }
    }

    public void HideButton() {
        _button.setVisibility(View.GONE);
    }

    public void ShowButton() {
        _button.setVisibility(View.VISIBLE);
    }

    public void GetSizeAndVisibility(String MeasurementCode) throws Exception {
        _buttonHelper.GetSizeAndVisibility(MeasurementCode, false);
        _button.setText(_buttonHelper.buttonText);
    }
}
