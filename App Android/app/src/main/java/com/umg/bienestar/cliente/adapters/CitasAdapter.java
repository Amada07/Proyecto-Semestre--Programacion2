package com.umg.bienestar.cliente.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.umg.bienestar.cliente.R;
import com.umg.bienestar.cliente.api.ApiClient;
import com.umg.bienestar.cliente.api.ApiService;
import com.umg.bienestar.cliente.models.Cita;
import com.umg.bienestar.cliente.utils.SharedPrefsManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitasAdapter extends RecyclerView.Adapter<CitasAdapter.CitaViewHolder> {

    private Context context;
    private List<Cita> citas;
    private OnCitaCanceladaListener listener;

    public interface OnCitaCanceladaListener {
        void onCitaCancelada();
    }

    public CitasAdapter(Context context, OnCitaCanceladaListener listener) {
        this.context = context;
        this.citas = new ArrayList<>();
        this.listener = listener;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cita, parent, false);
        return new CitaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CitaViewHolder holder, int position) {
        Cita cita = citas.get(position);
        holder.bind(cita);
    }

    @Override
    public int getItemCount() {
        return citas.size();
    }

    class CitaViewHolder extends RecyclerView.ViewHolder {
        TextView tvServicioNombre, tvEstado, tvFechaHora, tvObservaciones;
        MaterialButton btnCancelar;

        public CitaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServicioNombre = itemView.findViewById(R.id.tvServicioNombre);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvFechaHora = itemView.findViewById(R.id.tvFechaHora);
            tvObservaciones = itemView.findViewById(R.id.tvObservaciones);
            btnCancelar = itemView.findViewById(R.id.btnCancelar);
        }

        public void bind(Cita cita) {
            tvServicioNombre.setText(cita.getServicioNombre());

            // Estado
            String estado = cita.getEstado();
            tvEstado.setText(estado);
            tvEstado.setBackgroundColor(getColorEstado(estado));

            // Fecha y hora formateada
            String fechaHoraFormateada = formatearFechaHora(cita.getFechaHora());
            tvFechaHora.setText("📅 " + fechaHoraFormateada);

            // Observaciones
            if (cita.getObservaciones() != null && !cita.getObservaciones().isEmpty()) {
                tvObservaciones.setVisibility(View.VISIBLE);
                tvObservaciones.setText("📝 " + cita.getObservaciones());
            } else {
                tvObservaciones.setVisibility(View.GONE);
            }

            // Botón cancelar (solo si está PENDIENTE o CONFIRMADA)
            if ("PENDIENTE".equals(estado) || "CONFIRMADA".equals(estado)) {
                btnCancelar.setVisibility(View.VISIBLE);
                btnCancelar.setOnClickListener(v -> mostrarDialogoCancelar(cita));
            } else {
                btnCancelar.setVisibility(View.GONE);
            }
        }

        private int getColorEstado(String estado) {
            switch (estado) {
                case "CONFIRMADA":
                    return Color.parseColor("#10B981"); // Verde
                case "PENDIENTE":
                    return Color.parseColor("#F59E0B"); // Amarillo
                case "ATENDIDA":
                    return Color.parseColor("#3B82F6"); // Azul
                case "CANCELADA":
                case "RECHAZADA":
                    return Color.parseColor("#EF4444"); // Rojo
                default:
                    return Color.parseColor("#6B7280"); // Gris
            }
        }

        private String formatearFechaHora(String fechaHora) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.getDefault());
                Date date = inputFormat.parse(fechaHora);
                return outputFormat.format(date);
            } catch (ParseException e) {
                return fechaHora;
            }
        }

        private void mostrarDialogoCancelar(Cita cita) {
            new AlertDialog.Builder(context)
                    .setTitle("Cancelar Cita")
                    .setMessage("¿Está seguro de cancelar esta cita?")
                    .setPositiveButton("Sí, cancelar", (dialog, which) -> cancelarCita(cita.getId()))
                    .setNegativeButton("No", null)
                    .show();
        }

        private void cancelarCita(Long citaId) {
            ApiService apiService = ApiClient.getApiService(context);
            String token = "Bearer " + SharedPrefsManager.getInstance(context).getToken();

            apiService.cancelarCita(token, citaId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Cita cancelada exitosamente", Toast.LENGTH_SHORT).show();
                        listener.onCitaCancelada();
                    } else {
                        Toast.makeText(context, "Error al cancelar cita", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}