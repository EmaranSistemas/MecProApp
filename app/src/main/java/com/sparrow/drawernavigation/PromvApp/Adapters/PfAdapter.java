package com.sparrow.drawernavigation.PromvApp.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sparrow.drawernavigation.PromvApp.Entities.Frescos;
import com.sparrow.drawernavigation.PromvApp.ProVMainActivity;
import com.sparrow.drawernavigation.R;

import java.util.ArrayList;
import java.util.List;

public class PfAdapter extends RecyclerView.Adapter<PfAdapter.ViewHolder>  {
    private Context context;
    private List<Frescos> pfList;
    private List<Frescos> pfList_r;

    public PfAdapter(Context context, List<Frescos> pfList,List<Frescos> pfList_r) {
        this.context = context;
        this.pfList = pfList;
        this.pfList_r = pfList_r;
    }

    public List<Frescos> getPfList() {
        return pfList;
    }
    public List<Frescos> getPfList_r() {
        return pfList_r;
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

        final PfAdapter adapter = this;

        // Obtener el valor actual del EditText
        String editTextValue = holder.valueCount.getText().toString();
        if (!TextUtils.isEmpty(editTextValue)) {
            holder.counterValue = Integer.parseInt(editTextValue);
        } else {
            holder.counterValue = 0;
        }
        holder.valueCount.setText(String.valueOf(holder.counterValue));


        // Obtener el valor actual del EditText
        String editTextSale = holder.valueSale.getText().toString();
        if (!TextUtils.isEmpty(editTextSale)) {
            holder.counterSale = Integer.parseInt(editTextValue);
        } else {
            holder.counterSale = 0;
        }
        holder.valueSale.setText(String.valueOf(holder.counterSale));




        // Configurar un OnClickListener para el EditText
        holder.valueCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.valueCount.setText(""); // Eliminar el valor actual del EditText
            }
        });


        holder.valueSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.valueSale.setText(""); // Eliminar el valor actual del EditText
            }
        });

        // Configurar un Listener para el botón "-"
        holder.btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.counterValue > 0) {
                    holder.counterValue--;
                    holder.valueCount.setText(String.valueOf(holder.counterValue));
                }
                String editTextValue = holder.valueCount.getText().toString();
                // Utilizar editTextValue según tus necesidades
            }
        });

        // Listener para el botón "+"
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.counterValue++;
                holder.valueCount.setText(String.valueOf(holder.counterValue));
                String editTextValue = holder.valueCount.getText().toString();
                // Utilizar editTextValue según tus necesidades
            }
        });

        // Configurar un Listener para el CheckBox
        holder.checkBoxValidado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Frescos frescos = adapter.getPfList_r().get(adapterPosition);
                    frescos.setChecked(isChecked);
                    Log.d("VALUE SALES: ","GOGGGGGOGOOGO");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return pfList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvID;
        TextView tvName;
        int counterValue = 0;
        double counterSale = 0.0;
        Button btnMenos;
        EditText valueCount;
        Button btnAdd;
        EditText valueSale;
        CheckBox checkBoxValidado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.txt_store_id);
            tvName = itemView.findViewById(R.id.txt_store_name);
            btnMenos = itemView.findViewById(R.id.btn_menos);
            valueCount = itemView.findViewById(R.id.value_count);
            btnAdd = itemView.findViewById(R.id.btn_add);
            valueSale = itemView.findViewById(R.id.value_ventas);
            checkBoxValidado = itemView.findViewById(R.id.validado);
        }
    }

    public List<Frescos> getCheckedItems() {
        List<Frescos> checkedItems = new ArrayList<>();

        for (Frescos frescos : pfList) {
            if (frescos.isChecked()) {
                checkedItems.add(frescos);
            }
        }

        return checkedItems;
    }

}
