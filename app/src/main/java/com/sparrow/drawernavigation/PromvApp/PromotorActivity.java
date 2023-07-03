package com.sparrow.drawernavigation.PromvApp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sparrow.drawernavigation.MainActivity;
import com.sparrow.drawernavigation.PromvApp.Adapters.DetalleAdapter;
import com.sparrow.drawernavigation.PromvApp.Entities.detalle;
import com.sparrow.drawernavigation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PromotorActivity extends AppCompatActivity {

    ListView listView;

    DetalleAdapter adapter;
    public static ArrayList<detalle> employeeArrayList = new ArrayList<>();

    detalle employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotor);

        listView = findViewById(R.id.myListView_promotor);
        adapter = new DetalleAdapter(this,employeeArrayList);
        listView.setAdapter(adapter);

        retrieveData();
    }

    public void retrieveData(){

        StringRequest request = new StringRequest(Request.Method.POST,"https://emaransac.com/android/resumen_promotor.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        employeeArrayList.clear();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String exito = jsonObject.getString("exito");
                            JSONArray jsonArray = jsonObject.getJSONArray("datos");
                            if(exito.equals("1")){
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id");
                                    String fecha = object.getString("fecha");
                                    String distribuidor = object.getString("distribuidor");
                                    String cliente = object.getString("cliente");
                                    String telefono = object.getString("telefono");
                                    String direccion = object.getString("direccion");
                                    employee = new detalle(id,fecha,distribuidor,cliente,telefono,direccion);
                                    employeeArrayList.add(employee);
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
                Toast.makeText(PromotorActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    public void promotor(View view) {
        startActivity(new Intent(getApplicationContext(), ProVMainActivity.class));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        // Finalizar la actividad actual
        finish();
    }
}