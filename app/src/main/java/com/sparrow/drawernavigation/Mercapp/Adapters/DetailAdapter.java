package com.sparrow.drawernavigation.Mercapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.sparrow.drawernavigation.Mercapp.Entity.Register;
import com.sparrow.drawernavigation.R;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder> {
    private Context context;
    private List<Register> detalleList;

    public DetailAdapter(Context context, List<Register> detalleList) {
        this.context = context;
        this.detalleList = detalleList;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_register, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        Register detalle = detalleList.get(position);
        holder.tvID.setText(detalle.getId());
        holder.tvFecha.setText(detalle.getFecha());
        holder.tvLocal.setText(detalle.getLocal());
        holder.tvMotivo.setText(detalle.getMotivo());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWithOptions(detalle.getId());
            }
        });
    }

    private void showPopupWithOptions(String itemID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Opciones")
                .setMessage("¿Deseas finalizar?")
                .setPositiveButton("Finalizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acción a realizar al finalizar
                        Toast.makeText(context, "Finalizado: " + itemID, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return detalleList.size();
    }

    static class DetailViewHolder extends RecyclerView.ViewHolder {
        TextView tvID;
        TextView tvFecha;
        TextView tvLocal;
        TextView tvMotivo;

        DetailViewHolder(View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.id_txt_r);
            tvFecha = itemView.findViewById(R.id.fecha_txt_r);
            tvLocal = itemView.findViewById(R.id.local_txt_r);
            tvMotivo = itemView.findViewById(R.id.motivo_txt_r);
        }
    }
}