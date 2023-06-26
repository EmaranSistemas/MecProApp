package com.sparrow.drawernavigation.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.sparrow.drawernavigation.Mercapp.MercAppActivity;
import com.sparrow.drawernavigation.PreciosApp.PreciosActivity;
import com.sparrow.drawernavigation.PromvApp.PromotorActivity;
import com.sparrow.drawernavigation.R;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        /*
        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción a realizar cuando se hace clic en el botón
                Intent intent = new Intent(getActivity(), OtraActividad.class);
                startActivity(intent);
            }
        });


        */


        Button button = view.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Promotor ventas", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), PromotorActivity.class);
                startActivity(intent);
            }
        });


        Button button2 = view.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Mercap", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MercAppActivity.class);
                startActivity(intent);
            }
        });


        Button button3 = view.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Prices", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), PreciosActivity.class);
                startActivity(intent);
            }
        });


        Button button4 = view.findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "No implementado!!", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getActivity(), MercAppActivity.class);
                //startActivity(intent);
            }
        });


        return view;
    }
}