package com.tamilbible.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public class AndroidShareBridge {
        @JavascriptInterface
        public void shareImage(final String dataUrl, final String title) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        String base64 = dataUrl.contains(",")
                                ? dataUrl.substring(dataUrl.indexOf(',') + 1)
                                : dataUrl;
                        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        if (bmp == null) { shareTextFallback(title); return; }

                        File cacheDir = new File(getCacheDir(), "shared_images");
                        cacheDir.mkdirs();
                        File imgFile = new File(cacheDir, "tamil-bible-verse.png");
                        FileOutputStream fos = new FileOutputStream(imgFile);
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush(); fos.close();

                        Uri contentUri = FileProvider.getUriForFile(
                                MainActivity.this,
                                getPackageName() + ".fileprovider",
                                imgFile);

                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("image/png");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(shareIntent, "வசனம் பகிர் / Share Verse"));
                    } catch (IOException e) {
                        shareTextFallback(title);
                    }
                }
            });
        }

        private void shareTextFallback(String title) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, title);
            startActivity(Intent.createChooser(intent, "Share"));
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xFF1a237e);
        }

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDefaultFontSize(16);
        settings.setMinimumFontSize(10);

        webView.addJavascriptInterface(new AndroidShareBridge(), "AndroidShare");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.startsWith("file://")) return false;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage msg) { return true; }
        });

        webView.loadUrl("file:///android_asset/www/index.html");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }

    @Override protected void onPause()   { super.onPause();   webView.onPause();   }
    @Override protected void onResume()  { super.onResume();  webView.onResume();  }
    @Override protected void onDestroy() { if (webView != null) webView.destroy(); super.onDestroy(); }
}
