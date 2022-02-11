package strutfit.button;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StrutFitButtonView extends LinearLayout {

    public StrutFitButtonView(Context context) {
        super(context);
        init(context, null);
    }

    public StrutFitButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private TextView _text;
    private ImageView _image;
    private LinearLayout _button;

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.strutfit_button_view, this, true);
        _text = (TextView) findViewById(R.id.strutfit_button_text);
        _image = (ImageView) findViewById(R.id.strutfit_logo);
        _button = (LinearLayout) findViewById(R.id.strutfit_button);

        _text.setTextSize(12);
    }

    public void setText(String text) {
        _text.setText(text);
    }

    public LinearLayout getButton() {
        return _button;
    }
}
