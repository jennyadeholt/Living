package com.jd.living.activity.detail;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.app.Fragment;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.jd.living.R;
import com.jd.living.model.Listing;
import com.jd.living.server.ListingsDatabase;

@EFragment(R.layout.web)
public class DetailsWebView extends DetailsFragment {

    @ViewById
    WebView webView;

    @Override
    protected void onInit() {

    }

    protected void onUpdate() {
        Log.d("Living", "DetailsWebView");
        if (listing != null) {
            Log.d("Living", "DetailsWebView 2");

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
