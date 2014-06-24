package com.jd.living.activity.details;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.jd.living.R;

@EActivity(R.layout.web)
public class DetailsWebView extends DetailsActivity {

    @ViewById
    WebView webView;

    @Override
    public void onInit() {
        listingsDatabase.addDetailsListener(this);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onUpdate() {
        String url = listing.getUrl() + "/bilder/";
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //getActivity().setProgress(progress * 1000);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(DetailsWebView.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });
        webView.loadUrl(url);
    }
}
