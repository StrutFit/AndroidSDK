package strutfit.button;
import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class StrutFitButtonWebview {

    private String _url;
    private WebView _webView;

    public StrutFitButtonWebview(WebView webview, String url) {
        _url = url;
        _webView = webview;
    }

    public void SetJavaScriptInterface (StrutFitJavaScriptInterface javascriptInterFace) {
        // Set settings
        WebSettings webSettings = _webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Combine javascript interface
        _webView.addJavascriptInterface(javascriptInterFace, "Android");
    }

    public void OpenAndInitializeWebView() {
        _webView.loadUrl(_url);
        _webView.setVisibility(View.VISIBLE);
    }

    public void OpenWebView() {
        _webView.setVisibility(View.GONE);
        _webView.bringToFront();
    }

    public void CloseWebView() {
        _webView.setVisibility(View.GONE);
    }
}
