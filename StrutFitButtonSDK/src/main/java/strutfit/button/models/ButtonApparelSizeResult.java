package strutfit.button.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ButtonApparelSizeResult {
    @SerializedName("Size")
    @Expose
    private String size;

    @SerializedName("SizeUnitText")
    @Expose
    private String sizeUnitText;

    @SerializedName("IsValid")
    @Expose
    private Boolean isValid;

    @SerializedName("SizedForMeasurementCode")
    @Expose
    private String sizedForMeasurementCode;

    @SerializedName("OrganizationUnitId")
    @Expose
    private long organizationUnitId;

    @SerializedName("ProductCode")
    @Expose
    private String productCode;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }
}
