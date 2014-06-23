package com.jd.living.activity.detail;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.jd.living.R;

@EFragment(R.layout.web)
public class DetailsWebView extends DetailsInfo {

    @ViewById
    WebView webView;

    @Override
    protected void onInit() {
        // Do nothing
    }

    protected void onUpdate() {
        if (listing != null) {

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
                    Toast.makeText(getActivity(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                }
            });
            webView.loadUrl(url);
        }
    }
}
