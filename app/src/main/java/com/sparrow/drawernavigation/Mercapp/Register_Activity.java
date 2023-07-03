package com.sparrow.drawernavigation.Mercapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.sparrow.drawernavigation.Mercapp.Adapters.DetailAdapter;
import com.sparrow.drawernavigation.Mercapp.Entity.Register;
import com.sparrow.drawernavigation.PreciosApp.Adapterp.PriceAdapter;
import com.sparrow.drawernavigation.PreciosApp.Entity.prices;
import com.sparrow.drawernavigation.PreciosApp.ReportPriceActivity;
import com.sparrow.drawernavigation.R;

import java.util.ArrayList;

public class Register_Activity extends AppCompatActivity {

    String[] locales = {"------SELECCIONE------","Franco Supermercados","Supermercados Peruanos","Hipermercados Tottus","Cencosud Retail","R-Internacinales El-super","Entidad Bancaria"};
    String[] motivos = {"------SELECCIONE------","Cobranza","Inventario","Cobranza && Inventario"};

    ArrayAdapter<String> adapterlocales;
    ArrayAdapter<String> adaptermotivo;
    private RecyclerView recyclerView;
    public static ArrayList<Register> RegisterList = new ArrayList<>();
    String local, motiv;
    DetailAdapter adapter_;
    Register reg;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Spinner localesSpinner = findViewById(R.id.local_txt);
        adapterlocales = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locales);
        adapterlocales.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        localesSpinner.setAdapter(adapterlocales);
        localesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                local = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner motivoSpinner = findViewById(R.id.motivo_txt);
        adaptermotivo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, motivos);
        adaptermotivo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        motivoSpinner.setAdapter(adaptermotivo);
        motivoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                motiv = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button btnStart_End = findViewById(R.id.btn_start_end);
        btnStart_End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (local.isEmpty() || local.equals("------SELECCIONE------")) {
                    Toast.makeText(Register_Activity.this, "Ingrese Local", Toast.LENGTH_SHORT).show();
                    return;
                } else if (motiv.isEmpty() || motiv.equals("------SELECCIONE------")) {
                    Toast.makeText(Register_Activity.this, "Ingrese Motivo de visita", Toast.LENGTH_SHORT).show();
                    return;
                }

                //RegisterList.add(new Register("001", "02/12/2023", " Hipermercados Tottus ", "Inventario"));
                //RegisterList.add(new Register("002", "02/12/2023", "Supermercados Peruanos", "Inventario && Cobranza"));
                String it =  String.valueOf(++counter);
                reg = new Register(it, "02/12/2023", "Supermercados Peruanos", "Inventario && Cobranza");
                RegisterList.add(reg);
                // Agregar los elementos al adaptador nuevamente
                adapter_.notifyDataSetChanged();
            }
        });

        recyclerView = findViewById(R.id.myregister);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter_ = new DetailAdapter(this, RegisterList);
        recyclerView.setAdapter(adapter_);
    }
}
