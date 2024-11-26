package strutfit.button;

import com.google.gson.Gson;

import java.util.Arrays;

import strutfit.button.models.ButtonVisibilityResult;
import strutfit.button.models.CustomTextValue;

public class StrutFitGlobalState {
    private static StrutFitGlobalState instance;
    private CustomTextValue[] preLoginButtonTextAdultsTranslations;

    private CustomTextValue[] preLoginButtonTextKidsTranslations;

    private CustomTextValue[] buttonResultTextTranslations;

    private String unavailableSizeText;

    private StrutFitGlobalState() {}

    public static synchronized StrutFitGlobalState getInstance() {
        if (instance == null) {
            instance = new StrutFitGlobalState();
        }
        return instance;
    }

    //TODO: Translations
    public String getPreLoginButtonTextAdults() {
        if(preLoginButtonTextAdultsTranslations != null && preLoginButtonTextAdultsTranslations.length > 0) {
            CustomTextValue translation = Arrays.stream(preLoginButtonTextAdultsTranslations)
                    .filter(t -> t.isDefault) // Predicate
                    .findFirst()              // Get the first match
                    .orElse(null);
            if(translation != null) {
                return translation.text;
            }
        }

        return "What is my size?";
    }

    public String getPreLoginButtonTextKids() {
        if(preLoginButtonTextKidsTranslations != null && preLoginButtonTextKidsTranslations.length > 0) {
            CustomTextValue translation = Arrays.stream(preLoginButtonTextKidsTranslations)
                    .filter(t -> t.isDefault) // Predicate
                    .findFirst()              // Get the first match
                    .orElse(null);
            if(translation != null) {
                return translation.text;
            }
        }

        return "What is my child's size?";
    }

    public String getButtonResultText(String size, String sizeUnit, String width) {
        if(buttonResultTextTranslations != null && buttonResultTextTranslations.length > 0) {
            CustomTextValue translation = Arrays.stream(buttonResultTextTranslations)
                    .filter(t -> t.isDefault) // Predicate
                    .findFirst()              // Get the first match
                    .orElse(null);
            if(translation != null) {
                return translation.text.replace("@size", size).replace("@unit", sizeUnit).replace("@width", width);
            }
        }

        return "Your size in this style is " + size + " " + sizeUnit + " " + width;
    }

    public String getUnavailableSizeText() {
        return unavailableSizeText;
    }

    public void setButtonTexts(ButtonVisibilityResult visibilityResult) {
        // JSON to Object using Gson
        Gson gson = new Gson();
        preLoginButtonTextAdultsTranslations = gson.fromJson(visibilityResult.getPreLoginButtonTextAdultsTranslations(), CustomTextValue[].class);
        preLoginButtonTextKidsTranslations = gson.fromJson(visibilityResult.getPreLoginButtonTextKidsTranslations(), CustomTextValue[].class);
        buttonResultTextTranslations = gson.fromJson(visibilityResult.getButtonResultTextTranslations(), CustomTextValue[].class);
        unavailableSizeText = "Unavailable in your recommended size";
    }

}
