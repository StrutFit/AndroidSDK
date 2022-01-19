package strutfit.button;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import io.reactivex.rxjava3.core.Observable;
import strutfit.button.models.ButtonVisibilityAndSizeOutput;
import strutfit.button.models.ButtonVisibilityOutput;

public class StrutFitClient {
    private static final String STRUTFIT_BASE_URL = "https://api-prod.strut.fit/";

    private static StrutFitClient instance;
    private StrutFitService strutFitService;

    private StrutFitClient() {
        final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(STRUTFIT_BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        strutFitService = retrofit.create(StrutFitService.class);
    }

    public static StrutFitClient getInstance() {
        if (instance == null) {
            instance = new StrutFitClient();
        }
        return instance;
    }

    public Observable<ButtonVisibilityOutput> getButtonVisibility(int organizationUnitId, String shoeId) {
        return strutFitService.getButtonVisibility(organizationUnitId, shoeId);
    }

    public Observable<ButtonVisibilityAndSizeOutput> getButtonSizeAndVisibility(int organizationUnitId, String shoeId, String measurementCode) {
        return strutFitService.getButtonSizeAndVisibility(organizationUnitId, shoeId, measurementCode);
    }
}
