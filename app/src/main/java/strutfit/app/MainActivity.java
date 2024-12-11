package strutfit.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import java.util.Random;

import strutfit.button.StrutFitBridge;
import strutfit.button.StrutFitButtonView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the StrutFitBridge
        new StrutFitBridge(this, R.id.StruftFitButton,
                this.getResources().getInteger(R.integer.organizationUnitId),
                this.getResources().getString(R.string.productCode));


//        StrutFitTracking sfTracking = new StrutFitTracking(this, this.getResources().getInteger(R.integer.organizationUnitId));
//        ArrayList<ConversionItem> items = new ArrayList<ConversionItem>();
//
//        ConversionItem item = new ConversionItem();
//        item.size = "5 US";
//        item.price = 50.00;
//        item.quantity = 1;
//        item.productIdentifier = this.getResources().getString(R.string.productCode);
//        items.add(item);
//
//        ConversionItem item2 = new ConversionItem();
//        item2.size = "8";
//        item2.sizeUnit = "US";
//        item2.price = 60.00;
//        item2.quantity = 1;
//        item2.productIdentifier = this.getResources().getString(R.string.productCode);
//        items.add(item2);
//
//        sfTracking.registerOrder(generateRandomString(7), 110, "USD", items);
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }

        return result.toString();
    }
}