package strutfit.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import java.util.ArrayList;

import strutfit.button.SizeUnit;
import strutfit.button.StrutFitBridge;
import strutfit.button.StrutFitButtonView;
import strutfit.button.StrutFitEventListener;
import strutfit.button.StrutFitTracking;
import strutfit.button.models.ConversionItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create your button and hide it
        StrutFitButtonView button = findViewById(R.id.StruftFitButton);
        button.setVisibility(View.GONE);

        // Create your web view
        WebView webView = findViewById(R.id.StruftFitWebview);
        webView.setVisibility(View.GONE);

        StrutFitEventListener strutFitEventListener = new StrutFitEventListener() {
            @Override
            public void onSizeEvent(String size, SizeUnit unit) {
                if(size != null) {
                    Log.i("OnSizeEvent", "Size: " + size + " " + SizeUnit.getSizeUnitString(unit));
                } else {
                    Log.i("OnSizeEvent", "No size");
                }
            }
        };


        // Pass the the two components into the StrutFit bridge
        StrutFitBridge bridge = new StrutFitBridge(button, webView, this,
                this.getResources().getInteger(R.integer.organizationUnitId),
                this.getResources().getString(R.string.productCode),
                strutFitEventListener);
        bridge.initializeStrutFit();


        // StrutFitTracking sfTracking = new StrutFitTracking(this, this.getResources().getInteger(R.integer.organizationUnitId));
        // ArrayList<ConversionItem> items = new ArrayList<ConversionItem>();
        // ConversionItem item = new ConversionItem();
        // item.size = "5";
        // item.price = 3.00;
        // item.productIdentifier = "LC123";
        // item.sku = "12312313";
        // items.add(item);
        // sfTracking.registerOrder("TestRef", 123, "USD", items);
    }
}