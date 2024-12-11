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
import strutfit.button.enums.OnlineScanInstructionsType;
import strutfit.button.enums.ProductType;
import strutfit.button.enums.SizeUnit;
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
    public OnlineScanInstructionsType onlineScanInstructionsType = OnlineScanInstructionsType.OneFootOnPaper;

    public StrutFitButtonView _sfButtonView;

    private int  _organizationID;
    private String _productCode;

    private Context _context;
    private CompositeDisposable disposables = new CompositeDisposable();
    private Consumer<Boolean> _buttonVisibleCallback;

    public StrutFitButtonHelper (StrutFitButtonView sfButtonView, Context context, int organizationID,
                                 String productCode, Consumer<Boolean> buttonVisibleCallback) throws Exception {
        _organizationID = organizationID;
        _productCode = productCode;
        _context = context;
        _buttonVisibleCallback = buttonVisibleCallback;
        _sfButtonView = sfButtonView;

        String footMeasurementCode = StrutFitCommonHelper.getLocalFootMCode(context);
        String bodyMeasurementCode = StrutFitCommonHelper.getLocalBodyMCode(context);
        getSizeAndVisibility(footMeasurementCode, bodyMeasurementCode,true);
    }

    public void getSizeAndVisibility(String footMeasurementCode, String bodyMeasurementCode, Boolean isInitializing) throws Exception  {

            disposables.add(StrutFitClient.getInstance(_context)
                .getButtonSizeAndVisibility(_organizationID, _productCode, footMeasurementCode, bodyMeasurementCode)
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
                                globalState.setButtonTexts(_context, _visibilityData);
                            }

                            isKids = _visibilityData != null ? _visibilityData.getIsKids() : false;

                            // Set initial rendering parameters
                            productType = _visibilityData != null ? _visibilityData.getProductType() : ProductType.Footwear;
                            onlineScanInstructionsType =  _visibilityData != null ?
                                    (isKids ?
                                            _visibilityData.getKidsOnlineScanInstructionsType() :
                                            _visibilityData.getAdultsOnlineScanInstructionsType()) :
                                    OnlineScanInstructionsType.OneFootOnPaper;
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

                            String _buttonText = isKids ? globalState.getPreLoginButtonTextKids(_context) : globalState.getPreLoginButtonTextAdults(_context);

                            if(_footwearSizeData != null) {
                                _buttonText = globalState.getUnavailableSizeText();
                            }
                            if(_size != null && !_size.isEmpty()) {
                                _buttonText = globalState.getButtonResultText(_context, _size, _sizeUnit, _width);
                            }
                            buttonText = _buttonText;

                            setInitialButtonValues();
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

    private void setInitialButtonValues() {
        if(buttonIsVisible) {
            _sfButtonView.setText(buttonText);
            _buttonVisibleCallback.accept(true);
        }
    }
}
