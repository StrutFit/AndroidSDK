package strutfit.button.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ButtonApparelSizeResult {
    @SerializedName("Size")
    @Expose
    private String size;

    public String getSize() {
        return size;
    }
}
