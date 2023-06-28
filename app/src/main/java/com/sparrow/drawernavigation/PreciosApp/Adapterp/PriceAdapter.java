package com.sparrow.drawernavigation.PreciosApp.Adapterp;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sparrow.drawernavigation.PreciosApp.Entity.prices;
import com.sparrow.drawernavigation.PromvApp.Entities.Frescos;
import com.sparrow.drawernavigation.R;
import java.util.ArrayList;
import java.util.List;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.ViewHolder> {

    private final Context context;
    private List<prices> pfList;
    private final ArrayList<prices> productoArrayList;
    private itemClickListener itemClickListener;
    private TextInputListener textInputListener; // Agregado el campo TextInputListener

    public PriceAdapter(Context context, ArrayList<prices> pricesArrayList, TextInputListener textInputListener) {
        this.context = context;
        this.productoArrayList = pricesArrayList;
        this.textInputListener = textInputListener; // Asignado el TextInputListener
        this.pfList = pricesArrayList;
    }

    public ArrayList<prices> getPfList() {
        return productoArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.price_report, parent, false);
        return new ViewHolder(view, textInputListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        prices pri = productoArrayList.get(position);

        holder.txtid.setText(pri.getId());
        holder.txtProduct.setText(pri.getProducto());
        holder.txtFrasco.setText(pri.getFrasco());
        holder.txtGramaje.setText(pri.getGramaje());
        holder.txtPrecio.setText(pri.getPrecio());


        holder.txtPrecio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    holder.txtPrecio.setText("");
                }
            }
        });

        holder.txtGramaje.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    holder.txtGramaje.setText("");
                }
            }
        });


        holder.txtFrasco.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    holder.txtFrasco.setText("");
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return productoArrayList.size();
    }

    public void removeItem(int position) {
        productoArrayList.remove(position);
        notifyItemRemoved(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtid;
        TextView txtProduct;
        TextView txtFrasco;
        TextView txtGramaje;
        TextView txtPrecio;
        Button button;
        itemClickListener itemClickListener;
        TextInputListener textInputListener;

        public ViewHolder(@NonNull View itemView,  TextInputListener textInputListener) {
            super(itemView);

            txtid = itemView.findViewById(R.id.txt_id);
            txtProduct = itemView.findViewById(R.id.txt_producto);
            txtFrasco = itemView.findViewById(R.id.txt_frasco);
            txtGramaje = itemView.findViewById(R.id.txt_gramaje);
            txtPrecio = itemView.findViewById(R.id.txt_precio);

            button = itemView.findViewById(R.id.button);
            this.textInputListener = textInputListener;

            //button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String id = txtid.getText().toString();
            String producto = txtProduct.getText().toString();
            String frasco = txtFrasco.getText().toString();
            String gramaje = txtGramaje.toString();
            String precio = txtGramaje.getText().toString();


            boolean isInternetAvailable = isInternetAvailable(); // Verificar disponibilidad de internet
            boolean isGpsEnabled = isGpsEnabled(); // Verificar si el GPS está activado

            if (isInternetAvailable && isGpsEnabled) {
                    itemClickListener.onItemClick(getAdapterPosition());
                    textInputListener.onTextInputClicked(id,producto,frasco, gramaje, precio);
                    PriceAdapter.this.removeItem(getAdapterPosition());
            } else {
                if (!isInternetAvailable) {
                    Toast.makeText(context, "No hay conexión a internet", Toast.LENGTH_SHORT).show();
                }
                if (!isGpsEnabled) {
                    Toast.makeText(context, "El GPS no está activado", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public interface itemClickListener {
        void onItemClick(int position);
    }

    public interface TextInputListener {
        void onTextInputClicked(String id,String producto, String frasco, String gramaeje, String precio);
    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        return networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
    }

    private boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
