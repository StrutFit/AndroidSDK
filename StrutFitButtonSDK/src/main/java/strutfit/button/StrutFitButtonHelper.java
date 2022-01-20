package strutfit.button;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import strutfit.button.models.ButtonSizeResult;
import strutfit.button.models.ButtonVisibilityAndSizeOutput;
import strutfit.button.models.ButtonVisibilityAndSizeResult;
import strutfit.button.models.ButtonVisibilityOutput;
import strutfit.button.models.ButtonVisibilityResult;

public class StrutFitButtonHelper {

    public boolean buttonIsVisible = false;
    public String buttonText;
    public String webViewURL;

    private final OkHttpClient client = new OkHttpClient();
    private int  _organizationID;
    private String  _shoeID;
    private String _sizeUnavailableText;
    private String _childPreSizeText;
    private String _childPostSizeText;
    private String _adultPreSizeText;
    private String _adultPostSizeText;

    private Context _context;
    private CompositeDisposable disposables = new CompositeDisposable();
    private Runnable _buttonDataCallback;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String Measurement_Code = "measurementCode";
    private static final String StrutFit_In_Use = "isStrutFitInUse";

    public StrutFitButtonHelper (Context context, Runnable callback, int organizationID, String shoeID, String sizeUnavailableText, String childPreSizeText, String childPostSizeText, String adultPreSizeText, String adultPostSizeText) throws Exception {
        _organizationID = organizationID;
        _shoeID = shoeID;
        _context = context;
        _buttonDataCallback = callback;

        _sizeUnavailableText = sizeUnavailableText;
        _childPreSizeText = childPreSizeText;
        _childPostSizeText = childPostSizeText;
        _adultPreSizeText = adultPreSizeText;
        _adultPostSizeText = adultPostSizeText ;

        String measurementCode = getMeasurementCodeLocally();
        getSizeAndVisibility(measurementCode, true);
    }

    public void getSizeAndVisibility(String measurementCode, Boolean isInitializing) throws Exception  {

        if(measurementCode.isEmpty())
        {
            disposables.add(StrutFitClient.getInstance(_context)
                    .getButtonVisibility(_organizationID, _shoeID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<ButtonVisibilityOutput>() {
                        @Override public void onComplete() {
                            Log.d("StrutFitButtonHelper", "onComplete()");
                            disposables.clear();
                        }

                        @Override public void onError(Throwable e) {
                            Log.e("StrutFitButtonHelper", "onError()", e);
                            disposables.clear();
                        }

                        @Override public void onNext(ButtonVisibilityOutput output) {
                            try {
                                ButtonVisibilityResult result = output.getResult();
                                Boolean _isKids = result.getIsKids();

                                // Set initial rendering parameters
                                buttonIsVisible = result.getShow();
                                buttonText = _isKids ? _childPreSizeText : _adultPreSizeText;

                                Random rand = new Random();
                                int int_random = rand.nextInt(99999);
                                webViewURL = String.format(_context.getResources().getString(R.string.webViewBaseUrl) +
                                        "%s?random=%s&organisationId=%s&shoeId=%s&inApp=true",
                                        _isKids ? "nkids" : "nadults", int_random, _organizationID, _shoeID);
                                _buttonDataCallback.run();
                            }
                            catch(Exception e) {
                                Log.e("StrutFitButtonHelper", "onError()", e);
                            }
                        }
                    }));
        }
        else
        {
            disposables.add(StrutFitClient.getInstance(_context)
                    .getButtonSizeAndVisibility(_organizationID, _shoeID, measurementCode)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<ButtonVisibilityAndSizeOutput>() {
                        @Override public void onComplete() {
                            Log.d("StrutFitButtonHelper", "onComplete()");
                            disposables.clear();
                        }

                        @Override public void onError(Throwable e) {
                            Log.e("StrutFitButtonHelper", "onError()", e);
                            disposables.clear();
                        }

                        @Override public void onNext(ButtonVisibilityAndSizeOutput output) {
                            try {
                                ButtonVisibilityAndSizeResult result = output.getResult();
                                ButtonSizeResult _sizeData = result.getSizeData();
                                ButtonVisibilityResult _visibilityData = result.getVisibilityData();


                                Boolean _isKids = _visibilityData.getIsKids();
                                String _size = _sizeData.getSize();
                                int _sizeUnit = _sizeData.getUnit();
                                Boolean _showWidthCategory = _sizeData.getShowWidthCategory();
                                String _widthAbbreviation = _sizeData.getWidthAbbreviation();
                                String _width = (!_showWidthCategory || _widthAbbreviation.isEmpty() || _widthAbbreviation == "null")? "" : _widthAbbreviation;


                                // Set initial rendering parameters
                                buttonIsVisible = _visibilityData.getShow();

                                String _buttonText = _sizeUnavailableText;
                                if(!_size.isEmpty() && _size != "null") {
                                    _buttonText = _isKids ? String.format("%s %s %s %s", _childPostSizeText, _size, SizeUnit.getSizeUnitString(SizeUnit.valueOf(_sizeUnit)), _width) : String.format("%s %s %s %s", _adultPostSizeText,  _size, SizeUnit.getSizeUnitString(SizeUnit.valueOf(_sizeUnit)), _width);
                                }
                                buttonText = _buttonText;

                                // When initializing we need to set the webview URL
                                if (isInitializing) {
                                    Random rand = new Random();
                                    int int_random = rand.nextInt(99999);
                                    webViewURL = String.format(_context.getResources().getString(R.string.webViewBaseUrl) +
                                            "%s?random=%s&organisationId=%s&shoeId=%s&inApp=true",
                                            _isKids ? "nkids" : "nadults", int_random, _organizationID, _shoeID);
                                }

                                // When a post message comes back from the modal with a new measurement code
                                if (!isInitializing) {
                                    setMeasurementCodeLocally(measurementCode);
                                    setStrutFitInUseLocally(true);
                                }

                                _buttonDataCallback.run();
                        }
                            catch(Exception e) {
                                Log.e("StrutFitButtonHelper", "onError()", e);
                            }
                        }
                    }));
        }
    }

    private String getMeasurementCodeLocally() {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Measurement_Code, "");
    }

    private void setMeasurementCodeLocally(String measurementCode) {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Measurement_Code, measurementCode);

        editor.apply();
    }

    private void setStrutFitInUseLocally(Boolean inUse) {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(StrutFit_In_Use, inUse);

        editor.apply();
    }
}
