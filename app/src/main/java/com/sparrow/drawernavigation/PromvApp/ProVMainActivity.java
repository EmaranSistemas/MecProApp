package com.sparrow.drawernavigation.PromvApp;

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
import com.sparrow.drawernavigation.MainActivity;
import com.sparrow.drawernavigation.PromvApp.Adapters.PfAdapter;
import com.sparrow.drawernavigation.PromvApp.Entities.Frescos;
import com.sparrow.drawernavigation.R;
import com.sparrow.drawernavigation.Ubication.GpsTracker;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProVMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView, recyclerView2;
    private PfAdapter adapter, adapter2;

    String[] distribuidores = {"Adriel","Francisco","Clara","Rodolfo","Juan Carlos","María","Carmen","Mirtha","Gerardo"};
    String[] categorias = {"Bodega","Minimarket","Supermercado","Especiería","Puesto de Mercado",
            "Tienda de Abarrotes","Food Truck","Puesto de Comida","Snack","Carniceria","Pizzeria","Otros"};
    String[] polvosarr = {"------SELECCIONE------",
            "SAZONADOR COMPLETO  GIGANTE X 42 SBS",
            "COMINO MOLIDO GIGANTE X 42 SBS",
            "PIMIENTA BATAN GIGANTE X 42 SBS",
            "PALILLO BATAN  GIGANTE X 42 SBS",
            "TUCO SAZON SALSA BATAN GIGANTE X 42 SBS",
            "AJO BATAN GIGANTE X 42 SBS",
            "CANELA MOLIDA GIGANTE X 42 SBS",
            "EL VERDE BATAN GIGANTE X 42 SBS",
            "KION MOLIDO BATAN GIGANTE X 42 SBS"
            ,"OREGANO SELECTO BATAN X 42 SBS",
            "EL VERDE BATAN GIGANTE x 27 SBS"};

    String[] frescosarr = {"------SELECCIONE------","AJI PANCA FRESCO BATAN x 24 SBS","AJI AMARILLO FRESCO BATAN x24 SBS","AJO FRESCO BATAN x 24 SBS","CULANTRO FRESCO BATAN x 24 SBS"};

    String Distribuidor;
    String categoriasfinal;
    GpsTracker gpsTracker;
    ArrayAdapter<String> adapterdistribuidores;
    ArrayAdapter<String> adaptercategoria;
    ArrayAdapter<String> adapterpolvos;
    ArrayAdapter<String> adapterfrescos;

    Spinner categoria_list;
    Spinner polvos_list;
    Spinner frescos_list;



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

/*
    for(int i=0; i<pro_polvos.size(); i++){
        frecos = new Frescos(Integer.toHexString(contador), pro_polvos.get(i));
        pro_polvos.add(frecos);
        adapter.notifyDataSetChanged();
    }

    */



        try { //Request Permission if not permitted
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        pro_frecos.clear();
        pro_polvos.clear();


        for (int i = 1; i < polvosarr.length; i++) {
            frecos = new Frescos(Integer.toHexString(i), polvosarr[i]);
            pro_polvos.add(frecos);
        }

        for (int i = 1; i < frescosarr.length; i++) {
            frecos = new Frescos(Integer.toHexString(i), frescosarr[i]);
            pro_frecos.add(frecos);
        }




        recyclerView = findViewById(R.id.mypolvos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PfAdapter(this, pro_polvos,pro_polvos);
        recyclerView.setAdapter(adapter);

        recyclerView2 = findViewById(R.id.myfrecos);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        adapter2 = new PfAdapter(this, pro_frecos,pro_frecos);
        recyclerView2.setAdapter(adapter2);

/*
        Spinner distribuidor = findViewById(R.id.distribuidor_txt);
        adapterdistribuidores = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, distribuidores);
        adapterdistribuidores.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distribuidor.setAdapter(adapterdistribuidores);
*/

        Spinner distribuidor = findViewById(R.id.distribuidor_txt);
        adapterdistribuidores = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, distribuidores);
        adapterdistribuidores.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distribuidor.setAdapter(adapterdistribuidores);
        distribuidor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Distribuidor = item;
                //Toast.makeText(getApplicationContext(),"Item: "+item,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });









        txtCliente = findViewById(R.id.cliente_txt);
        txtTelefono = findViewById(R.id.telefono_txt);
        txtDireccion = findViewById(R.id.direccion_txt);
        txtNombreComercial = findViewById(R.id.nombre_co);


        categoria_list = findViewById(R.id.categoria_txt);
        adaptercategoria = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        adaptercategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoria_list.setAdapter(adaptercategoria);
        categoria_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                categoriasfinal = item;
                //Toast.makeText(getApplicationContext(),"Item: "+item,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        polvos_list = findViewById(R.id.polvos_txt);
        adapterpolvos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, polvosarr);
        adapterpolvos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        polvos_list.setAdapter(adapterpolvos);
        polvos_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                // Verificar si se ha seleccionado un elemento
                if (position != 0) { // Cambia 0 por el índice del elemento vacío en el Spinner
                    //Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();
                    //contador++;
                    //frecos = new Frescos(Integer.toHexString(contador), item);
                    //pro_polvos.add(frecos);
                    //adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        frescos_list = findViewById(R.id.frescos_txt);
        adapterfrescos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, frescosarr);
        adapterfrescos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frescos_list.setAdapter(adapterfrescos);
        frescos_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                //polvosfinal = item;
                if (position != 0) {
                    //Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();

                    //contador2++;
                    //final String frescos = txtFrescos.getText().toString().trim();
                    //frecos = new Frescos(Integer.toString(contador2), item);
                    //pro_frecos.add(frecos);
                    //adapter2.notifyDataSetChanged();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



/*
       polvos_list = findViewById(R.id.polvos_txt);
       adapterpolvos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, polvosarr);
        adapterpolvos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        polvos_list.setAdapter(adapterpolvos);


       frescos_list = findViewById(R.id.polvos_txt);
       adapterfrescos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, frescosarr);
        adapterfrescos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frescos_list.setAdapter(adapterfrescos);

*/


        checkBoxExhibidor = findViewById(R.id.exhibidor);
        checkBoxPop = findViewById(R.id.pop);




        txtVentas = findViewById(R.id.ventas_txt);
        txtObservaciones = findViewById(R.id.observaciones_txt);

        btn_insert = findViewById(R.id.btn_guardar);
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });

    }


    private void insertData() {
        try { //Request Permission if not permitted
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String distribuidor = Distribuidor;
        final String cliente = txtCliente.getText().toString().trim();//obligatorio
        final String telefono = txtTelefono.getText().toString().trim();//opcional
        final String direccion = txtDireccion.getText().toString().trim();//obligatorio
        final String nombrecomercial = txtNombreComercial.getText().toString().trim();//opcional
        final String categoria = categoriasfinal;


        //final String polvos = polvos_list.getText().toString().trim();
        //final String frescos = frescos_list.getText().toString().trim();

        boolean isCheckedEx = checkBoxExhibidor.isChecked();
        boolean isCheckedPop = checkBoxPop.isChecked();


        final String ventas = txtVentas.getText().toString().trim();
        final String observaciones = txtObservaciones.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("cargando...");

        if(cliente.isEmpty()){
            Toast.makeText(this, "Ingrese Cliente", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(direccion.isEmpty()){
            Toast.makeText(this, "Ingrese Teléfono", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(categoria.isEmpty()){
            Toast.makeText(this, "Ingrese Categoria", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(ventas.isEmpty()){
            Toast.makeText(this, "Ingrese Ventas", Toast.LENGTH_SHORT).show();
            return;
        }
        else{

            //List<Frescos> lista = adapter.getPfList_r();
            List<Frescos> lista = adapter.getCheckedItems();
            String[] arraypolvos = new String[lista.size()];
            int i = 0;
            for (Frescos f : lista) {
                arraypolvos[i] = f.getName();
                i++;
            }


            List<Frescos> lista2 = adapter2.getCheckedItems();
            String[] arraypolvos2 = new String[lista2.size()];
            int i2 = 0;
            for (Frescos f2 : lista2) {
                arraypolvos2[i2] = f2.getName();
                i2++;
            }

            JSONArray jsonArray = new JSONArray(Arrays.asList(arraypolvos));
            JSONArray jsonArray2 = new JSONArray(Arrays.asList(arraypolvos2));


            progressDialog.show();




            StringRequest request = new StringRequest(Request.Method.POST, "https://emaransac.com/android/insertar_reporte_promotor.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("Se guardo correctamente.")){
                                Toast.makeText(ProVMainActivity.this, "Se guardo correctamente.", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                            else{
                                Toast.makeText(ProVMainActivity.this, response, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ProVMainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();

                    String valorCadena1 = String.valueOf(isCheckedEx);
                    String valorCadena2 = String.valueOf(isCheckedPop);

                    Log.d("------------------TAG------1--------------", jsonArray.toString());
                    Log.d("------------------TAG-------2-------------", jsonArray2.toString());


                    String a_lat = "0";
                    String a_lon = "0";
                    a_lat = getLocs(1);
                    a_lon = getLocs(2);


                    params.put("distribuidor",distribuidor);
                    params.put("cliente",cliente);
                    params.put("telefono",telefono);
                    params.put("direccion",direccion);
                    params.put("nombrecomercial",nombrecomercial);
                    params.put("categoriasfinal",categoriasfinal);
                    params.put("polvosarr", jsonArray.toString());
                    params.put("frescosarr", jsonArray2.toString());
                    params.put("isCheckedEx",valorCadena1);
                    params.put("isCheckedPop",valorCadena2);
                    params.put("ventas",ventas);
                    params.put("observaciones",observaciones);
                    params.put("longitud",a_lon);
                    params.put("latitud",a_lat);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(ProVMainActivity.this);
            requestQueue.add(request);

            requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                @Override
                public void onRequestFinished(Request<Object> request) {
                    // Redirigir a PromotorActivity después de la inserción exitosa
                    Intent intent = new Intent(ProVMainActivity.this, PromotorActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        Log.d("TAG--------------------", "distrinuidor: " + distribuidor +
                "  cliente: " + cliente +
                "  Telefono: " + telefono +
                "  Direccion: " + direccion +
                "  NombreCom: " + nombrecomercial +
                "  Categoria: " + categoriasfinal +
                //"  Polvos: " + polvos +
                //"  Frescos: " + frescos +
                "  Exhibidor: " + isCheckedEx +
                "  Pop: " + isCheckedPop +
                "  Ventas" + ventas +
                "  Observaciones: " + observaciones+
                "  Long: " + ventas +
                "  Latitud: " + observaciones
        );
    }

    public String getLocs(int ID) { //Get Current Lat and Lon 1=lat, 2=lon
        String asd_lat = "";
        String asd_lon = "";
        gpsTracker = new GpsTracker(ProVMainActivity.this);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, PromotorActivity.class);
        startActivity(intent);
        // Finalizar la actividad actual
        finish();
    }


}