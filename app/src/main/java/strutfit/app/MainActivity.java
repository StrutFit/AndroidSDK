package strutfit.app;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import strutfit.button.StrutFitBridge;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create your button and hide it
        Button button = (Button) findViewById(R.id.StruftFitButton);
        button.setVisibility(View.GONE);

        // Create your web view
        WebView webView = (WebView) findViewById(R.id.StruftFitWebview);
        webView.setVisibility(View.GONE);

        // Pass the the two components into the StrutFit bridge
        StrutFitBridge bridge = new StrutFitBridge(button, webView, this, 800, 800, 100, 100, "#f2f2f2", 1, "Test Product 1");
        bridge.execute();
    }
}