package strutfit.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Random;

import strutfit.button.StrutFitTracking;
import strutfit.button.models.ConversionItem;
import strutfit.button.ui.StrutFitButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Activity activity = this;
        int buttonId = R.id.StrutFitButton;
        int organizationId = this.getResources().getInteger(R.integer.organizationUnitId);
        String productCode = this.getResources().getString(R.string.productCode);
        String sizeUnit = "UK";

        // Initialize the StrutFitButton
        new StrutFitButton(activity, buttonId, organizationId, productCode, sizeUnit);

//        StrutFitTracking sfTracking = new StrutFitTracking(this, this.getResources().getInteger(R.integer.organizationUnitId));
//        ArrayList<ConversionItem> items = new ArrayList<ConversionItem>();
//
//        ConversionItem item = new ConversionItem(this.getResources().getString(R.string.productCode), 50.00, 1, "5 US");
//        items.add(item);
//
//        ConversionItem item2 = new ConversionItem(this.getResources().getString(R.string.productCode), 50.00, 2, "8", "US");
//        items.add(item2);
//
//        sfTracking.registerOrder(generateRandomString(7), 150.00, "USD", items);
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