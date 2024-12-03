package strutfit.button.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    public ProductType getProductType() {
        return ProductType.valueOf(productType);
    }

    public void setProductType(int productType) {
        this.productType = productType;
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
