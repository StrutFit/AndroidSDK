package strutfit.button.clients;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import io.reactivex.rxjava3.core.Observable;
import strutfit.button.R;
import strutfit.button.models.ButtonVisibilityAndSizeResult;
import strutfit.button.services.StrutFitService;

public class StrutFitClient {
    private static StrutFitClient instance;
    private StrutFitService strutFitService;

    private StrutFitClient(Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // Connection timeout
                .readTimeout(30, TimeUnit.SECONDS)    //
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    // Add the Origin header if necessary
                    Request newRequest = originalRequest.newBuilder()
                            .header("Origin", context.getPackageName())
                            .build();
                    return chain.proceed(newRequest);
                })
                .build();

        final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(context.getResources().getString(R.string.sfButtonUrl))
                .client(client)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        strutFitService = retrofit.create(StrutFitService.class);
    }

    public static StrutFitClient getInstance(Context context) {
        if (instance == null) {
            instance = new StrutFitClient(context);
        }
        return instance;
    }

    public Observable<ButtonVisibilityAndSizeResult> getButtonSizeAndVisibility(int organizationUnitId, String productCode, String footMeasurementCode, String bodyMeasurementCode) {
        return strutFitService.getButtonSizeAndVisibility(organizationUnitId, productCode, footMeasurementCode, bodyMeasurementCode);
    }
}
