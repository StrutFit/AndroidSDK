package strutfit.button.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ButtonVisibilityResult {

    @SerializedName("Show")
    @Expose
    private Boolean show;
    @SerializedName("IsKids")
    @Expose
    private Boolean isKids;
    @SerializedName("PreLoginButtonTextAdults")
    @Expose
    private String preLoginButtonTextAdults;
    @SerializedName("PreLoginButtonTextKids")
    @Expose
    private String preLoginButtonTextKids;
    @SerializedName("PreLoginButtonTextAdultsTranslations")
    @Expose
    private Object preLoginButtonTextAdultsTranslations;
    @SerializedName("PreLoginButtonTextKidsTranslations")
    @Expose
    private Object preLoginButtonTextKidsTranslations;
    @SerializedName("ButtonResultTextTranslations")
    @Expose
    private Object buttonResultTextTranslations;
    @SerializedName("ShowProductName")
    @Expose
    private Boolean showProductName;
    @SerializedName("ShowProductImage")
    @Expose
    private Boolean showProductImage;
    @SerializedName("HideDefaultSizeUnit")
    @Expose
    private Boolean hideDefaultSizeUnit;
    @SerializedName("HideOnDesktop")
    @Expose
    private Boolean hideOnDesktop;
    @SerializedName("ShowWidthUnavailableText")
    @Expose
    private Boolean showWidthUnavailableText;
    @SerializedName("ChartSpecificResultScreenText")
    @Expose
    private Object chartSpecificResultScreenText;

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public Boolean getIsKids() {
        return isKids;
    }

    public void setIsKids(Boolean isKids) {
        this.isKids = isKids;
    }

    public String getPreLoginButtonTextAdults() {
        return preLoginButtonTextAdults;
    }

    public void setPreLoginButtonTextAdults(String preLoginButtonTextAdults) {
        this.preLoginButtonTextAdults = preLoginButtonTextAdults;
    }

    public String getPreLoginButtonTextKids() {
        return preLoginButtonTextKids;
    }

    public void setPreLoginButtonTextKids(String preLoginButtonTextKids) {
        this.preLoginButtonTextKids = preLoginButtonTextKids;
    }

    public Object getPreLoginButtonTextAdultsTranslations() {
        return preLoginButtonTextAdultsTranslations;
    }

    public void setPreLoginButtonTextAdultsTranslations(Object preLoginButtonTextAdultsTranslations) {
        this.preLoginButtonTextAdultsTranslations = preLoginButtonTextAdultsTranslations;
    }

    public Object getPreLoginButtonTextKidsTranslations() {
        return preLoginButtonTextKidsTranslations;
    }

    public void setPreLoginButtonTextKidsTranslations(Object preLoginButtonTextKidsTranslations) {
        this.preLoginButtonTextKidsTranslations = preLoginButtonTextKidsTranslations;
    }

    public Object getButtonResultTextTranslations() {
        return buttonResultTextTranslations;
    }

    public void setButtonResultTextTranslations(Object buttonResultTextTranslations) {
        this.buttonResultTextTranslations = buttonResultTextTranslations;
    }

    public Boolean getShowProductName() {
        return showProductName;
    }

    public void setShowProductName(Boolean showProductName) {
        this.showProductName = showProductName;
    }

    public Boolean getShowProductImage() {
        return showProductImage;
    }

    public void setShowProductImage(Boolean showProductImage) {
        this.showProductImage = showProductImage;
    }

    public Boolean getHideDefaultSizeUnit() {
        return hideDefaultSizeUnit;
    }

    public void setHideDefaultSizeUnit(Boolean hideDefaultSizeUnit) {
        this.hideDefaultSizeUnit = hideDefaultSizeUnit;
    }

    public Boolean getHideOnDesktop() {
        return hideOnDesktop;
    }

    public void setHideOnDesktop(Boolean hideOnDesktop) {
        this.hideOnDesktop = hideOnDesktop;
    }

    public Boolean getShowWidthUnavailableText() {
        return showWidthUnavailableText;
    }

    public void setShowWidthUnavailableText(Boolean showWidthUnavailableText) {
        this.showWidthUnavailableText = showWidthUnavailableText;
    }

    public Object getChartSpecificResultScreenText() {
        return chartSpecificResultScreenText;
    }

    public void setChartSpecificResultScreenText(Object chartSpecificResultScreenText) {
        this.chartSpecificResultScreenText = chartSpecificResultScreenText;
    }

}
