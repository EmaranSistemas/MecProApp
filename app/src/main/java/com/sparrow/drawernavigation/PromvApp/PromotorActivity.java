package com.sparrow.drawernavigation.PromvApp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sparrow.drawernavigation.MainActivity;
import com.sparrow.drawernavigation.Mercapp.Adapters.SummaryAdapter;
import com.sparrow.drawernavigation.Mercapp.MercAppActivity;
import com.sparrow.drawernavigation.Mercapp.Register_Activity;
import com.sparrow.drawernavigation.Mercapp.SummaryActivity;
import com.sparrow.drawernavigation.PromvApp.Adapters.DetalleAdapter;
import com.sparrow.drawernavigation.PromvApp.Entities.detalle;
import com.sparrow.drawernavigation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Acciones a realizar cuando se hace clic en un elemento del ListView
                detalle employee = employeeArrayList.get(position);
                Toast.makeText(PromotorActivity.this,employee.getCliente(),Toast.LENGTH_SHORT).show();
                // Aquí puedes realizar las operaciones deseadas con el objeto "employee" según el elemento clicado
                showAlertDialog(employee.getId(),employee.getDistribuidor(),employee.getCliente(),employee.getTelefono(),employee.getDirrecion(),
                        employee.getNonCommercial(),employee.getVentas(),employee.getObservaciones());
            }
        });

        FloatingActionButton btn2 = findViewById(R.id.register_btn_por);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PromotorActivity.this, RegisterP_Activity.class);
                startActivity(intent);
            }
        });

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
                                    String nombre_comercial = object.getString("nombre_comercial");
                                    String ventas = object.getString("ventas");
                                    String obs = object.getString("observaciones");
                                    employee = new detalle(id,fecha,distribuidor,cliente,telefono,direccion,nombre_comercial,ventas,obs);
                                    //employee = new detalle()
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

    private void showAlertDialog(String id, String distrib,String client,String telef,String direccion,
                                 String Nombre_comercial,String ventas,String Observacines) {

        AlertDialog.Builder builder = new AlertDialog.Builder(PromotorActivity.this);

        SpannableStringBuilder spannableTitle = new SpannableStringBuilder(id + " - "+client);
        spannableTitle.setSpan(new RelativeSizeSpan(0.7f), 0, spannableTitle.length(), 0);

        builder.setTitle("   "+spannableTitle);
        // Agrega los componentes necesarios al cuadro de diálogo (EditTexts, botones, etc.)
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        EditText distribuidor = new EditText(this);
        EditText cliente = new EditText(this);
        EditText telefono = new EditText(this);
        EditText direcion = new EditText(this);
        EditText Nom_comercial = new EditText(this);
        EditText venta = new EditText(this);
        EditText obs = new EditText(this);

        //distribuidor.setText(ped);
        //cliente.setText(inv);

        // Establece la propiedad singleLine en true
        distribuidor.setSingleLine(true);
        cliente.setSingleLine(true);
        telefono.setSingleLine(true);
        direcion.setSingleLine(true);
        Nom_comercial.setSingleLine(true);
        venta.setSingleLine(true);
        obs.setSingleLine(true);


        final TextView dist = new TextView(PromotorActivity.this);
        dist.setText("Distribuidor");
        dist.setTextColor(getResources().getColor(android.R.color.black));
        dist.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMarginStart(20);
        dist.setLayoutParams(params);
        linearLayout.addView(dist);
        //distribuidor.setHint("   "+distrib);
        distribuidor.setText(distrib);
        distribuidor.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        linearLayout.addView(distribuidor);
        distribuidor.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);





        final TextView cli = new TextView(PromotorActivity.this);
        cli.setText("Cliente");
        cli.setTextColor(getResources().getColor(android.R.color.black));
        cli.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams paramscli = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        paramscli.setMarginStart(20);
        cli.setLayoutParams(paramscli);
        linearLayout.addView(cli);
        cliente.setText(client);
        cliente.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        linearLayout.addView(cliente);




        final TextView tele = new TextView(PromotorActivity.this);
        tele.setText("Teléfono");
        tele.setTextColor(getResources().getColor(android.R.color.black));
        tele.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams paramstele = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        paramstele.setMarginStart(20);
        tele.setLayoutParams(paramstele);
        linearLayout.addView(tele);
        telefono.setText(telef);
        telefono.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        linearLayout.addView(telefono);


        // Configura el tipo de entrada de texto como numérico

        cliente.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

        telefono.setInputType(InputType.TYPE_CLASS_NUMBER);
        direcion.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        Nom_comercial.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

        venta.setInputType(InputType.TYPE_CLASS_NUMBER);
        obs.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);




        final TextView dire = new TextView(PromotorActivity.this);
        dire.setText("Dirección");
        dire.setTextColor(getResources().getColor(android.R.color.black));
        dire.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams paramdire = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        paramdire.setMarginStart(20);
        dire.setLayoutParams(paramdire);
        linearLayout.addView(dire);
        direcion.setText(direccion);
        direcion.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        linearLayout.addView(direcion);



        final TextView nomcom = new TextView(PromotorActivity.this);
        nomcom.setText("Nombre Comercial");
        nomcom.setTextColor(getResources().getColor(android.R.color.black));
        nomcom.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams paramcom = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        paramcom.setMarginStart(20);
        nomcom.setLayoutParams(paramcom);
        linearLayout.addView(nomcom);
        Nom_comercial.setText(Nombre_comercial);
        Nom_comercial.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        linearLayout.addView(Nom_comercial);


        final TextView vent = new TextView(PromotorActivity.this);
        vent.setText("Venta");
        vent.setTextColor(getResources().getColor(android.R.color.black));
        vent.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams paramvent = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        paramvent.setMarginStart(20);
        vent.setLayoutParams(paramvent);
        linearLayout.addView(vent);
        venta.setText(ventas);
        venta.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        linearLayout.addView(venta);



        final TextView obss = new TextView(PromotorActivity.this);
        obss.setText("Observaciones");
        obss.setTextColor(getResources().getColor(android.R.color.black));
        obss.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams paramobss = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        paramobss.setMarginStart(20);
        obss.setLayoutParams(paramobss);
        linearLayout.addView(obss);
        obs.setText(Observacines);
        obs.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        linearLayout.addView(obs);

        builder.setView(linearLayout);

        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String distribuidorvalue = distribuidor.getText().toString();
                String clientevalue = cliente.getText().toString();
                String telefonovalue = telefono.getText().toString();
                String direcionvalue = direcion.getText().toString();
                String Nom_comercialvalue = Nom_comercial.getText().toString();
                String ventacalue = venta.getText().toString();
                String obsvalue = obs.getText().toString();


                // Lógica para guardar los cambios realizados en el elemento seleccionado
                //String id, String distrib,String client,String telef,String direccion,
                //                                 String Nombre_comercial,String ventas,String Observacines

                if(distribuidorvalue.isEmpty()){
                    distribuidorvalue = distrib;
                }
                if(clientevalue.isEmpty()){
                    clientevalue = client;
                }

                if(telefonovalue.isEmpty()){
                    telefonovalue = telef;
                }
                if(direcionvalue.isEmpty()){
                    direcionvalue = direccion;
                }

                if(Nom_comercialvalue.isEmpty()){
                    Nom_comercialvalue = Nombre_comercial;
                }
                if(ventacalue.isEmpty()){
                    ventacalue = ventas;
                }
                if(obsvalue.isEmpty()){
                    obsvalue = Observacines;
                }

                actualizar_reporte(id,distribuidorvalue,clientevalue,direcionvalue,Nom_comercialvalue,ventacalue,obsvalue);
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lógica para cancelar la edición del elemento seleccionado
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void actualizar_reporte(String id,String Distribuidor, String Cliente,String Direccion,String NombreComercial, String Ventas,String Observaciones) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Actualizando....");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, "https://emaransac.com/android/actualizar_reporte_promotor_ventas.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(PromotorActivity.this, response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PromotorActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("distribuidor", Distribuidor);
                params.put("cliente", Cliente);
                params.put("direccion", Direccion);
                params.put("nombrecomercial", NombreComercial);
                params.put("ventas", Ventas);
                params.put("observaciones", Observaciones);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PromotorActivity.this);
        requestQueue.add(request);
        retrieveData();
    }
}