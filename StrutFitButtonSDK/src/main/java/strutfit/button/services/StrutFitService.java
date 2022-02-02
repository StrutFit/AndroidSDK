package strutfit.button.services;

import retrofit2.http.GET;
import retrofit2.http.Query;
import io.reactivex.rxjava3.core.Observable;
import strutfit.button.models.ButtonVisibilityAndSizeOutput;
import strutfit.button.models.ButtonVisibilityOutput;

public interface StrutFitService {
    @GET("api/MobileApp/DetermineButtonVisibility")
    Observable<ButtonVisibilityOutput> getButtonVisibility(@Query("OrganizationUnitId") int organizationID, @Query("Code") String shoeID);

    @GET("api/MobileApp/GetSizeandVisibility")
    Observable<ButtonVisibilityAndSizeOutput> getButtonSizeAndVisibility(@Query("OrganizationUnitId") int organizationID, @Query("Code") String shoeID, @Query("MCode") String measurementCode);

}
