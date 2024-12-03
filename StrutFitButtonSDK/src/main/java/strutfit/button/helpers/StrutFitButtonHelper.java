package strutfit.button.helpers;

import android.content.Context;
import android.util.Log;

import strutfit.button.R;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import strutfit.button.StrutFitGlobalState;
import strutfit.button.enums.ProductType;
import strutfit.button.enums.SizeUnit;
import strutfit.button.StrutFitEventListener;
import strutfit.button.clients.StrutFitClient;
import strutfit.button.models.ButtonApparelSizeResult;
import strutfit.button.models.ButtonFootwearSizeResult;
import strutfit.button.models.ButtonVisibilityAndSizeResult;
import strutfit.button.models.ButtonVisibilityResult;

public class StrutFitButtonHelper {

    public boolean buttonIsVisible = false;
    public String buttonText;
    public String webViewURL;

    public ProductType productType = ProductType.Footwear;
    public Boolean isKids = false;

    private int  _organizationID;
    private String  _shoeID;
    private StrutFitEventListener _strutFitEventListener;

    private Context _context;
    private CompositeDisposable disposables = new CompositeDisposable();
    private Runnable _buttonDataCallback;

    public StrutFitButtonHelper (Context context,
                                 Runnable callback,
                                 int organizationID,
                                 String shoeID,
                                 StrutFitEventListener strutFitEventListener) throws Exception {
        _organizationID = organizationID;
        _shoeID = shoeID;
        _context = context;
        _buttonDataCallback = callback;
        _strutFitEventListener = strutFitEventListener;

        String footMeasurementCode = StrutFitCommonHelper.getLocalFootMCode(context);
        String bodyMeasurementCode = StrutFitCommonHelper.getLocalBodyMCode(context);
        getSizeAndVisibility(footMeasurementCode, bodyMeasurementCode,true);
    }

    public void getSizeAndVisibility(String footMeasurementCode, String bodyMeasurementCode, Boolean isInitializing) throws Exception  {

            disposables.add(StrutFitClient.getInstance(_context)
                .getButtonSizeAndVisibility(_organizationID, _shoeID, footMeasurementCode, bodyMeasurementCode)
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
                            ButtonFootwearSizeResult _footwearSizeData = result != null ? result.getFootwearSizeData() : null;
                            ButtonApparelSizeResult _apparelSizeData = result != null ? result.getApparelSizeData() : null;
                            ButtonVisibilityResult _visibilityData = result != null ? result.getVisibilityData() : null;

                            StrutFitGlobalState globalState = StrutFitGlobalState.getInstance();
                            if(_visibilityData != null) {
                                globalState.setButtonTexts(_visibilityData);
                            }

                            isKids = _visibilityData != null ? _visibilityData.getIsKids() : false;

                            // Set initial rendering parameters
                            productType = _visibilityData != null ? _visibilityData.getProductType() : ProductType.Footwear;
                            buttonIsVisible = _visibilityData != null ? _visibilityData.getShow() : false;

                            // When initializing we need to set the webview URL
                            if (isInitializing) {
                                webViewURL = _context.getResources().getString(R.string.webViewBaseUrl);
                            }

                            String _size = productType == ProductType.Footwear ?
                                    (_footwearSizeData != null ? _footwearSizeData.getSize() : null) :
                                    (_apparelSizeData != null ? _apparelSizeData.getSize() : null);
                            String _sizeUnit = productType == ProductType.Footwear ?
                                    (_footwearSizeData != null ? SizeUnit.getSizeUnitString(SizeUnit.valueOf(_footwearSizeData.getUnit())) : "") :
                                    "";
                            Boolean _showWidthCategory = _footwearSizeData != null ? _footwearSizeData.getShowWidthCategory() : false;
                            String _widthAbbreviation = _footwearSizeData != null ? _footwearSizeData.getWidthAbbreviation() : "";
                            String _width = (!_showWidthCategory || _widthAbbreviation == null || _widthAbbreviation.isEmpty()) ? "" : _widthAbbreviation;

                            String _buttonText = isKids ? globalState.getPreLoginButtonTextKids() : globalState.getPreLoginButtonTextAdults();

                            if(_footwearSizeData != null) {
                                _buttonText = globalState.getUnavailableSizeText();
                            }
                            if(_size != null && !_size.isEmpty()) {
                                _buttonText = globalState.getButtonResultText(_size, _sizeUnit, _width);
                            }
                            buttonText = _buttonText;

                            _buttonDataCallback.run();
                            if(_strutFitEventListener != null) {
                                _strutFitEventListener.onSizeEvent(_size, _sizeUnit);
                            }
                    }
                        catch(Exception e) {
                            Log.e("StrutFitButtonHelper", "onError()", e);
                        }
                    }
                }));
    }
}
