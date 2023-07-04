package com.sparrow.drawernavigation.Mercapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sparrow.drawernavigation.Mercapp.Adapters.DetailAdapter;
import com.sparrow.drawernavigation.Mercapp.Entity.Register;
import com.sparrow.drawernavigation.PreciosApp.Adapterp.PriceAdapter;
import com.sparrow.drawernavigation.PreciosApp.Entity.prices;
import com.sparrow.drawernavigation.PreciosApp.ReportPriceActivity;
import com.sparrow.drawernavigation.R;
import com.sparrow.drawernavigation.Ubication.GpsTracker;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    GpsTracker gpsTracker;

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
                insertData();
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


    private void insertData() {
        final String mylocal = local;//obligatorio
        final String mymotivo = motiv;

        //Toast.makeText(Register_Activity.this,mylocal+" "+mymotivo,Toast.LENGTH_LONG).show();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("cargando...");

        if(mylocal.isEmpty()){
            Toast.makeText(this, "Ingrese Local", Toast.LENGTH_SHORT).show();
            return;
        }
        else{

            progressDialog.show();


            StringRequest request = new StringRequest(Request.Method.POST, "https://emaransac.com/android/insertar_reporte_visita.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("Se guardo correctamente.")){
                                Toast.makeText(Register_Activity.this, "Se guardo correctamente.", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                //finish();
                            }
                            else{
                                Toast.makeText(Register_Activity.this, response, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Register_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();


                    String a_lat = "0";
                    String a_lon = "0";
                    a_lat = getLocs(1);
                    a_lon = getLocs(2);



                    //Log.d("---REPORTE--DE--PRECIOS---", observacines_+">>>>>>>>>>>> ");
                    params.put("local",mylocal);
                    params.put("motivo",mymotivo);
                    params.put("ilog",a_lon);
                    params.put("ilat",a_lat);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(Register_Activity.this);
            requestQueue.add(request);

            requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

                @Override
                public void onRequestFinished(Request<Object> request) {
                    //por ahora
                    //RegisterList.clear();
                }
            });
        }
    }

    public String getLocs(int ID) { //Get Current Lat and Lon 1=lat, 2=lon
        String asd_lat = "";
        String asd_lon = "";
        gpsTracker = new GpsTracker(Register_Activity.this);
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            asd_lat = String.valueOf(latitude);
            asd_lon = String.valueOf(longitude);
        } else {
            gpsTracker.showSettingsAlert();
        }
        if (ID == 1) {
            return asd_lat;
        } else if (ID == 2) {
            return asd_lon;
        } else {
            return "0";
        }
    }
}
