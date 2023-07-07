package com.sparrow.drawernavigation.PromvApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
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
import com.sparrow.drawernavigation.Mercapp.Register_Activity;
import com.sparrow.drawernavigation.PromvApp.Adapters.DetailPAdapter;
import com.sparrow.drawernavigation.PromvApp.Entities.RegisterP;
import com.sparrow.drawernavigation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterP_Activity extends AppCompatActivity implements DetailPAdapter.ButtonClickListener{

    String[] motivos = {"------SELECCIONE------","Inicio ","Durante el día","Fin "};

    ArrayAdapter<String> adaptermotivo;
    private RecyclerView recyclerView;
    public static ArrayList<RegisterP> RegisterList = new ArrayList<>();
    String motiv;
    DetailPAdapter adapter_;
    RegisterP reg;
    int counter = 0;
    location gpsTracker;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pactivity);

        try { //Request Permission if not permitted
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                if (motiv.isEmpty() || motiv.equals("------SELECCIONE------")) {
                    Toast.makeText(RegisterP_Activity.this, "Ingrese Motivo de visita", Toast.LENGTH_SHORT).show();
                    return;
                }

                insertData();
                //Toast.makeText(Register_Activity.this, "Creando registro.....!", Toast.LENGTH_LONG).show();

                // Mostrar el ProgressDialog
                ProgressDialog progressDialog = ProgressDialog.show(RegisterP_Activity.this, "", "Espere por favor...", true);

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

        recyclerView = findViewById(R.id.myregister_promotor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter_ = new DetailPAdapter(this, RegisterList);
        recyclerView.setAdapter(adapter_);
        adapter_.setButtonClickListener(this);

        retrieveData();
    }


    private void insertData() {
        final String mymotivo = motiv;

        //Toast.makeText(Register_Activity.this,mylocal+" "+mymotivo,Toast.LENGTH_LONG).show();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("cargando...");

        if(mymotivo.isEmpty()){
            Toast.makeText(this, "Ingrese Motivo", Toast.LENGTH_SHORT).show();
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
            StringRequest request = new StringRequest(Request.Method.POST, "https://emaransac.com/android/insertar_reportevisita_promotor.php",
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
                                Toast.makeText(RegisterP_Activity.this, response, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegisterP_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();






                    //Log.d("---REPORTE--DE--PRECIOS---", observacines_+">>>>>>>>>>>> ");
                    params.put("motivo",mymotivo);
                    params.put("ilog", finalA_lon);
                    params.put("ilat", finalA_lat);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(RegisterP_Activity.this);
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
        gpsTracker = new location(RegisterP_Activity.this);
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

        StringRequest request = new StringRequest(Request.Method.POST, "https://emaransac.com/android/reporte_promotor.php",
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
                                    String motivo = object.getString("motivo");
                                    String fin = object.getString("fin");
                                    String tiempo = object.getString("tiempo");
                                    int horas = Integer.parseInt(tiempo) / 60; // Obtener la cantidad de horas
                                    int minutos = Integer.parseInt(tiempo) % 60; // Obtener la cantidad de minutos restantes
                                    //reg = new RegisterP(id,inicio,motivo,fin,tiempo);
                                    reg = new RegisterP(id,inicio,motivo,fin,horas + " horas " + minutos + " minutos");
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
                Toast.makeText(RegisterP_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void updateData(String id,String motivo) {


        //Toast.makeText(Register_Activity.this,mylocal+" "+mymotivo,Toast.LENGTH_LONG).show();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("cargando...");


        progressDialog.show();

        String a_lat = "0";
        String a_lon = "0";
        a_lat = getLocs(1);
        a_lon = getLocs(2);
        //Toast.makeText(RegisterP_Activity.this,"Ubicacion: "+a_lat + a_lon,Toast.LENGTH_SHORT);


        String finalA_lon = a_lon;
        String finalA_lat = a_lat;

        StringRequest request = new StringRequest(Request.Method.POST, "https://emaransac.com/android/actualizar_reporte_promotor.php",
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
                            Toast.makeText(RegisterP_Activity.this, response, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterP_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();






                //Log.d("---REPORTE--DE--PRECIOS---", observacines_+">>>>>>>>>>>> ");
                params.put("id",id);
                params.put("motivo",motivo);
                params.put("flog", finalA_lon);
                params.put("flat", finalA_lat);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterP_Activity.this);
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
    public void onButtonClick(String tvID,String motivo) {
        // Aquí puedes manejar la acción del botón
        //Toast.makeText(this, "tvID: -> " + tvID, Toast.LENGTH_SHORT).show();
        updateData(tvID,motivo);
    }
}