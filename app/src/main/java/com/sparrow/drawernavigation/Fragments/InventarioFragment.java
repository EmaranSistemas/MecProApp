package com.sparrow.drawernavigation.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sparrow.drawernavigation.R;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class InventarioFragment extends Fragment {

    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*
        View rootView = inflater.inflate(R.layout.fragment_inventario, container, false);

        webView = rootView.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://emadata.emaransac.com:4000/public/dashboard/cc3c8161-8f8f-4beb-ab14-47db049ddc10?escoge_la_fecha=past12months~");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        */

        return  inflater.inflate(R.layout.fragment_inventario, container, false);
    }
}