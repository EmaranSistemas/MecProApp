package com.sparrow.drawernavigation.Fragments;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
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
    private String url = "https://github.com/"; // Reemplaza con tu URL

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        mprogresbar = view.findViewById(R.id.loading_progresbar);
        mwebview = view.findViewById(R.id.webView);

        mwebview.getSettings().setJavaScriptEnabled(true);
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

        mwebview.loadUrl(url);

        return view;
    }
}


        /*
        // Inicializa las variables
        pickDateBtn = view.findViewById(R.id.idBtnPickDate);
        selectedDateTV = view.findViewById(R.id.idTVSelectedDate);

        // Agrega el listener al botón
        pickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene la instancia del calendario
                final Calendar c = Calendar.getInstance();

                // Obtiene el día, mes y año actual
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // Crea el cuadro de diálogo de selección de fecha
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        requireContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Establece la fecha seleccionada en el TextView
                                selectedDateTV.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        },
                        year, month, day);
                // Muestra el cuadro de diálogo de selección de fecha
                datePickerDialog.show();
            }
        });


         */