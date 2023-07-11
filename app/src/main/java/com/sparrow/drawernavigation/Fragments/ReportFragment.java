package com.sparrow.drawernavigation.Fragments;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sparrow.drawernavigation.R;

import java.util.Calendar;

public class ReportFragment extends Fragment {

    private ProgressBar mprogresbar;
    private WebView mwebview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        mprogresbar = view.findViewById(R.id.loading_progresbar);
        mwebview = view.findViewById(R.id.webView);

        mwebview.getSettings().setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mwebview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mwebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mprogresbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mprogresbar.setVisibility(View.GONE);
            }
        });

        mwebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mprogresbar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //getActivity().getActionBar().setTitle(title);
            }
        });

        String METABASE_SITE_URL = "https://emadata.emaransac.com";
        int dashboardId = 258;
        String token = JwtUtils.generateMetabaseToken(dashboardId);

        String iframeUrl = METABASE_SITE_URL + "/embed/dashboard/" + token + "#bordered=true&titled=true";

        mwebview.loadUrl(iframeUrl);

        return view;
    }
}
