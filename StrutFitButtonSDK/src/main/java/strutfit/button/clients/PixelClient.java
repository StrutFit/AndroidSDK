package strutfit.button.clients;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import io.reactivex.rxjava3.core.Completable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import io.reactivex.rxjava3.core.Observable;
import strutfit.button.services.PixelService;
import strutfit.button.R;

public class PixelClient {
    private static PixelClient instance;
    private PixelService pixelService;

    private PixelClient(Context context) {
        final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(context.getResources().getString(R.string.conversionUrl))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        pixelService = retrofit.create(PixelService.class);
    }

    public static PixelClient getInstance(Context context) {
        if (instance == null) {
            instance = new PixelClient(context);
        }
        return instance;
    }

    public Completable registerConversion(String token) {
        return pixelService.registerConversion(token);
    }
}
