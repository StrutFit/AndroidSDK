package strutfit.button;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import strutfit.button.helpers.StrutFitCommonHelper;
import strutfit.button.models.ButtonVisibilityAndSizeOutput;;
import strutfit.button.models.ConversionItem;
import strutfit.button.clients.PixelClient;

public class StrutFitTracking {

    private Context _context;
    private String _endPoint;
    private int _organizationId;
    private Gson _gson;
    private CompositeDisposable disposables = new CompositeDisposable();

    public StrutFitTracking (Context context, int organizationId) {
        _endPoint  = context.getResources().getString(R.string.consversionUrl);
        _context = context;
        _organizationId = organizationId;
        _gson = new Gson();
    }

    public void registerOrder (String orderReference, float orderValue, String currencyCode, ArrayList<ConversionItem> items ) {

        // Construct conversion data
        pixelData data = new pixelData();

        data.organizationId = _organizationId;
        data.sfEnabled = StrutFitCommonHelper.getStrutFitInUse(_context);
        data.orderRef = orderReference;
        data.orderValue = orderValue;
        data.mCode = StrutFitCommonHelper.getLocalMcode(_context);
        data.items = _gson.toJson(items);
        data.currencyCode = currencyCode;
        data.domain = _context.getResources().getString(R.string.conversiondomain);

        // Send data to conversion API
        String pixelJsonString = _gson.toJson(data);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String encodedString = Base64.getEncoder().encodeToString(pixelJsonString.getBytes());
            disposables.add(PixelClient.getInstance(_context)
                    .registerConversion(encodedString)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<JSONObject>() {
                        @Override
                        public void onComplete() {
                            Log.d("StrutFitTracker", "onComplete()");
                            disposables.clear();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("StrutFitTracker", "onError()", e);
                            disposables.clear();
                        }

                        @Override
                        public void onNext(JSONObject output) {
                            try {

                            } catch (Exception e) {
                                Log.e("StrutFitTracker", "onError()", e);
                            }
                        }
                    }));
        }
    }

    public class pixelData {

        public int organizationId;

        public Boolean sfEnabled;

        public String orderRef;

        public Float orderValue;

        public String mCode;

        public String items;

        public String currencyCode;

        public String domain;
    }
}
