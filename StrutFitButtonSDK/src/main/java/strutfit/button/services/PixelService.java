package strutfit.button.services;

import org.json.JSONObject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PixelService {
    @GET("ingest")
    Completable registerConversion(@Query("token") String token);
}
