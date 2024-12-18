package strutfit.button.helpers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
    private ProductType _productType;
    private Boolean _isKids;
    private OnlineScanInstructionsType _onlineScanInstructionsType;

    private Boolean _initialAppInfoSent = false;


    public StrutFitJavaScriptInterface(StrutFitButtonHelper sfButtonHelper, StrutFitButtonWebview sfWebView, Context context,
                                       int organizationId, String productCode, SizeUnit sizeUnit,
                                       String apparelSizeUnit, ProductType productType,
                                       Boolean isKids, OnlineScanInstructionsType onlineScanInstructionsType) {
        _sfButtonHelper = sfButtonHelper;
        _sfWebView = sfWebView;
        _context = context;
        _organizationId = organizationId;
        _productCode = productCode;
        _sizeUnit = sizeUnit;
        _apparelSizeUnit = apparelSizeUnit;
        _productType = productType;
        _isKids = isKids;
        _onlineScanInstructionsType = onlineScanInstructionsType;
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
                        PostMessageInitialAppInfoDto input = new PostMessageInitialAppInfoDto();
                        input.strutfitMessageType = PostMessageType.InitialAppInfo.getValue();
                        input.productType = _productType.getValue();
                        input.isKids = _isKids;
                        input.productId = _productCode;
                        input.organizationUnitId = _organizationId;
                        input.defaultSizeUnit = _sizeUnit != null ? _sizeUnit.getValue() : null;
                        input.defaultApparelSizeUnit = _apparelSizeUnit;
                        input.onlineScanInstructionsType = _onlineScanInstructionsType.getValue();
                        input.hideSizeGuide = true;
                        input.hideUsualSize = true;
                        input.inApp = true;

                        _sfWebView.sendInitialAppInfo(input);

                        StrutFitGlobalState globalState = StrutFitGlobalState.getInstance();
                        PostMessageUpdateThemeDto updateThemeInput = new PostMessageUpdateThemeDto();
                        updateThemeInput.strutfitMessageType = PostMessageType.UpdateTheme.getValue();

                        JsonParser jsonParser = new JsonParser();
                        updateThemeInput.theme = jsonParser.parse(globalState.getThemeJson());

                        _sfWebView.sendTheme(updateThemeInput);

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
                if(_productType == ProductType.Footwear) {
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
                if(_productType == ProductType.Apparel) {
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