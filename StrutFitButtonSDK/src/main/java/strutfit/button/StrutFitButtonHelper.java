package strutfit.button;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class StrutFitButtonHelper {

    public boolean buttonIsVisible = false;
    public String buttonText;
    public String webViewURL;

    private final OkHttpClient client = new OkHttpClient();
    private int  _organizationID;
    private String  _shoeID;
    private String _baseAPIUrl;
    private String _baseWebViewUrl;
    private String _measurementCode;

    private Context _context;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String Measurement_Code = "measurementCode";
    private static final String StrutFit_In_Use = "isStrutFitInUse";

    public StrutFitButtonHelper (Context context, int organizationID, String shoeID, boolean isDev) throws Exception {
        _organizationID = organizationID;
        _shoeID = shoeID;
        _baseAPIUrl = isDev ? "https://api-dev.strut.fit/api/" : "https://api-prod.strut.fit/api/";
        _baseWebViewUrl = isDev ? "https://consumer-portal-dev.strut.fit/" : "https://scan.strut.fit/";
        _context = context;
        String measurementCode = getMeasurementCodeLocally();
        GetSizeAndVisibility(measurementCode, true);
    }

    public void GetSizeAndVisibility(String measurementCode, Boolean isInitializing) throws Exception  {

        if(measurementCode.isEmpty())
        {
            try {
                URL url = new URL(String.format(_baseAPIUrl + "MobileApp/DetermineButtonVisibility?OrganizationUnitId=%s&Code=%s", _organizationID, _shoeID));
                Request request = new Request.Builder().url(url).build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    String responseAsText = response.body().string();
                    JSONObject json = new JSONObject(responseAsText);
                    JSONObject result = json.getJSONObject("result");
                    Boolean _isKids = result.getBoolean("isKids");

                    // Set initial rendering parameters
                    buttonIsVisible = result.getBoolean("show");
                    buttonText = _isKids ? "What is my child's size?" : "What is my size?";

                    Random rand = new Random();
                    int int_random = rand.nextInt(99999);
                    webViewURL = String.format(_baseWebViewUrl + "%s?random=%s&organisationId=%s&shoeId=%s&inApp=true", _isKids ? "nkids" : "nadults", int_random, _organizationID, _shoeID);
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                URL url = new URL(String.format(_baseAPIUrl + "MobileApp/GetSizeandVisibility?OrganizationUnitId=%s&Code=%s&MCode=%s", _organizationID, _shoeID, measurementCode));
                Request request = new Request.Builder().url(url).build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    String responseAsText = response.body().string();
                    JSONObject json = new JSONObject(responseAsText);
                    JSONObject result = json.getJSONObject("result");
                    JSONObject _sizeData = result.getJSONObject("sizeData");
                    JSONObject _visibilityData= result.getJSONObject("visibilityData");


                    Boolean _isKids = _visibilityData.getBoolean("isKids");
                    String _size = _sizeData.getString("size");
                    int _sizeUnit = _sizeData.getInt("unit");
                    Boolean _showWidthCategory = _sizeData.getBoolean("showWidthCategory");
                    String _widthAbbreviation = _sizeData.getString("widthAbbreviation");
                    String _width = (!_showWidthCategory || _widthAbbreviation.isEmpty() || _widthAbbreviation == "null")? "" : _widthAbbreviation;


                    // Set initial rendering parameters
                    buttonIsVisible = _visibilityData.getBoolean("show");

                    String _buttonText = "Unavaliable in your roccomended size";
                    if(!_size.isEmpty() && _size != "null") {
                        _buttonText = _isKids ? String.format("Your child's size in this style is %s %s %s", _size, SizeUnit.getSizeUnitString(SizeUnit.valueOf(_sizeUnit)), _width) : String.format("Your size in this style is %s %s %s", _size, SizeUnit.getSizeUnitString(SizeUnit.valueOf(_sizeUnit)), _width);
                    }
                    buttonText = _buttonText;

                    // When initializing we need to set the web view URL
                    if (isInitializing) {
                        Random rand = new Random();
                        int int_random = rand.nextInt(99999);
                        webViewURL = String.format(_baseWebViewUrl + "%s?random=%s&organisationId=%s&shoeId=%s&inApp=true", _isKids ? "nkids" : "nadults", int_random, _organizationID, _shoeID);
                    }

                    // When a post message comes back from the modal with a new measurement code
                    if (!isInitializing) {
                        this.SetMeasurementCodeLocally(measurementCode);
                        this.SetStrutFitInUseLocally(true);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private String getMeasurementCodeLocally() {
        try
        {
            SharedPreferences sharedPreferences = _context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            return sharedPreferences.getString(Measurement_Code, "");
        }
        catch (Exception e)
        {
            return ""; //zrgx0nf
        }
    }

    private void SetMeasurementCodeLocally(String measurementCode) {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Measurement_Code, measurementCode);

        editor.apply();
    }

    private void SetStrutFitInUseLocally(Boolean inUse) {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(StrutFit_In_Use, inUse);

        editor.apply();
    }
}
