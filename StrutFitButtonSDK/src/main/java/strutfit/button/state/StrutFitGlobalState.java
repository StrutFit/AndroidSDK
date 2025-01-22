package strutfit.button.state;

import android.content.Context;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Locale;

import strutfit.button.R;
import strutfit.button.enums.Language;
import strutfit.button.models.ButtonTheme;
import strutfit.button.models.ButtonVisibilityResult;
import strutfit.button.models.CustomTextValue;
import strutfit.button.models.StrutFitTheme;

public class StrutFitGlobalState {
    private static StrutFitGlobalState instance;
    private CustomTextValue[] preLoginButtonTextAdultsTranslations;

    private CustomTextValue[] preLoginButtonTextKidsTranslations;

    private CustomTextValue[] buttonResultTextTranslations;

    private String unavailableSizeText;

    private String themeJson;

    private ButtonTheme buttonTheme;

    private Boolean useCustomTheme;

    private StrutFitGlobalState() {}

    public static synchronized StrutFitGlobalState getInstance() {
        if (instance == null) {
            instance = new StrutFitGlobalState();
        }
        return instance;
    }

    public String getPreLoginButtonTextAdults(Context context) {
        Locale locale = context.getResources().getConfiguration().getLocales().get(0);
        String languageCode = locale.getLanguage();
        if(preLoginButtonTextAdultsTranslations != null && preLoginButtonTextAdultsTranslations.length > 0) {
            CustomTextValue translation = Arrays.stream(preLoginButtonTextAdultsTranslations)
                    .filter(t -> t.language == Language.valueFromCode(languageCode).getValue()) // Predicate
                    .findFirst()              // Get the first match
                    .orElse(null);
            if(translation != null) {
                return translation.text;
            }
        }

        return context.getString(R.string.WhatIsMySize);
    }

    public String getPreLoginButtonTextKids(Context context) {
        Locale locale = context.getResources().getConfiguration().getLocales().get(0);
        String languageCode = locale.getLanguage();
        if(preLoginButtonTextKidsTranslations != null && preLoginButtonTextKidsTranslations.length > 0) {
            CustomTextValue translation = Arrays.stream(preLoginButtonTextKidsTranslations)
                    .filter(t -> t.language == Language.valueFromCode(languageCode).getValue()) // Predicate
                    .findFirst()              // Get the first match
                    .orElse(null);
            if(translation != null) {
                return translation.text;
            }
        }

        return context.getString(R.string.WhatIsMySize_Kids);
    }

    public String getButtonResultText(Context context, String size, String sizeUnit, String width) {
        Locale locale = context.getResources().getConfiguration().getLocales().get(0);
        String languageCode = locale.getLanguage();
        if(buttonResultTextTranslations != null && buttonResultTextTranslations.length > 0) {
            CustomTextValue translation = Arrays.stream(buttonResultTextTranslations)
                    .filter(t -> t.language == Language.valueFromCode(languageCode).getValue()) // Predicate
                    .findFirst()              // Get the first match
                    .orElse(null);
            if(translation != null) {
                return translation.text.replace("@size", size).replace("@unit", sizeUnit).replace("@width", width).replace("  ", " ");
            }
        }

        return context.getString(R.string.YourStrutfitSize, size, sizeUnit, width).replace("  ", " ");
    }

    public String getUnavailableSizeText() {
        return unavailableSizeText;
    }

    public String getThemeJson() { return themeJson; }

    public ButtonTheme getButtonTheme() {
        return buttonTheme;
    }

    public Boolean getUseCustomTheme() {
        return useCustomTheme;
    }

    public void setButtonTexts(Context context, ButtonVisibilityResult visibilityResult) {
        // JSON to Object using Gson
        Gson gson = new Gson();
        preLoginButtonTextAdultsTranslations = gson.fromJson(visibilityResult.getPreLoginButtonTextAdultsTranslations(), CustomTextValue[].class);
        preLoginButtonTextKidsTranslations = gson.fromJson(visibilityResult.getPreLoginButtonTextKidsTranslations(), CustomTextValue[].class);
        buttonResultTextTranslations = gson.fromJson(visibilityResult.getButtonResultTextTranslations(), CustomTextValue[].class);
        unavailableSizeText = context.getString(R.string.UnavailableInYourSize);
    }

    public void setTheme(ButtonVisibilityResult visibilityResult) {
        themeJson = visibilityResult.getThemeData();
        useCustomTheme = visibilityResult.getUseCustomTheme();

        // JSON to Object using Gson
        Gson gson = new Gson();
        StrutFitTheme theme = gson.fromJson(visibilityResult.getThemeData(), StrutFitTheme.class);

        buttonTheme = theme.SFButton.stream().filter(ButtonTheme::isDefault).findFirst().orElse(theme.SFButton.isEmpty() ? null : theme.SFButton.get(0));
    }
}
