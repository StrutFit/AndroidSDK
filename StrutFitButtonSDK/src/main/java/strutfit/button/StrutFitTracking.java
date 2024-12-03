package strutfit.button;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Base64;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import strutfit.button.helpers.StrutFitCommonHelper;
import strutfit.button.models.ConversionItem;
import strutfit.button.clients.PixelClient;
import strutfit.button.models.PixelData;

public class StrutFitTracking {
    private Context _context;
    private int _organizationId;
    private Gson _gson;
    private CompositeDisposable disposables = new CompositeDisposable();

    public StrutFitTracking(Context context, int organizationId) {
        _context = context;
        _organizationId = organizationId;
        _gson = new Gson();
    }

    public void registerOrder(String orderReference, float orderValue, String currencyCode, ArrayList<ConversionItem> items) {
        registerOrder(orderReference, orderValue, currencyCode, items, null);
    }

    public void registerOrder(String orderReference, float orderValue, String currencyCode, ArrayList<ConversionItem> items, String userEmail) {

        // Construct conversion data
        PixelData data = new PixelData();

        data.organizationUnitId = _organizationId;
        data.orderRef = orderReference;
        data.orderValue = orderValue;
        data.userId = StrutFitCommonHelper.getLocalUserId(_context);
        data.footScanMCode = StrutFitCommonHelper.getLocalFootMCode(_context);
        data.bodyScanMCode = StrutFitCommonHelper.getLocalBodyMCode(_context);
        data.items = _gson.toJson(items);
        data.currencyCode = currencyCode;
        data.isMobile = true;
        data.domain = _context.getPackageName();
        data.emailHash = userEmail != null ? hashCode(userEmail) : null;

        // Send data to conversion API
        String pixelJsonString = _gson.toJson(data);
        String encodedString = "";

        // Different API versions have different way to base64 strings
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            encodedString = Base64.getEncoder().encodeToString(pixelJsonString.getBytes());
        } else {
            encodedString = android.util.Base64.encodeToString(pixelJsonString.getBytes(), android.util.Base64.DEFAULT);
        }

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

    public static int hashCode(String str) {
        int hash = 0;
        for (int i = 0; i < str.length(); i++) {
            char chr = str.charAt(i);
            hash = (hash << 5) - hash + chr;
        }
        return hash;
    }
}
