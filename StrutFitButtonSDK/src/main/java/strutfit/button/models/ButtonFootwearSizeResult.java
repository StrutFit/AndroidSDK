package strutfit.button.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ButtonFootwearSizeResult {
    @SerializedName("Size")
    @Expose
    private String size;
    @SerializedName("Unit")
    @Expose
    private Integer unit;
    @SerializedName("WidthAbbreviation")
    @Expose
    private String widthAbbreviation;
    @SerializedName("ShowWidthCategory")
    @Expose
    private Boolean showWidthCategory;

    public String getSize() {
        return size;
    }

    public Integer getUnit() {
        return unit;
    }

    public String getWidthAbbreviation() {
        return widthAbbreviation;
    }

    public Boolean getShowWidthCategory() {
        return showWidthCategory;
    }

}