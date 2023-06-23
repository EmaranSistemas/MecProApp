package com.sparrow.drawernavigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sparrow.drawernavigation.PromvApp.Adapters.PfAdapter;
import com.sparrow.drawernavigation.PromvApp.Entities.Frescos;
import com.sparrow.drawernavigation.Ubication.GpsTracker;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProVMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView, recyclerView2;
    private PfAdapter adapter, adapter2,adapter3,adapter4;

    String[] distribuidores = {"Adriel","Francisco","Clara","Rodolfo","Juan Carlos","María","Carmen","Mirtha","Gerardo"};
    String[] categorias = {"Bodega","Minimarket","Supermercado","Especiería","Puesto de Mercado",
            "Tienda de Abarrotes","Food Truck","Puesto de Comida","Snack","Carniceria","Pizzeria","Otros"};
    String[] polvosarr = {"SAZONADOR COMPLETO  GIGANTE X 42 SBS","COMINO MOLIDO GIGANTE X 42 SBS","PIMIENTA BATAN GIGANTE X 42 SBS","PALILLO BATAN  GIGANTE X 42 SBS",
            "TUCO SAZON SALSA BATAN GIGANTE X 42 SBS","AJO BATAN GIGANTE X 42 SBS","CANELA MOLIDA GIGANTE X 42 SBS","EL VERDE BATAN GIGANTE X 42 SBS","KION MOLIDO BATAN GIGANTE X 42 SBS"
            ,"OREGANO SELECTO BATAN X 42 SBS","EL VERDE BATAN GIGANTE x 27 SBS"};

    String[] frescosarr = {"AJI PANCA FRESCO BATAN x 24 SBS","AJI AMARILLO FRESCO BATAN x24 SBS","AJO FRESCO BATAN x 24 SBS","CULANTRO FRESCO BATAN x 24 SBS"};



    String Distribuidor;
    String categoriasfinal;

    GpsTracker gpsTracker;
    AutoCompleteTextView distribuidor;
    AutoCompleteTextView categoria;
    AutoCompleteTextView polvos_list;
    AutoCompleteTextView frescos_list;
    ArrayAdapter<String> adapterdistribuidores;
    ArrayAdapter<String> adaptercategoria;
    ArrayAdapter<String> adapterpolvos;
    ArrayAdapter<String> adapterfrescos;



    EditText txtCliente, txtTelefono, txtDireccion,txtNombreComercial,txtVentas,txtObservaciones;
    private CheckBox checkBoxExhibidor;
    private CheckBox checkBoxPop;

    Button btn_insert;
    Frescos frecos;

    int contador = 0;
    int contador2 = 0;
    public static ArrayList<Frescos> pro_frecos = new ArrayList<>();
    public static ArrayList<Frescos> pro_polvos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_vmain);


        Spinner spinner = findViewById(R.id.distribuidor_txt);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, distribuidores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);




    }
}