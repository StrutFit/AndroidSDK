package strutfit.button.helpers;

import android.content.Context;
import android.util.Log;

import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import strutfit.button.R;
import strutfit.button.SizeUnit;
import strutfit.button.StrutFitEventListener;
import strutfit.button.clients.StrutFitClient;
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
    private StrutFitEventListener _strutFitEventListener;
    private String _sizeUnavailableText;
    private String _childPreSizeText;
    private String _childPostSizeText;
    private String _adultPreSizeText;
    private String _adultPostSizeText;

    private Context _context;
    private CompositeDisposable disposables = new CompositeDisposable();
    private Runnable _buttonDataCallback;

    public StrutFitButtonHelper (Context context, Runnable callback, int organizationID, String shoeID, StrutFitEventListener strutFitEventListener, String sizeUnavailableText, String childPreSizeText, String childPostSizeText, String adultPreSizeText, String adultPostSizeText) throws Exception {
        _organizationID = organizationID;
        _shoeID = shoeID;
        _context = context;
        _buttonDataCallback = callback;
        _strutFitEventListener = strutFitEventListener;

        _sizeUnavailableText = sizeUnavailableText;
        _childPreSizeText = childPreSizeText;
        _childPostSizeText = childPostSizeText;
        _adultPreSizeText = adultPreSizeText;
        _adultPostSizeText = adultPostSizeText ;

        String measurementCode = StrutFitCommonHelper.getLocalMcode(context);
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
                                Boolean _isKids = result != null ? result.getIsKids() : false;

                                // Set initial rendering parameters
                                buttonIsVisible = result != null ? result.getShow() : false;
                                buttonText = _isKids ? _childPreSizeText : _adultPreSizeText;

                                Random rand = new Random();
                                int int_random = rand.nextInt(99999);
                                webViewURL = String.format(_context.getResources().getString(R.string.webViewBaseUrl) +
                                        "%s?random=%s&organisationId=%s&shoeId=%s&inApp=true",
                                        _isKids ? "nkids" : "nadults", int_random, _organizationID, _shoeID);

                                // When a post message comes back from the modal with empty measurement code
                                if (!isInitializing) {
                                    StrutFitCommonHelper.setLocalMcode(_context, null);
                                }

                                _buttonDataCallback.run();
                                if(_strutFitEventListener != null) {
                                    _strutFitEventListener.onSizeEvent(null, null);
                                }
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
                                ButtonSizeResult _sizeData = result != null ? result.getSizeData() : null;
                                ButtonVisibilityResult _visibilityData = result != null ? result.getVisibilityData() : null;

                                //SizeData being null indicates invalid measurement code, so we want to clear this in the cache
                                if(_sizeData == null) {
                                    StrutFitCommonHelper.setLocalMcode(_context, null);
                                }

                                Boolean _isKids = _visibilityData != null ? _visibilityData.getIsKids() : false;
                                String _size = _sizeData != null ? _sizeData.getSize() : null;
                                int _sizeUnit = _sizeData != null ? _sizeData.getUnit() : 0;
                                Boolean _showWidthCategory = _sizeData != null ? _sizeData.getShowWidthCategory() : false;
                                String _widthAbbreviation = _sizeData != null ? _sizeData.getWidthAbbreviation() : "";
                                String _width = (!_showWidthCategory || _widthAbbreviation == null || _widthAbbreviation.isEmpty()) ? "" : _widthAbbreviation;


                                // Set initial rendering parameters
                                buttonIsVisible = _visibilityData != null ? _visibilityData.getShow() : false;

                                String _buttonText = _sizeUnavailableText;
                                if(_size != null && !_size.isEmpty()) {
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
                                    StrutFitCommonHelper.setLocalMcode(_context, measurementCode);
                                }

                                _buttonDataCallback.run();
                                if(_strutFitEventListener != null) {
                                    _strutFitEventListener.onSizeEvent(_size, SizeUnit.valueOf(_sizeUnit));
                                }
                        }
                            catch(Exception e) {
                                Log.e("StrutFitButtonHelper", "onError()", e);
                            }
                        }
                    }));
        }
    }
}
