package com.sparrow.drawernavigation.Mercapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.sparrow.drawernavigation.Mercapp.Entity.Stores;
import com.sparrow.drawernavigation.PreciosApp.Adapterp.PriceAdapter;
import com.sparrow.drawernavigation.PreciosApp.Entity.prices;
import com.sparrow.drawernavigation.PreciosApp.ReportPriceActivity;
import com.sparrow.drawernavigation.R;
import com.sparrow.drawernavigation.Ubication.GpsTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Register_Activity extends AppCompatActivity implements DetailAdapter.ButtonClickListener {

    String[] locales = {"------SELECCIONE------","Franco Supermercados","Supermercados Peruanos","Hipermercados Tottus","Cencosud Retail","R-Internacinales El-super","Entidad Bancaria"};
    String[] motivos = {"------SELECCIONE------","Cobranza","Inventario && Pedido","Entrega de Pedido","Depósito"};

    ArrayAdapter<String> adapterlocales;
    ArrayAdapter<String> adaptermotivo;
    private RecyclerView recyclerView;
    public static ArrayList<Register> RegisterList = new ArrayList<>();
    String local, motiv;
    DetailAdapter adapter_;
    Register reg;
    int counter = 0;
    GpsTracker gpsTracker;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        try { //Request Permission if not permitted
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


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
                //Toast.makeText(Register_Activity.this, "Creando registro.....!", Toast.LENGTH_LONG).show();

                // Mostrar el ProgressDialog
                ProgressDialog progressDialog = ProgressDialog.show(Register_Activity.this, "", "Espere por favor...", true);

                // Simular una tarea que toma tiempo utilizando un Handler
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();

                        retrieveData();

                        // Agregar los elementos al adaptador nuevamente
                        //adapter_.notifyDataSetChanged();
                    }
                }, 2500); // Retraso de 2.5 segundos (2500 milisegundos)
            }
        });

        recyclerView = findViewById(R.id.myregister);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter_ = new DetailAdapter(this, RegisterList);
        recyclerView.setAdapter(adapter_);
        adapter_.setButtonClickListener(this);

        retrieveData();
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

            String a_lat = "0";
            String a_lon = "0";
            a_lat = getLocs(1);
            a_lon = getLocs(2);


            String finalA_lon = a_lon;
            String finalA_lat = a_lat;
            StringRequest request = new StringRequest(Request.Method.POST, "https://emaransac.com/android/insertar_reporte_visita.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("Se guardo correctamente.")){
                                //Toast.makeText(Register_Activity.this, "Se guardo correctamente.", Toast.LENGTH_SHORT).show();
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





                    //Log.d("---REPORTE--DE--PRECIOS---", observacines_+">>>>>>>>>>>> ");
                    params.put("local",mylocal);
                    params.put("motivo",mymotivo);
                    params.put("ilog", finalA_lon);
                    params.put("ilat", finalA_lat);
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
                    gpsTracker.stopUsingGPS();
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

    public void retrieveData(){

        StringRequest request = new StringRequest(Request.Method.POST, "https://emaransac.com/android/reporte_visita.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        RegisterList.clear();
                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            String exito = jsonObject.getString("exito");
                            JSONArray jsonArray = jsonObject.getJSONArray("datos");
                            if(exito.equals("1")){
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id");
                                    String inicio = object.getString("inicio");
                                    String local = object.getString("local");
                                    String motivo = object.getString("motivo");
                                    String fin = object.getString("fin");
                                    String tiempo = object.getString("tiempo");
                                    int horas = Integer.parseInt(tiempo) / 60; // Obtener la cantidad de horas
                                    int minutos = Integer.parseInt(tiempo) % 60; // Obtener la cantidad de minutos restantes
                                    reg = new Register(id,inicio,local,motivo,fin,horas + " hrs " + minutos + " min");
                                    RegisterList.add(reg);
                                    adapter_.notifyDataSetChanged();
                                }
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void updateData(String id,String local,String motivo) {


        //Toast.makeText(Register_Activity.this,mylocal+" "+mymotivo,Toast.LENGTH_LONG).show();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("cargando...");


            progressDialog.show();

        String a_lat = "0";
        String a_lon = "0";
        a_lat = getLocs(1);
        a_lon = getLocs(2);


        String finalA_lon = a_lon;
        String finalA_lat = a_lat;
        StringRequest request = new StringRequest(Request.Method.POST, "https://emaransac.com/android/actualizar_reporte_visita.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("Se guardo correctamente.")){
                                //Toast.makeText(Register_Activity.this, "Se guardo correctamente.", Toast.LENGTH_SHORT).show();
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






                    //Log.d("---REPORTE--DE--PRECIOS---", observacines_+">>>>>>>>>>>> ");
                    params.put("id",id);
                    params.put("local",local);
                    params.put("motivo",motivo);
                    params.put("flog", finalA_lon);
                    params.put("flat", finalA_lat);
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
                    retrieveData();
                }
            });
        }



    @Override
    public void onButtonClick(String tvID,String local,String motivo) {
        // Aquí puedes manejar la acción del botón
        //Toast.makeText(this, "tvID: -> " + tvID, Toast.LENGTH_SHORT).show();
        updateData(tvID,local,motivo);
    }
}
