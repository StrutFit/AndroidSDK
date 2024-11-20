package strutfit.button.services;

import retrofit2.http.GET;
import retrofit2.http.Query;
import io.reactivex.rxjava3.core.Observable;
import strutfit.button.models.ButtonVisibilityAndSizeOutput;
import strutfit.button.models.ButtonVisibilityAndSizeResult;
import strutfit.button.models.ButtonVisibilityOutput;

public interface StrutFitService {
    @GET("SFButton-Dev")
    Observable<ButtonVisibilityAndSizeResult> getButtonSizeAndVisibility(@Query("organizationUnitId") int organizationID, @Query("code") String shoeID, @Query("mcode") String measurementCode);
}
