package strutfit.button.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ButtonVisibilityAndSizeResult {

    @SerializedName("VisibilityData")
    @Expose
    private ButtonVisibilityResult visibilityData;
    @SerializedName("SizeData")
    @Expose
    private ButtonFootwearSizeResult sizeData;
    @SerializedName("ApparelSizeData")
    private ButtonApparelSizeResult apparelSizeData;

    public ButtonVisibilityResult getVisibilityData() {
        return visibilityData;
    }

    public void setVisibilityData(ButtonVisibilityResult visibilityData) {
        this.visibilityData = visibilityData;
    }

    public ButtonFootwearSizeResult getFootwearSizeData() {
        return sizeData;
    }

    public void setFootwearSizeData(ButtonFootwearSizeResult sizeData) {
        this.sizeData = sizeData;
    }

    public ButtonApparelSizeResult getApparelSizeData() {
        return apparelSizeData;
    }

    public void setApparelSizeData(ButtonApparelSizeResult apparelSizeData) {
        this.apparelSizeData = apparelSizeData;
    }

}