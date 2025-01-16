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
    @SerializedName("BrandName")
    @Expose
    private String brandName;
    @SerializedName("IsScanningEnabled")
    @Expose
    private Boolean isScanningEnabled;
    @SerializedName("IsSizeGuideEnabled")
    @Expose
    private Boolean isSizeGuideEnabled;
    @SerializedName("IsUsualSizeEnabled")
    @Expose
    private Boolean isUsualSizeEnabled;
    @SerializedName("UsualSizeMethods")
    @Expose
    private int[] usualSizeMethods;


    public Boolean getShow() {
        return show;
    }

    public ProductType getProductType() {
        return ProductType.valueOf(productType);
    }

    public OnlineScanInstructionsType getAdultsOnlineScanInstructionsType() {
        return OnlineScanInstructionsType.valueOf(adultsOnlineScanInstructionsType);
    }

    public OnlineScanInstructionsType getKidsOnlineScanInstructionsType() {
        return OnlineScanInstructionsType.valueOf(kidsOnlineScanInstructionsType);
    }

    public Boolean getIsKids() {
        return isKids;
    }

    public String getPreLoginButtonTextAdults() {
        return preLoginButtonTextAdults;
    }

    public String getPreLoginButtonTextKids() {
        return preLoginButtonTextKids;
    }

    public String getPreLoginButtonTextAdultsTranslations() {
        return preLoginButtonTextAdultsTranslations;
    }

    public String getPreLoginButtonTextKidsTranslations() {
        return preLoginButtonTextKidsTranslations;
    }

    public String getButtonResultTextTranslations() {
        return buttonResultTextTranslations;
    }

    public Boolean getShowProductName() {
        return showProductName;
    }

    public Boolean getShowProductImage() {
        return showProductImage;
    }

    public Boolean getHideSizeUnit() {
        return hideSizeUnit;
    }

    public Boolean getShowWidthUnavailableText() {
        return showWidthUnavailableText;
    }

    public Object getChartSpecificResultScreenText() {
        return chartSpecificResultScreenText;
    }

    public String getThemeData() { return themeData; }

    public String getBrandName() {
        return brandName;
    }

    public Boolean getScanningEnabled() {
        return isScanningEnabled;
    }

    public Boolean getSizeGuideEnabled() {
        return isSizeGuideEnabled;
    }

    public Boolean getUsualSizeEnabled() {
        return isUsualSizeEnabled;
    }

    public int[] getUsualSizeMethods() {
        return usualSizeMethods;
    }
}
