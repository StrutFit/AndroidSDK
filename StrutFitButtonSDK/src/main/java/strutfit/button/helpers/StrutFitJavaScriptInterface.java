package strutfit.button.helpers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import strutfit.button.enums.OnlineScanInstructionsType;
import strutfit.button.enums.PostMessageType;
import strutfit.button.enums.ProductType;
import strutfit.button.enums.SizeUnit;
import strutfit.button.models.ButtonVisibilityResult;
import strutfit.button.models.PostMessageInitialAppInfoDto;
import strutfit.button.models.PostMessageUpdateThemeDto;
import strutfit.button.models.PostMessageUserBodyMeasurementCodeDataDto;
import strutfit.button.models.PostMessageUserFootMeasurementCodeDataDto;
import strutfit.button.models.PostMessageUserIdDataDto;
import strutfit.button.state.StrutFitGlobalState;
import strutfit.button.ui.StrutFitButtonWebview;

public class StrutFitJavaScriptInterface {

    // SF Properties
    private StrutFitButtonHelper _sfButtonHelper;
    private StrutFitButtonWebview _sfWebView;
    private Context _context;
    private String TAG;

    private int _organizationId;
    private String _productCode;
    private SizeUnit _sizeUnit;
    private String _apparelSizeUnit;
    private String _productName;
    private String _productImageURL;

    private ButtonVisibilityResult _visibilityData;

    private Boolean _initialAppInfoSent = false;


    public StrutFitJavaScriptInterface(StrutFitButtonHelper sfButtonHelper, StrutFitButtonWebview sfWebView, Context context,
                                       int organizationId, String productCode, SizeUnit sizeUnit,
                                       String apparelSizeUnit, String productName, String productImageURL,
                                       ButtonVisibilityResult visibilityData) {
        _sfButtonHelper = sfButtonHelper;
        _sfWebView = sfWebView;
        _context = context;
        _organizationId = organizationId;
        _productCode = productCode;
        _sizeUnit = sizeUnit;
        _apparelSizeUnit = apparelSizeUnit;
        _productName = productName;
        _productImageURL = productImageURL;
        _visibilityData = visibilityData;
        TAG = ((Activity) _context).getClass().getSimpleName();
    }

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @JavascriptInterface
    public void receiveMessage(String message) {
        try {
            JSONObject json = new JSONObject(message);
            PostMessageType result = PostMessageType.valueOf(json.getInt("strutfitMessageType"));

            switch (result)
            {
                case UserAuthData:
                    //update uuid
                    updateUserId(message);
                    break;
                case UserFootMeasurementCodeData:
                    // update m-code
                    updateFootMeasurementCode(message);
                    break;
                case UserBodyMeasurementCodeData:
                    //update body m-code
                    updateBodyMeasurementCode(message);
                    break;
                case CloseIFrame:
                    closeModal();
                    break;
                case IframeReady:
                    if(!_initialAppInfoSent) {
                        boolean isKids = _visibilityData != null ? _visibilityData.getIsKids() : false;
                        PostMessageInitialAppInfoDto input = new PostMessageInitialAppInfoDto();
                        input.strutfitMessageType = PostMessageType.InitialAppInfo.getValue();
                        input.productType = _visibilityData != null ? _visibilityData.getProductType().getValue() : ProductType.Footwear.getValue();
                        input.isKids = isKids;
                        input.productId = _productCode;
                        input.productName = _productName != null && !_productName.trim().isEmpty() ?
                                _productName :
                                _visibilityData != null && _visibilityData.getUseStrutFitProductNameAsFallback() ?
                                        _visibilityData.getProductName() :
                                        "";
                        input.productImageURL =  _productImageURL != null && !_productImageURL.trim().isEmpty() ?
                                _productImageURL :
                                _visibilityData != null ?
                                        _visibilityData.getProductImageURL() :
                                        "";
                        input.organizationUnitId = _organizationId;
                        input.defaultSizeUnit = _sizeUnit != null ? _sizeUnit.getValue() : null;
                        input.defaultApparelSizeUnit = _apparelSizeUnit;
                        input.onlineScanInstructionsType = _visibilityData != null ?
                                (isKids ?
                                        _visibilityData.getKidsOnlineScanInstructionsType().getValue() :
                                        _visibilityData.getAdultsOnlineScanInstructionsType().getValue()) :
                                OnlineScanInstructionsType.OneFootOnPaper.getValue();
                        input.brandName = _visibilityData != null ? _visibilityData.getBrandName() : null;
                        input.hideScanning = _visibilityData != null && !_visibilityData.getScanningEnabled();
                        input.hideSizeGuide = _visibilityData != null && !_visibilityData.getSizeGuideEnabled();
                        input.hideUsualSize = _visibilityData == null || !_visibilityData.getUsualSizeEnabled();
                        input.usualSizeMethods = _visibilityData != null ? _visibilityData.getUsualSizeMethods() : null;
                        input.inApp = true;

                        _sfWebView.sendInitialAppInfo(input);

                        StrutFitGlobalState globalState = StrutFitGlobalState.getInstance();
                        if(globalState.getUseCustomTheme()) {
                            PostMessageUpdateThemeDto updateThemeInput = new PostMessageUpdateThemeDto();
                            updateThemeInput.strutfitMessageType = PostMessageType.UpdateTheme.getValue();

                            JsonParser jsonParser = new JsonParser();
                            updateThemeInput.theme = jsonParser.parse(globalState.getThemeJson());

                            _sfWebView.sendTheme(updateThemeInput);
                        }

                        _initialAppInfoSent = true;
                        ((Activity) _context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _sfButtonHelper.showButton();
                            }
                        });

                    }
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            Log.e(TAG, "Unable to process message", e);
        }
    }

    private void updateFootMeasurementCode(String json){
        try {
            // JSON to Object using Gson
            Gson gson = new Gson();
            PostMessageUserFootMeasurementCodeDataDto postMessage = gson.fromJson(json, PostMessageUserFootMeasurementCodeDataDto.class);
            String currentFootMeasurementCode = StrutFitCommonHelper.getLocalFootMCode(_context);
            if(!Objects.equals(currentFootMeasurementCode, postMessage.footMeasurementCode)){
                ProductType productType = _visibilityData != null ? _visibilityData.getProductType() : ProductType.Footwear;
                if(productType == ProductType.Footwear) {
                    String bodyMeasurementCode = StrutFitCommonHelper.getLocalBodyMCode(_context);
                    _sfButtonHelper.getSizeAndVisibility(postMessage.footMeasurementCode, bodyMeasurementCode, false);
                }
                StrutFitCommonHelper.setLocalFootMCode(_context, postMessage.footMeasurementCode);
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable to update measurement code", e);
        }
    }

    private void updateBodyMeasurementCode(String json){
        try {
            // JSON to Object using Gson
            Gson gson = new Gson();
            PostMessageUserBodyMeasurementCodeDataDto postMessage = gson.fromJson(json, PostMessageUserBodyMeasurementCodeDataDto.class);
            String currentBodyMeasurementCode = StrutFitCommonHelper.getLocalFootMCode(_context);
            if(!Objects.equals(currentBodyMeasurementCode, postMessage.bodyMeasurementCode)) {
                ProductType productType = _visibilityData != null ? _visibilityData.getProductType() : ProductType.Footwear;
                if(productType == ProductType.Apparel) {
                    String footMeasurementCode = StrutFitCommonHelper.getLocalFootMCode(_context);
                    _sfButtonHelper.getSizeAndVisibility(footMeasurementCode, postMessage.bodyMeasurementCode, false);
                }
                StrutFitCommonHelper.setLocalBodyMCode(_context, postMessage.bodyMeasurementCode);
            }

        } catch (Exception e) {
            Log.e(TAG, "Unable to update measurement code", e);
        }
    }

    private void updateUserId(String json){
        try {
            // JSON to Object using Gson
            Gson gson = new Gson();
            PostMessageUserIdDataDto postMessage = gson.fromJson(json, PostMessageUserIdDataDto.class);
            StrutFitCommonHelper.setLocalUserId(_context, postMessage.uuid);

        } catch (Exception e) {
            Log.e(TAG, "Unable to update measurement code", e);
        }
    }


    private void closeModal() {
        ((Activity) _context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _sfWebView.closeWebView();
            }
        });
    }
}