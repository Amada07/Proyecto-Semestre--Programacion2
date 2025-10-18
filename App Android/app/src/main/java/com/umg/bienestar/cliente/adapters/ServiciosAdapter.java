package com.umg.bienestar.cliente.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.umg.bienestar.cliente.R;
import com.umg.bienestar.cliente.activities.AgendarCitaActivity;
import com.umg.bienestar.cliente.models.Servicio;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServiciosAdapter extends RecyclerView.Adapter<ServiciosAdapter.ServicioViewHolder> {

    private Context context;
    private List<Servicio> servicios;

    public ServiciosAdapter(Context context) {
        this.context = context;
        this.servicios = new ArrayList<>();
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_servicio, parent, false);
        return new ServicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicioViewHolder holder, int position) {
        Servicio servicio = servicios.get(position);
        holder.bind(servicio);
    }

    @Override
    public int getItemCount() {
        return servicios.size();
    }

    class ServicioViewHolder extends RecyclerView.ViewHolder {
        TextView tvCodigo, tvNombre, tvDescripcion, tvPrecio, tvDuracion, tvCupo;
        MaterialButton btnAgendar;

        public ServicioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodigo = itemView.findViewById(R.id.tvCodigo);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvDuracion = itemView.findViewById(R.id.tvDuracion);
            tvCupo = itemView.findViewById(R.id.tvCupo);
            btnAgendar = itemView.findViewById(R.id.btnAgendar);
        }

        public void bind(Servicio servicio) {
            tvCodigo.setText(servicio.getCodigo());
            tvNombre.setText(servicio.getNombre());
            tvDescripcion.setText(servicio.getDescripcion());
            tvPrecio.setText(String.format(Locale.getDefault(), "Q %.2f", servicio.getPrecio()));
            tvDuracion.setText(String.format(Locale.getDefault(), "⏱ %d min", servicio.getDuracionMinutos()));
            tvCupo.setText(String.format(Locale.getDefault(), "👥 Cupo: %d", servicio.getMaxConcurrentes()));

            // Click en agendar
            btnAgendar.setOnClickListener(v -> {
                Intent intent = new Intent(context, AgendarCitaActivity.class);
                intent.putExtra("SERVICIO_ID", servicio.getId());
                intent.putExtra("SERVICIO_NOMBRE", servicio.getNombre());
                context.startActivity(intent);
            });
        }
    }
}