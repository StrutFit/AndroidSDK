package strutfit.button.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import strutfit.button.enums.OnlineScanInstructionsType;
import strutfit.button.enums.ProductType;

public class ButtonVisibilityResult {

    @SerializedName("Show")
    @Expose
    private Boolean show;
    @SerializedName("ProductType")
    @Expose
    private int productType;

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
    private String preLoginButtonTextAdultsTranslations;
    @SerializedName("PreLoginButtonTextKidsTranslations")
    @Expose
    private String preLoginButtonTextKidsTranslations;
    @SerializedName("ButtonResultTextTranslations")
    @Expose
    private String buttonResultTextTranslations;
    @SerializedName("ShowProductName")
    @Expose
    private Boolean showProductName;
    @SerializedName("ShowProductImage")
    @Expose
    private Boolean showProductImage;
    @SerializedName("HideSizeUnit")
    @Expose
    private Boolean hideSizeUnit;
    @SerializedName("HideOnDesktop")
    @Expose
    private Boolean hideOnDesktop;
    @SerializedName("ShowWidthUnavailableText")
    @Expose
    private Boolean showWidthUnavailableText;
    @SerializedName("ChartSpecificResultScreenText")
    @Expose
    private Object chartSpecificResultScreenText;
    @SerializedName("AdultsOnlineScanInstructionsType")
    @Expose
    private int adultsOnlineScanInstructionsType;
    @SerializedName("KidsOnlineScanInstructionsType")
    @Expose
    private int kidsOnlineScanInstructionsType;
    @SerializedName("ThemeData")
    @Expose
    private String themeData;

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public ProductType getProductType() {
        return ProductType.valueOf(productType);
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public OnlineScanInstructionsType getAdultsOnlineScanInstructionsType() {
        return OnlineScanInstructionsType.valueOf(adultsOnlineScanInstructionsType);
    }

    public void setAdultsOnlineScanInstructionsType(int onlineScanInstructionsType) {
        this.adultsOnlineScanInstructionsType = onlineScanInstructionsType;
    }

    public OnlineScanInstructionsType getKidsOnlineScanInstructionsType() {
        return OnlineScanInstructionsType.valueOf(kidsOnlineScanInstructionsType);
    }

    public void setKidsOnlineScanInstructionsType(int onlineScanInstructionsType) {
        this.kidsOnlineScanInstructionsType = onlineScanInstructionsType;
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

    public String getPreLoginButtonTextAdultsTranslations() {
        return preLoginButtonTextAdultsTranslations;
    }

    public void setPreLoginButtonTextAdultsTranslations(String preLoginButtonTextAdultsTranslations) {
        this.preLoginButtonTextAdultsTranslations = preLoginButtonTextAdultsTranslations;
    }

    public String getPreLoginButtonTextKidsTranslations() {
        return preLoginButtonTextKidsTranslations;
    }

    public void setPreLoginButtonTextKidsTranslations(String preLoginButtonTextKidsTranslations) {
        this.preLoginButtonTextKidsTranslations = preLoginButtonTextKidsTranslations;
    }

    public String getButtonResultTextTranslations() {
        return buttonResultTextTranslations;
    }

    public void setButtonResultTextTranslations(String buttonResultTextTranslations) {
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

    public Boolean getHideSizeUnit() {
        return hideSizeUnit;
    }

    public void setHideSizeUnit(Boolean hideSizeUnit) {
        this.hideSizeUnit = hideSizeUnit;
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

    public String getThemeData() { return themeData; }

    public void setThemeData(String themeData) {
        this.themeData = themeData;
    }


}
