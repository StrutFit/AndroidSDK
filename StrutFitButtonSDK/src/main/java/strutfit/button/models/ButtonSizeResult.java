package strutfit.button.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ButtonSizeResult {

    @SerializedName("footLength")
    @Expose
    private Double footLength;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("unit")
    @Expose
    private Integer unit;
    @SerializedName("growthLength")
    @Expose
    private Double growthLength;
    @SerializedName("productHasWidth")
    @Expose
    private Boolean productHasWidth;
    @SerializedName("usersWidthCategory")
    @Expose
    private String usersWidthCategory;
    @SerializedName("widthCategory")
    @Expose
    private String widthCategory;
    @SerializedName("widthAbbreviation")
    @Expose
    private String widthAbbreviation;
    @SerializedName("showWidthCategory")
    @Expose
    private Boolean showWidthCategory;
    @SerializedName("maxWidth")
    @Expose
    private Double maxWidth;
    @SerializedName("productName")
    @Expose
    private Object productName;
    @SerializedName("productImageURL")
    @Expose
    private Object productImageURL;
    @SerializedName("isValid")
    @Expose
    private Boolean isValid;
    @SerializedName("showProductName")
    @Expose
    private Boolean showProductName;
    @SerializedName("showProductImage")
    @Expose
    private Boolean showProductImage;
    @SerializedName("hideDefaultSizeUnit")
    @Expose
    private Boolean hideDefaultSizeUnit;
    @SerializedName("hideOnDesktop")
    @Expose
    private Boolean hideOnDesktop;
    @SerializedName("showWidthUnavailableText")
    @Expose
    private Boolean showWidthUnavailableText;
    @SerializedName("chartSpecificResultScreenText")
    @Expose
    private Object chartSpecificResultScreenText;

    public Double getFootLength() {
        return footLength;
    }

    public void setFootLength(Double footLength) {
        this.footLength = footLength;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public Double getGrowthLength() {
        return growthLength;
    }

    public void setGrowthLength(Double growthLength) {
        this.growthLength = growthLength;
    }

    public Boolean getProductHasWidth() {
        return productHasWidth;
    }

    public void setProductHasWidth(Boolean productHasWidth) {
        this.productHasWidth = productHasWidth;
    }

    public String getUsersWidthCategory() {
        return usersWidthCategory;
    }

    public void setUsersWidthCategory(String usersWidthCategory) {
        this.usersWidthCategory = usersWidthCategory;
    }

    public String getWidthCategory() {
        return widthCategory;
    }

    public void setWidthCategory(String widthCategory) {
        this.widthCategory = widthCategory;
    }

    public String getWidthAbbreviation() {
        return widthAbbreviation;
    }

    public void setWidthAbbreviation(String widthAbbreviation) {
        this.widthAbbreviation = widthAbbreviation;
    }

    public Boolean getShowWidthCategory() {
        return showWidthCategory;
    }

    public void setShowWidthCategory(Boolean showWidthCategory) {
        this.showWidthCategory = showWidthCategory;
    }

    public Double getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(Double maxWidth) {
        this.maxWidth = maxWidth;
    }

    public Object getProductName() {
        return productName;
    }

    public void setProductName(Object productName) {
        this.productName = productName;
    }

    public Object getProductImageURL() {
        return productImageURL;
    }

    public void setProductImageURL(Object productImageURL) {
        this.productImageURL = productImageURL;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
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