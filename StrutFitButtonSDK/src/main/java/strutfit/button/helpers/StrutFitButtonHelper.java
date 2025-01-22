package strutfit.button.helpers;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.util.function.Consumer;

import strutfit.button.R;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import strutfit.button.StrutFitButtonView;
import strutfit.button.state.StrutFitGlobalState;
import strutfit.button.enums.ProductType;
import strutfit.button.enums.SizeUnit;
import strutfit.button.clients.StrutFitClient;
import strutfit.button.models.ButtonApparelSizeResult;
import strutfit.button.models.ButtonFootwearSizeResult;
import strutfit.button.models.ButtonVisibilityAndSizeResult;
import strutfit.button.models.ButtonVisibilityResult;

public class StrutFitButtonHelper {
    public String buttonText;
    public String webViewURL;

    public ButtonVisibilityResult visibilityData = null;

    public StrutFitButtonView _sfButtonView;

    private int  _organizationID;
    private String _productCode;

    private SizeUnit _sizeUnit;
    private String _apparelSizeUnit;

    private ProductType _productType = ProductType.Footwear;
    private Boolean _isKids = false;
    private boolean _buttonIsVisible = false;

    private Context _context;
    private CompositeDisposable disposables = new CompositeDisposable();
    private Consumer<Boolean> _buttonVisibleCallback;

    public StrutFitButtonHelper(StrutFitButtonView sfButtonView, Context context, int organizationID,
                                 String productCode, SizeUnit sizeUnit, String apparelSizeUnit, Consumer<Boolean> buttonVisibleCallback) throws Exception {
        _organizationID = organizationID;
        _productCode = productCode;
        _sizeUnit = sizeUnit;
        _apparelSizeUnit = apparelSizeUnit;
        _context = context;
        _buttonVisibleCallback = buttonVisibleCallback;
        _sfButtonView = sfButtonView;

        String footMeasurementCode = StrutFitCommonHelper.getLocalFootMCode(context);
        String bodyMeasurementCode = StrutFitCommonHelper.getLocalBodyMCode(context);
        getSizeAndVisibility(footMeasurementCode, bodyMeasurementCode,true);
    }

    public void getSizeAndVisibility(String footMeasurementCode, String bodyMeasurementCode, Boolean isInitializing) throws Exception  {

            disposables.add(StrutFitClient.getInstance(_context)
                .getButtonSizeAndVisibility(_organizationID, _productCode, footMeasurementCode, bodyMeasurementCode, _sizeUnit, _apparelSizeUnit)
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
                            if (result == null) {
                                return;
                            }

                            ButtonFootwearSizeResult _footwearSizeData = result.getFootwearSizeData();
                            ButtonApparelSizeResult _apparelSizeData = result.getApparelSizeData();
                            visibilityData = result.getVisibilityData();

                            StrutFitGlobalState globalState = StrutFitGlobalState.getInstance();
                            if(visibilityData != null) {
                                globalState.setButtonTexts(_context, visibilityData);
                                globalState.setTheme(visibilityData);
                            }

                            // Set initial rendering parameters
                            _isKids = visibilityData != null ? visibilityData.getIsKids() : false;
                            _productType = visibilityData != null ? visibilityData.getProductType() : ProductType.Footwear;
                            _buttonIsVisible = visibilityData != null ? visibilityData.getShow() : false;

                            boolean _hideSizeUnit = visibilityData != null ? visibilityData.getHideSizeUnit() : false;

                            // When initializing we need to set the webview URL
                            if (isInitializing) {
                                webViewURL = _context.getResources().getString(R.string.webViewBaseUrl);
                            }

                            String _size = _productType == ProductType.Footwear ?
                                    (_footwearSizeData != null ? _footwearSizeData.getSize() : null) :
                                    (_apparelSizeData != null ? _apparelSizeData.getSize() : null);
                            String _sizeUnit = _productType == ProductType.Footwear ?
                                    (_footwearSizeData != null && !_hideSizeUnit ? SizeUnit.getSizeUnitString(SizeUnit.valueOf(_footwearSizeData.getUnit())) : "") :
                                    "";
                            boolean _showWidthCategory = _footwearSizeData != null ? _footwearSizeData.getShowWidthCategory() : false;
                            String _widthAbbreviation = _footwearSizeData != null ? _footwearSizeData.getWidthAbbreviation() : "";
                            String _width = (!_showWidthCategory || _widthAbbreviation == null || _widthAbbreviation.isEmpty()) ? "" : _widthAbbreviation;

                            String _buttonText = _isKids ? globalState.getPreLoginButtonTextKids(_context) : globalState.getPreLoginButtonTextAdults(_context);

                            if(_footwearSizeData != null) {
                                _buttonText = globalState.getUnavailableSizeText();
                            }
                            if(_size != null && !_size.isEmpty()) {
                                _buttonText = globalState.getButtonResultText(_context, _size, _sizeUnit, _width);
                            }
                            buttonText = _buttonText;

                            if(_buttonIsVisible) {
                                _sfButtonView.setText(buttonText);
                                if(isInitializing) {
                                    _sfButtonView.updateButtonDesign();
                                    _buttonVisibleCallback.accept(true);
                                }
                            }
                    }
                        catch(Exception e) {
                            Log.e("StrutFitButtonHelper", "onError()", e);
                        }
                    }
                }));
    }

    public void hideButton() {
        _sfButtonView.setVisibility(View.GONE);
    }

    public void showButton() {
        _sfButtonView.setVisibility(View.VISIBLE);
    }
}
