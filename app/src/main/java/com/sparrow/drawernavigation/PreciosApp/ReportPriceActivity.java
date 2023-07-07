package com.sparrow.drawernavigation.PreciosApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.sparrow.drawernavigation.PreciosApp.Adapterp.PriceAdapter;
import com.sparrow.drawernavigation.PreciosApp.Entity.prices;
import com.sparrow.drawernavigation.PromvApp.Entities.Frescos;
import com.sparrow.drawernavigation.PromvApp.PromotorActivity;
import com.sparrow.drawernavigation.R;
import com.sparrow.drawernavigation.Ubication.GpsTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportPriceActivity extends AppCompatActivity  implements PriceAdapter.TextInputListener {
    private RecyclerView recyclerView;
    private PriceAdapter adapter;
    private prices pri;
    GpsTracker gpsTracker;
    List<String> listaProductos = new ArrayList<>();
    List<String> listaPrecios = new ArrayList<>();
    List<String> listaGramaje = new ArrayList<>();
    public static ArrayList<prices> pricesArrayList = new ArrayList<>();
    String url ="https://emaransac.com/android/productos_retail.php";
    String url2 ="https://emaransac.com/android/productos_horeca.php";
    String url3 ="https://emaransac.com/android/productos_tradicional.php";
    String link;
    String catprod;
    EditText zona,marca,observaciones;

    String mybrand;
    String myobs;

    String[] catproducto = {"➤ Categoría ","Tradicional","Horeca","Retail"};
    ArrayAdapter<String> catproadapter;
    Spinner catlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_price);

        try { //Request Permission if not permitted
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        catlist = findViewById(R.id.catprod_txt);
        catproadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, catproducto);
        catproadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catlist.setAdapter(catproadapter);
        catlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                link = "";
                if (position != 0) { // Cambia 0 por el índice del elemento vacío en el Spinner
                    catprod = item;
                    if(catprod.equals("Horeca")){
                        link = url2;
                    } else if (catprod.equals("Tradicional")) {
                        link = url3;
                    }else{
                        link = url;
                        catprod = "Retail";
                    }
                    //Toast.makeText(getApplicationContext(),"Item: "+item,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ReportPriceActivity.this, "Ingrese una categoría !!!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Toast.makeText(getApplicationContext(),"Ingrese Categoría",Toast.LENGTH_SHORT).show();
            }
        });


        zona = findViewById(R.id.cliente_txt);
        marca = findViewById(R.id.brand_txt_marca);
        observaciones = findViewById(R.id.txt_observaciones_);


        Button btnAgregar = findViewById(R.id.btn_agregar);
        final int[] clickCount = {0};
            btnAgregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickCount[0]++;
                    if (clickCount[0] == 1) {
                        final String zonadistrito = zona.getText().toString().trim();//obligatorio
                        final String marca_txt = marca.getText().toString().trim();//opcional
                        final String categoria_prod = catprod;
                        mybrand = marca_txt;


                    /*
                    pricesArrayList.add(new prices("001", "Ajo", "Marca1", "Frasco1", "100g", "10.99"));
                    pricesArrayList.add(new prices("002", "AJI AMARILLO", "Marca2", "Frasco2", "200g", "5.99"));
                    */
                        if (zonadistrito.isEmpty()) {
                            Toast.makeText(ReportPriceActivity.this, "Ingrese Zona/Distrito", Toast.LENGTH_SHORT).show();
                            clickCount[0] = 0;
                            return;
                        }else if (marca_txt.isEmpty()) {
                            Toast.makeText(ReportPriceActivity.this, "Ingrese Marca", Toast.LENGTH_SHORT).show();
                            clickCount[0] = 0;
                            return;
                        }else if (categoria_prod.isEmpty()) {
                                // El valor de catprod no es válido
                                Toast.makeText(ReportPriceActivity.this, "Ingrese una categoría de producto válida", Toast.LENGTH_SHORT).show();
                                clickCount[0] = 0;
                                return;

                        } else {
                            // Cambiar el color de fondo del botón y el texto en el primer clic
                            btnAgregar.setBackgroundColor(Color.parseColor("#FF01579B"));
                            btnAgregar.setText("GUARDAR");

                            retrieveData(link);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(ReportPriceActivity.this, "Lista Productos", Toast.LENGTH_SHORT).show();

                            //lista = adapter.getPfList();
                            //Toast.makeText(ReportPriceActivity.this,"SIZE: "+lista.size(),Toast.LENGTH_LONG).show();
                        }
                    } else if (clickCount[0] == 2 && !catprod.equals("Categoría")) {
                        // Restaurar el color de fondo y el texto original en el segundo clic
                        btnAgregar.setBackgroundColor(Color.parseColor("#FF4CAF50"));
                        btnAgregar.setText("AGREGAR");
                        clickCount[0] = 0; // Reiniciar el contador de clics
                        pricesArrayList.clear();
                        adapter.notifyDataSetChanged();

                        insertData();
                        marca.setText("");
                        observaciones.setText("");


                        //Toast.makeText(ReportPriceActivity.this, "Se guardan los datos", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        recyclerView = findViewById(R.id.recycler_price_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PriceAdapter(this,pricesArrayList,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onTextInputClicked(String id, String producto, String frasco, String grameje, String precio) {
        //Log.d("------------------TAG PRICE--------------------",id + " -- " + producto + " -- " + marca + " -- " + frasco + " -- " + grameje + " -- " + precio);
    }

    public void retrieveData(String url){
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pricesArrayList.clear();
                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            String exito = jsonObject.getString("exito");
                            JSONArray jsonArray = jsonObject.getJSONArray("datos");
                            if(exito.equals("1")){
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id");
                                    String nombre = object.getString("nombre");
                                    pri = new prices(id,nombre,"","","","");
                                    pricesArrayList.add(pri);
                                    // add data--> local array
                                    //productos.add(nombre);
                                    listaProductos.add(nombre);
                                    listaGramaje.add("50");
                                    listaPrecios.add("9.5");
                                    //Toast.makeText(ReportPriceActivity.this,"SIZE: "+adapter.getPfList().size(),Toast.LENGTH_LONG).show();
                                    adapter.notifyDataSetChanged();
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
                Toast.makeText(ReportPriceActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }



    private void insertData() {
        final String zonadistrito = zona.getText().toString().trim();//obligatorio
        final String marca_ = mybrand;
        final String categoria_prod = catprod;
        final String observacines_ = observaciones.getText().toString().trim();
        //Toast.makeText(ReportPriceActivity.this,"OBS--->"+observacines_,Toast.LENGTH_SHORT);
        Log.d("REPORTE--DE--PRECIOS ZONA DISTRITO-->", zonadistrito
                + "  CATEGORIA : --> " + categoria_prod
                + " BRABDI:--> "+marca_+" OBSERVACIONES :--> "+observacines_+" ");

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("cargando...");

        if(zonadistrito.isEmpty()){
            Toast.makeText(this, "Ingrese Zona/Distrito", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(marca_.isEmpty()){
            Toast.makeText(this, "Ingrese Marca", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(categoria_prod.isEmpty()){
            Toast.makeText(this, "Ingrese Categoria producto", Toast.LENGTH_SHORT).show();
            return;
        }
        else{

            /*
            String[] arraypolvos = new String[lista.size()];
            int i = 0;
            for (prices f : lista) {
                Toast.makeText(ReportPriceActivity.this,"PRODUCTO: "+f.getProducto(),Toast.LENGTH_SHORT);
                arraypolvos[i] = f.getProducto();
                i++;
            }*/


/*
            listaProductos.add("comino");
            listaProductos.add("comino1");
            listaProductos.add("comino2");
            listaProductos.add("comino3");
*/
            progressDialog.show();
            String a_lat = "0";
            String a_lon = "0";
            a_lat = getLocs(1);
            a_lon = getLocs(2);


            String finalA_lon = a_lon;
            String finalA_lat = a_lat;
            StringRequest request = new StringRequest(Request.Method.POST, "https://emaransac.com/android/insertar_reporte_precios.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("Se guardo correctamente.")){
                                Toast.makeText(ReportPriceActivity.this, "Se guardo correctamente.", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                //finish();
                            }
                            else{
                                Toast.makeText(ReportPriceActivity.this, response, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ReportPriceActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();






                    //Log.d("---REPORTE--DE--PRECIOS---", observacines_+">>>>>>>>>>>> ");
                    params.put("zona",zonadistrito);
                    params.put("catprod",categoria_prod);
                    params.put("marca",marca_);
                    params.put("list_detalle", new JSONArray(listaProductos).toString());
                    params.put("list_gramaje", new JSONArray(listaGramaje).toString());
                    params.put("list_precio", new JSONArray(listaPrecios).toString());
                    params.put("observaciones",observacines_);
                    params.put("longitud", finalA_lon);
                    params.put("latitud", finalA_lat);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(ReportPriceActivity.this);
            requestQueue.add(request);

            requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

                @Override
                public void onRequestFinished(Request<Object> request) {
                    /*
                     //Redirigir a PromotorActivity después de la inserción exitosa
                    Intent intent = new Intent(ReportPriceActivity.this, ReportPriceActivity.class);
                    startActivity(intent);
                    finish();*/
                    listaProductos.clear();
                }
            });
        }

        //Log.d("---REPORTE--DE--PRECIOS---", zonadistrito + "  " + categoria_prod + " "+marca+" "+observacines_+" ");
    }

    public String getLocs(int ID) { //Get Current Lat and Lon 1=lat, 2=lon
        String asd_lat = "";
        String asd_lon = "";
        gpsTracker = new GpsTracker(ReportPriceActivity.this);
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