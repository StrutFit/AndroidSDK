package strutfit.button.helpers;

import android.content.Context;
import android.util.Log;

import java.util.Random;

import strutfit.button.R;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import strutfit.button.StrutFitGlobalState;
import strutfit.button.enums.SizeUnit;
import strutfit.button.StrutFitEventListener;
import strutfit.button.clients.StrutFitClient;
import strutfit.button.models.ButtonSizeResult;
import strutfit.button.models.ButtonVisibilityAndSizeResult;
import strutfit.button.models.ButtonVisibilityResult;

public class StrutFitButtonHelper {

    public boolean buttonIsVisible = false;
    public String buttonText;
    public String webViewURL;
    private int  _organizationID;
    private String  _shoeID;
    private StrutFitEventListener _strutFitEventListener;

    private Context _context;
    private CompositeDisposable disposables = new CompositeDisposable();
    private Runnable _buttonDataCallback;

    public StrutFitButtonHelper (Context context, Runnable callback, int organizationID, String shoeID, StrutFitEventListener strutFitEventListener) throws Exception {
        _organizationID = organizationID;
        _shoeID = shoeID;
        _context = context;
        _buttonDataCallback = callback;
        _strutFitEventListener = strutFitEventListener;

        String measurementCode = StrutFitCommonHelper.getLocalMcode(context);
        getSizeAndVisibility(measurementCode, true);
    }

    public void getSizeAndVisibility(String measurementCode, Boolean isInitializing) throws Exception  {

            disposables.add(StrutFitClient.getInstance(_context)
                .getButtonSizeAndVisibility(_organizationID, _shoeID, measurementCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ButtonVisibilityAndSizeResult>() {
                    @Override public void onComplete() {
                        Log.d("StrutFitButtonHelper", "onComplete()");
                        disposables.clear();
                    }

                    @Override public void onError(Throwable e) {
                        Log.e("StrutFitButtonHelper", "onError()", e);
                        disposables.clear();
                    }

                    @Override public void onNext(ButtonVisibilityAndSizeResult result) {
                        try {
                            ButtonSizeResult _sizeData = result != null ? result.getSizeData() : null;
                            ButtonVisibilityResult _visibilityData = result != null ? result.getVisibilityData() : null;

                            StrutFitGlobalState globalState = StrutFitGlobalState.getInstance();
                            if(_visibilityData != null) {
                                globalState.setButtonTexts(_visibilityData);
                            }

                            Boolean _isKids = _visibilityData != null ? _visibilityData.getIsKids() : false;

                            // Set initial rendering parameters
                            buttonIsVisible = _visibilityData != null ? _visibilityData.getShow() : false;

                            // When initializing we need to set the webview URL
                            if (isInitializing) {
                                Random rand = new Random();
                                int int_random = rand.nextInt(99999);
                                webViewURL = _context.getResources().getString(R.string.webViewBaseUrl);
                            }

                            _buttonDataCallback.run();
                            if(_strutFitEventListener != null) {
                                _strutFitEventListener.onSizeEvent(null, null);
                            }

                            String _size = _sizeData != null ? _sizeData.getSize() : null;
                            int _sizeUnit = _sizeData != null ? _sizeData.getUnit() : 0;
                            Boolean _showWidthCategory = _sizeData != null ? _sizeData.getShowWidthCategory() : false;
                            String _widthAbbreviation = _sizeData != null ? _sizeData.getWidthAbbreviation() : "";
                            String _width = (!_showWidthCategory || _widthAbbreviation == null || _widthAbbreviation.isEmpty()) ? "" : _widthAbbreviation;

                            String _buttonText = _isKids ? globalState.getPreLoginButtonTextKids() : globalState.getPreLoginButtonTextAdults();

                            if(_sizeData != null) {
                                _buttonText = globalState.getUnavailableSizeText();
                            }
                            if(_size != null && !_size.isEmpty()) {
                                _buttonText = globalState.getButtonResultText(_size, SizeUnit.getSizeUnitString(SizeUnit.valueOf(_sizeUnit)), _width);
                            }
                            buttonText = _buttonText;

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
