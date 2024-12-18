package strutfit.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import strutfit.button.models.ButtonTheme;
import strutfit.button.state.StrutFitGlobalState;

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

    public void setText(String text) {
        _text.setText(text);
    }

    public LinearLayout getButton() {
        return _button;
    }

    public void updateButtonDesign() {
        StrutFitGlobalState globalState = StrutFitGlobalState.getInstance();

        ButtonTheme buttonTheme = globalState.getButtonTheme();
        if(buttonTheme != null) {
            if(buttonTheme.Colors.SFButtonText != null && !buttonTheme.Colors.SFButtonText.isEmpty()) {
                try {
                    _text.setTextColor(Color.parseColor(buttonTheme.Colors.SFButtonText));
                } catch(Exception ignored) {
                }
            }

            if(buttonTheme.Colors.SFButtonBackground != null && !buttonTheme.Colors.SFButtonBackground.isEmpty()) {
                try {
                    _button.setBackgroundColor(Color.parseColor(buttonTheme.Colors.SFButtonBackground));
                } catch(Exception ignored) {
                }
            }

            if(buttonTheme.HideStrutFitButtonLogo) {
                _image.setVisibility(View.GONE);
            }

            if(buttonTheme.Fonts.PrimaryFontSize != null && !buttonTheme.Fonts.PrimaryFontSize.isEmpty()) {
                try {
                    int fontSize = Integer.parseInt(buttonTheme.Fonts.PrimaryFontSize.replaceAll("[^0-9]", ""));
                    _text.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                } catch(Exception ignored) {
                }
            }


        }
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.strutfit_button_view, this, true);
        _text = (TextView) findViewById(R.id.strutfit_button_text);
        _image = (ImageView) findViewById(R.id.strutfit_logo);
        _button = (LinearLayout) findViewById(R.id.strutfit_button);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StrutFitButtonView,
                0, 0);

        try {
            //logo
            boolean useWhiteLogo = a.getBoolean(R.styleable.StrutFitButtonView_useWhiteLogo, false);
            if (useWhiteLogo) {
                _image.setImageResource(R.drawable.ic_sf_btn_glyph_white);
            }

            //text color
            String textColor = a.getString(R.styleable.StrutFitButtonView_textColor);
            if(textColor != null && !textColor.isEmpty()) {
                _text.setTextColor(Color.parseColor(textColor));
            }

            //button colors
            int buttonColor = getResources().getColor(R.color.strutfit_button);
            int buttonPressedColor = getResources().getColor(R.color.strutfit_button_focused);

            String customButtonColor = a.getString(R.styleable.StrutFitButtonView_buttonColor);
            String customButtonPressedColor = a.getString(R.styleable.StrutFitButtonView_buttonPressedColor);

            if(customButtonColor != null && !customButtonColor.isEmpty()) {
                buttonColor = Color.parseColor(customButtonColor);
            }

            if(customButtonPressedColor != null && !customButtonPressedColor.isEmpty()) {
                buttonPressedColor = Color.parseColor(customButtonPressedColor);
            }

            _button.setBackground(this.getStateListDrawable(buttonColor, buttonPressedColor));

            //font
            if (a.hasValue(R.styleable.StrutFitButtonView_buttonTextFont)) {
                int fontId = a.getResourceId(R.styleable.StrutFitButtonView_buttonTextFont, -1);
                Typeface typeface = ResourcesCompat.getFont(context, fontId);
                _text.setTypeface(typeface);
            }

        } finally {
            a.recycle();
        }

    }

    /**
     * Get {@link StateListDrawable} given the {@code normalColor} and {@code pressedColor}
     * for dynamic button coloring
     *
     * @param normalColor  The color in normal state.
     * @param pressedColor The color in pressed state.
     * @return
     */
    private StateListDrawable getStateListDrawable(int normalColor, int pressedColor) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(pressedColor));
        stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(normalColor));
        return stateListDrawable;
    }
}
