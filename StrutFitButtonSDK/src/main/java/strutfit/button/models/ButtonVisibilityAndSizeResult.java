package strutfit.button.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ButtonVisibilityAndSizeResult {

    @SerializedName("visibilityData")
    @Expose
    private ButtonVisibilityResult visibilityData;
    @SerializedName("sizeData")
    @Expose
    private ButtonSizeResult sizeData;

    public ButtonVisibilityResult getVisibilityData() {
        return visibilityData;
    }

    public void setVisibilityData(ButtonVisibilityResult visibilityData) {
        this.visibilityData = visibilityData;
    }

    public ButtonSizeResult getSizeData() {
        return sizeData;
    }

    public void setSizeData(ButtonSizeResult sizeData) {
        this.sizeData = sizeData;
    }

}