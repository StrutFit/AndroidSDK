package strutfit.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import java.util.ArrayList;

import strutfit.button.StrutFitBridge;
import strutfit.button.StrutFitButtonView;
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


        // Pass the the two components into the StrutFit bridge
        StrutFitBridge bridge = new StrutFitBridge(button, webView, this, 1500, 1500, 100, 100, "#f2f2f2",
                this.getResources().getInteger(R.integer.organizationUnitId),
                this.getResources().getString(R.string.productCode),
                null, null, null, null, null);
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