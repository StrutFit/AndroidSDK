package strutfit.button;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
                            Context context, int organizationID, String shoeID, boolean isDev) {

        _button = button;
        _minWidth = minWidth;
        _maxWidth = maxWidth;
        _minHeight = minHeight;
        _maxHeight = maxHeight;
        _backGroundColor = backGroundColor;

        try {
            _buttonHelper = new StrutFitButtonHelper(context, organizationID, shoeID, isDev);
            _webviewUrl = _buttonHelper.webViewURL;
            _buttonIsReady = true;
        } catch (Exception e) {
            _buttonIsReady = true;
        }
    }

    public void setInitialButtonUI() {
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
}
