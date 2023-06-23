package com.sparrow.drawernavigation.PromvApp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sparrow.drawernavigation.PromvApp.Entities.Frescos;
import com.sparrow.drawernavigation.R;

import java.util.List;

public class PfAdapter extends RecyclerView.Adapter<PfAdapter.ViewHolder>  {
    private Context context;
    private List<Frescos> pfList;

    public PfAdapter(Context context, List<Frescos> pfList) {
        this.context = context;
        this.pfList = pfList;
    }

    public List<Frescos> getPfList() {
        return pfList;
    }

    // Métodos del adaptador

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frescos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Frescos frescos = pfList.get(position);
        holder.tvID.setText(frescos.getId());
        holder.tvName.setText(frescos.getName());
    }

    @Override
    public int getItemCount() {
        return pfList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvID;
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.txt_store_id);
            tvName = itemView.findViewById(R.id.txt_store_name);
        }
    }
}
